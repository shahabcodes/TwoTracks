#!/usr/bin/env bash
#
# Runs the pure-Kotlin unit tests without Gradle.
#
# Why this exists: a cloud session cannot build this project. There is no
# Android SDK, the JDK is usually 21 where the build wants 17, and if the
# environment's network policy denies dl.google.com then Gradle cannot resolve
# the Android Gradle plugin at all — and because the root build declares it,
# not even the plain-Kotlin modules get as far as configuring. Without this
# script such a session has no way to check its own work and has to push and
# wait for CI, which is no gate when CI is unavailable.
#
# core:model, core:planner and core:content depend on nothing but the Kotlin
# stdlib, java.time and JUnit, all of which live on Maven Central. So the
# compiler can be driven directly and those three modules tested anywhere.
#
# What it does NOT cover: the app module. That is Android, Compose and Hilt, so
# ./gradlew :app:testDebugUnitTest and :app:assembleDebug still need a real
# toolchain. Keep logic worth testing in the three modules above and this
# script stays useful.
#
#   ci/jvm-tests.sh              from the repository root
#
set -eo pipefail

KOTLIN=2.1.0
JUNIT=4.13.2
HAMCREST=1.3
COROUTINES=1.9.0
TROVE=1.0.20200330
ANNOTATIONS=24.1.0
CENTRAL=https://repo1.maven.org/maven2

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
WORK="$ROOT/build/jvm-tests"   # under build/, which .gitignore already covers
LIB="$WORK/lib"
mkdir -p "$LIB"

fetch() { # fetch <url>
  local f="$LIB/$(basename "$1")"
  [ -s "$f" ] && return 0
  echo "fetching $(basename "$1")"
  curl -sSfL --max-time 300 -o "$f" "$1"
}

# The compiler jar is not self-contained: these are what it needs to start.
fetch "$CENTRAL/org/jetbrains/kotlin/kotlin-compiler/$KOTLIN/kotlin-compiler-$KOTLIN.jar"
fetch "$CENTRAL/org/jetbrains/kotlin/kotlin-stdlib/$KOTLIN/kotlin-stdlib-$KOTLIN.jar"
fetch "$CENTRAL/org/jetbrains/kotlin/kotlin-reflect/$KOTLIN/kotlin-reflect-$KOTLIN.jar"
fetch "$CENTRAL/org/jetbrains/kotlin/kotlin-script-runtime/$KOTLIN/kotlin-script-runtime-$KOTLIN.jar"
fetch "$CENTRAL/org/jetbrains/kotlin/kotlin-daemon-embeddable/$KOTLIN/kotlin-daemon-embeddable-$KOTLIN.jar"
fetch "$CENTRAL/org/jetbrains/kotlinx/kotlinx-coroutines-core-jvm/$COROUTINES/kotlinx-coroutines-core-jvm-$COROUTINES.jar"
fetch "$CENTRAL/org/jetbrains/intellij/deps/trove4j/$TROVE/trove4j-$TROVE.jar"
fetch "$CENTRAL/org/jetbrains/annotations/$ANNOTATIONS/annotations-$ANNOTATIONS.jar"
fetch "$CENTRAL/junit/junit/$JUNIT/junit-$JUNIT.jar"
fetch "$CENTRAL/org/hamcrest/hamcrest-core/$HAMCREST/hamcrest-core-$HAMCREST.jar"

STDLIB="$LIB/kotlin-stdlib-$KOTLIN.jar"
COMPILER_CP=$(ls "$LIB"/*.jar | grep -vE 'junit|hamcrest' | tr '\n' ':')
TEST_CP="$WORK/main:$STDLIB:$LIB/junit-$JUNIT.jar:$LIB/hamcrest-core-$HAMCREST.jar"

kotlinc() { java -Xmx2g -cp "$COMPILER_CP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler "$@"; }
quiet() { grep -vE "^(warning|info):|JAVA_TOOL_OPTIONS" || true; }

main_roots=(); test_roots=()
for m in core/model core/planner core/content; do
  [ -d "$ROOT/$m/src/main/kotlin" ] && main_roots+=("$ROOT/$m/src/main/kotlin")
  [ -d "$ROOT/$m/src/test/kotlin" ] && test_roots+=("$ROOT/$m/src/test/kotlin")
done

rm -rf "$WORK/main" "$WORK/test"; mkdir -p "$WORK/main" "$WORK/test"

echo "== compiling main =="
kotlinc -no-stdlib -cp "$STDLIB" -jvm-target 17 -d "$WORK/main" "${main_roots[@]}" 2>&1 | quiet

# -Xfriend-paths makes the main output a friend module so tests can reach
# internal declarations, which is what Gradle does for the same source set.
# Without it this script is stricter than the real build and would push
# visibility changes that exist only to satisfy the harness.
echo "== compiling tests =="
kotlinc -no-stdlib -cp "$TEST_CP" -jvm-target 17 -Xfriend-paths="$WORK/main" \
  -d "$WORK/test" "${test_roots[@]}" 2>&1 | quiet

classes=$(find "$WORK/test" -name '*Test.class' | sed "s|$WORK/test/||; s|/|.|g; s|\.class$||" | sort)
[ -n "$classes" ] || { echo "no test classes compiled" >&2; exit 1; }

echo "== running =="
java -cp "$TEST_CP:$WORK/test" org.junit.runner.JUnitCore $classes 2>&1 | grep -v JAVA_TOOL_OPTIONS
