# Uploads the staged episode audio (install.sh leaves it in upload/) to the
# couple space's own folder in Firebase Storage, where only that space's
# members can read it (firebase/storage.rules). Uses the Google Cloud CLI with
# the owner's own sign-in (gcloud auth login); nothing here holds a secret.
#
#   tools/podcast/upload.ps1 -CoupleId <space id>                 everything staged
#   tools/podcast/upload.ps1 -CoupleId <space id> ai-redis sa-hub  some episodes
#
# The space id is the couple document's id in Firestore (couples/<id>).
param(
    [Parameter(Mandatory = $true)][string]$CoupleId,
    [Parameter(ValueFromRemainingArguments = $true)][string[]]$Episodes,
    [string]$Bucket = "gs://two-tracks.firebasestorage.app"
)
$ErrorActionPreference = "Stop"
$upload = Join-Path $PSScriptRoot "upload"
$gcloud = (Get-Command gcloud -ErrorAction SilentlyContinue).Source
if (-not $gcloud) { $gcloud = Join-Path $env:LOCALAPPDATA "Google\Cloud SDK\google-cloud-sdk\bin\gcloud.cmd" }

$files = if ($Episodes) { $Episodes | ForEach-Object { Join-Path $upload "$_.ogg" } } else { (Get-ChildItem $upload -Filter *.ogg).FullName }
# One call per file: a single call with dozens of files has failed part way.
foreach ($f in $files) {
    & $gcloud storage cp --content-type=audio/ogg $f "$Bucket/couples/$CoupleId/podcasts/"
    if ($LASTEXITCODE -ne 0) { throw "upload failed: $f" }
}
"Uploaded $($files.Count) episode(s) to $Bucket/couples/$CoupleId/podcasts/."
