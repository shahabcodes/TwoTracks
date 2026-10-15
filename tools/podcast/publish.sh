#!/usr/bin/env bash
# Publishes the rendered episodes as assets of one GitHub release, which is
# where the app downloads them from on first play.
#
# The release is podcasts-v1, separate from the app's own releases, and the
# CI clean-up step skips any podcasts-* tag so a new app build never deletes
# the audio. Uploading again replaces an episode in place (--clobber), so a
# corrected render needs nothing more than another run of this script.
#
#   tools/podcast/publish.sh                 every rendered episode
#   tools/podcast/publish.sh az-keyvault     one episode
#
# Run tools/podcast/check.py first, and listen to anything it flags.
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
out="$here/out"
tag="podcasts-v1"

if [ "$#" -gt 0 ]; then
  topics=("$@")
else
  # Only finished episodes: not the --lines trials or the kokoro renders.
  mapfile -t topics < <(cd "$out" && ls az-*.ogg | sed 's/\.ogg$//' | grep -v -- '-first[0-9]*$' | grep -v -- '-kokoro$')
fi

if ! gh release view "$tag" >/dev/null 2>&1; then
  gh release create "$tag" --title "AZ-204 podcast audio" --prerelease \
    --notes "Two-host episodes for the AZ-204 topics, read by the app's player. Each topic has an Opus file and a timing file the transcript follows. Not an app release; kept apart from the builds."
fi

files=()
for t in "${topics[@]}"; do
  for ext in ogg json; do
    [ -f "$out/$t.$ext" ] || { echo "missing $out/$t.$ext" >&2; exit 1; }
    files+=("$out/$t.$ext")
  done
done

gh release upload "$tag" "${files[@]}" --clobber
echo "Published ${#topics[@]} episode(s) to $tag."
