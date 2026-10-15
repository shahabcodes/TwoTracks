#!/usr/bin/env bash
# Puts the rendered episodes into the app, which ships them in the APK and
# plays them from there: no download, and nothing hosted anywhere else.
#
# Copies each finished episode's audio and timing from out/ into
# app/src/main/assets/podcasts. Run check.py first and listen to anything it
# flags; the next build carries whatever is here, and EpisodeAssetsTest holds
# every timing to its script line for line.
#
#   tools/podcast/install.sh                 every rendered episode
#   tools/podcast/install.sh az-keyvault     one episode
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
out="$here/out"
assets="$here/../../app/src/main/assets/podcasts"

if [ "$#" -gt 0 ]; then
  topics=("$@")
else
  # Only finished episodes: not the --lines trials or the kokoro renders.
  mapfile -t topics < <(cd "$out" && ls az-*.ogg | sed 's/\.ogg$//' | grep -v -- '-first[0-9]*$' | grep -v -- '-kokoro$')
fi

mkdir -p "$assets"
for t in "${topics[@]}"; do
  for ext in ogg json; do
    [ -f "$out/$t.$ext" ] || { echo "missing $out/$t.$ext" >&2; exit 1; }
    cp "$out/$t.$ext" "$assets/$t.$ext"
  done
done
echo "Installed ${#topics[@]} episode(s); the app now carries $(ls "$assets"/*.ogg | wc -l), $(du -sh "$assets" | cut -f1) in all."
