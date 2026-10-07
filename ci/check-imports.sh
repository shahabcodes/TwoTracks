#!/usr/bin/env bash
#
# Catches the one compile error a cloud session cannot otherwise see: a file
# in :app using a type from core:model, core:planner or core:content without
# importing it, or importing one that no longer exists.
#
# The app module needs Android, Compose and Hilt to compile, so a session
# without that toolchain learns about a bad import only from CI, four minutes
# and a red build later. This is a rough textual check, not a compiler: it
# knows nothing about scopes or shadowing, so treat a report as "look at this
# line", not as proof. It has caught three real breaks.
#
#   ci/check-imports.sh        from the repository root
#
set -eo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
exec python3 - "$ROOT" <<'PY'
import re, sys, glob, os, collections

root = sys.argv[1]
core_pkgs = ["core/model", "core/planner", "core/content", "core/common"]

# Top-level types each core package exports, and where they live.
home = {}
decl = re.compile(
    r'^\s*(?:@\w+\s+)*(?:public\s+|internal\s+|abstract\s+|sealed\s+|open\s+|data\s+|value\s+|enum\s+|annotation\s+)*'
    r'(?:class|object|interface)\s+([A-Z]\w*)', re.M)
for pkg in core_pkgs:
    for path in glob.glob(f"{root}/{pkg}/src/main/kotlin/**/*.kt", recursive=True):
        src = open(path).read()
        m = re.search(r'^package\s+([\w.]+)', src, re.M)
        if not m:
            continue
        package = m.group(1)
        for d in decl.finditer(src):
            home.setdefault(d.group(1), set()).add(package)

problems = []
for path in glob.glob(f"{root}/app/src/**/*.kt", recursive=True):
    src = open(path).read()
    rel = os.path.relpath(path, root)
    imported = set(re.findall(r'^import ([\w.]+)', src, re.M))
    imported_names = {i.split('.')[-1] for i in imported}
    star = {i[:-2] for i in imported if i.endswith('.*')}

    # An import naming a com.shahaabapps type that no longer exists anywhere.
    for imp in imported:
        if not imp.startswith("com.shahaabapps") or imp.endswith(".*"):
            continue
        name = imp.split('.')[-1]
        if not name[0].isupper():
            continue  # a function or constant: out of scope for this check
        pkg = imp.rsplit('.', 1)[0]
        if name in home and pkg not in home[name]:
            problems.append(f"{rel}: imports {imp}, but {name} lives in {', '.join(sorted(home[name]))}")

    # A core type used in the body with no import for it. Strings, comments and
    # the file's own declarations are stripped first: "Profile," in a subtitle, a
    # local `private fun Callout`, and a generic parameter named B are not uses
    # of a core type, and reporting them teaches you to ignore the tool.
    body = re.sub(r'^import .*$', '', src, flags=re.M)
    body = re.sub(r'"""(?:.|\n)*?"""', ' ', body)
    body = re.sub(r'"(?:\\.|[^"\\])*"', ' ', body)
    body = re.sub(r'//.*$', ' ', body, flags=re.M)
    body = re.sub(r'/\*(?:.|\n)*?\*/', ' ', body)
    local = set(re.findall(r'(?:class|object|interface|fun)\s+([A-Z]\w*)', body))
    for name, pkgs in home.items():
        if len(name) < 2 or name in imported_names or name in local or pkgs & star:
            continue
        if re.search(r'(?<![\w.])' + re.escape(name) + r'\s*[.(<{]', body):
            problems.append(f"{rel}: uses {name} with no import (it lives in {', '.join(sorted(pkgs))})")

for p in sorted(set(problems)):
    print(p)
print(f"\n{len(set(problems))} suspect import(s) across {len(glob.glob(f'{root}/app/src/**/*.kt', recursive=True))} app files")
sys.exit(1 if problems else 0)
PY
