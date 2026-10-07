#!/usr/bin/env bash
#
# Setup script for a Claude Code cloud environment, so a session can run the
# real build — :app:assembleDebug and :app:testDebugUnitTest — instead of only
# the Gradle-free gate in ci/jvm-tests.sh.
#
# Paste this into the environment's Setup script (cloud environment menu in the
# session title bar, then Edit), or call it from there once the checkout exists.
#
# IT WILL FAIL WITHOUT ONE NETWORK CHANGE. Under Edit -> Network access, add:
#
#     dl.google.com
#
# That one host serves both the Android SDK packages and Google's Maven, which
# is where the Android Gradle plugin lives. Without it Gradle cannot even
# configure this project, because the root build declares that plugin.
#
# Hosts this script needs and that are already reachable by default:
#   archive.ubuntu.com   JDK 17
#   repo1.maven.org      most dependencies
#   services.gradle.org  the Gradle distribution
#
set -euo pipefail

SDK=/opt/android-sdk
JDK=/usr/lib/jvm/java-17-openjdk-amd64
CMDLINE_TOOLS=commandlinetools-linux-11076708_latest.zip
COMPILE_SDK=35
BUILD_TOOLS=35.0.0

say() { printf '\n== %s\n' "$1"; }

say "JDK 17"
# The build sets jvmToolchain(17) and the image ships 21. api.foojay.io is not
# reachable, so Gradle cannot download a toolchain itself; apt can.
export DEBIAN_FRONTEND=noninteractive
apt-get update -qq
apt-get install -y -qq openjdk-17-jdk-headless unzip
[ -x "$JDK/bin/java" ] || { echo "JDK 17 did not land at $JDK" >&2; exit 1; }
"$JDK/bin/java" -version 2>&1 | grep -v JAVA_TOOL_OPTIONS | head -1

say "Android SDK"
if ! curl -sSf -o /dev/null --max-time 20 "https://dl.google.com/android/repository/repository2-3.xml"; then
  cat >&2 <<'MSG'
dl.google.com is not reachable from this container.

The environment's network policy is denying it. Open the cloud environment
menu in the session title bar, choose Edit, and under Network access add
dl.google.com (leaving "Allow package managers" ticked), then start a new
session. Nothing below this point can run until then.
MSG
  exit 1
fi

mkdir -p "$SDK/cmdline-tools"
curl -sSfL --max-time 600 -o "/tmp/$CMDLINE_TOOLS" "https://dl.google.com/android/repository/$CMDLINE_TOOLS"
unzip -q -o "/tmp/$CMDLINE_TOOLS" -d "$SDK/cmdline-tools"
rm -f "/tmp/$CMDLINE_TOOLS"
# The zip unpacks to cmdline-tools/; sdkmanager insists on being under latest/.
[ -d "$SDK/cmdline-tools/latest" ] || mv "$SDK/cmdline-tools/cmdline-tools" "$SDK/cmdline-tools/latest"

export ANDROID_HOME="$SDK"
export JAVA_HOME="$JDK"
SDKMANAGER="$SDK/cmdline-tools/latest/bin/sdkmanager"

say "SDK licences and packages"
# Accepting licences writes marker files under $SDK/licenses; without them the
# Android plugin refuses to build. `yes` answers each prompt.
yes | "$SDKMANAGER" --licenses > /dev/null 2>&1 || true
"$SDKMANAGER" --install "platform-tools" "platforms;android-$COMPILE_SDK" "build-tools;$BUILD_TOOLS" > /dev/null

say "Making it the default for every shell"
# A setup script's exports die with it, so write them where login shells read.
cat > /etc/profile.d/twotracks-android.sh <<EOF
export ANDROID_HOME=$SDK
export ANDROID_SDK_ROOT=$SDK
export JAVA_HOME=$JDK
export PATH="\$JAVA_HOME/bin:\$ANDROID_HOME/platform-tools:\$PATH"
EOF
chmod 0644 /etc/profile.d/twotracks-android.sh

# Gradle also reads sdk.dir from the checkout, which .gitignore already covers.
for repo in /home/user/twotracks /home/user/TwoTracks "$(pwd)"; do
  if [ -f "$repo/settings.gradle.kts" ]; then
    printf 'sdk.dir=%s\n' "$SDK" > "$repo/local.properties"
    echo "wrote $repo/local.properties"
  fi
done

say "Done"
echo "JDK 17: $JDK"
echo "SDK:    $SDK (platform $COMPILE_SDK, build-tools $BUILD_TOOLS)"
echo
echo "Check it with:  ./gradlew :app:assembleDebug"
