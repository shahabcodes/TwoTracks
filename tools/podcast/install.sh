#!/usr/bin/env bash
# Puts rendered episodes into the app. The app ships each episode's timing
# (its transcript and board work offline from the start) but never its audio:
# the sound is staged in upload/ for upload.sh, which puts it in the couple
# space's folder in Firebase Storage, readable only by that space's members.
#
# Run check.py first and listen to anything it flags; EpisodeAssetsTest holds
# every timing to its script line for line.
#
#   tools/podcast/install.sh                 every rendered episode
#   tools/podcast/install.sh ai-redis        one episode
#   OUT=/d/PA/tools/podcast/out tools/podcast/install.sh ...   renders from another checkout
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
out="${OUT:-$here/out}"
assets="$here/../../app/src/main/assets/podcasts"
upload="$here/upload"

if [ "$#" -gt 0 ]; then
  topics=("$@")
else
  # Only finished episodes: not the --lines trials or the kokoro renders.
  mapfile -t topics < <(cd "$out" && ls az-*.ogg ai-*.ogg sa-*.ogg 2>/dev/null | sed 's/\.ogg$//' | grep -v -- '-first[0-9]*$' | grep -v -- '-kokoro$')
fi

mkdir -p "$assets" "$upload"
for t in "${topics[@]}"; do
  for ext in ogg json; do
    [ -f "$out/$t.$ext" ] || { echo "missing $out/$t.$ext" >&2; exit 1; }
  done
  cp "$out/$t.json" "$assets/$t.json"
  cp "$out/$t.ogg" "$upload/$t.ogg"
done
echo "Installed ${#topics[@]} episode(s): timings in the app ($(ls "$assets"/*.json | wc -l) in all), audio staged in upload/ ($(du -sh "$upload" | cut -f1)). Run upload.sh next."
