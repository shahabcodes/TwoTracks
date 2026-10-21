#!/usr/bin/env bash
# Uploads the staged episode audio (install.sh leaves it in upload/) to the
# couple space's own folder in Firebase Storage, where only that space's
# members can read it (firebase/storage.rules). Uses the Google Cloud CLI with
# the owner's own sign-in (gcloud auth login); nothing here holds a secret.
#
#   COUPLE_ID=<space id> tools/podcast/upload.sh            everything staged
#   COUPLE_ID=<space id> tools/podcast/upload.sh ai-redis   one episode
#
# The space id is the couple document's id in Firestore (couples/<id>).
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
upload="$here/upload"
bucket="${BUCKET:-gs://two-tracks.firebasestorage.app}"
: "${COUPLE_ID:?Set COUPLE_ID to the couple space's id (its document id under couples/ in Firestore).}"

if [ "$#" -gt 0 ]; then
  files=()
  for t in "$@"; do files+=("$upload/$t.ogg"); done
else
  files=("$upload"/*.ogg)
fi

gcloud storage cp --content-type=audio/ogg "${files[@]}" "$bucket/couples/$COUPLE_ID/podcasts/"
echo "Uploaded ${#files[@]} episode(s) to $bucket/couples/$COUPLE_ID/podcasts/."
