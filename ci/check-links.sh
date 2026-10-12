#!/usr/bin/env bash
#
# Fetches every reference url in the three courses and reports the dead ones.
#
# Six ielts.org urls shipped dead: take-a-test/test-format, its four per-paper
# children and .../free-practice-tests. Between them they appeared under all
# 25 IELTS topics, and both of German's two sources had quietly collapsed onto
# the same redirect. Nothing noticed, because a url is a string to the unit
# tests: ContentTest checks the domain and that there are four of them, which
# is everything except whether they work.
#
# This is not part of the gate. It needs the network, and both sites push back
# at a script: goethe.de answers 403 to anything that does not look like a
# browser, and ielts.org answers 429 once you ask too quickly. Neither means
# the page is gone, so they are reported separately and do not fail the run.
# Only 404, 410 and 5xx count as dead. A url reported as unchecked is one to
# open yourself.
#
#   ci/check-links.sh            all three courses
#   ci/check-links.sh goethe     only urls containing a string
#
# ielts.org renders its 404 page with a 404 status, so the code is enough; the
# body check is there for the day it is not.
#
set -eo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
exec python3 - "$ROOT" "${1:-}" <<'PY'
import glob
import os
import re
import sys
import time
import urllib.error
import urllib.request

root, needle = sys.argv[1], sys.argv[2]

BASES = {
    'GOETHE': 'https://www.goethe.de/',
    'GermanA1.GOETHE': 'https://www.goethe.de/',
    'ORG': 'https://ielts.org/',
    'Ielts.ORG': 'https://ielts.org/',
}

# B.ref("title", BASE + "path") or B.ref("title", "https://…")
JOINED = re.compile(r'B\.ref\("([^"]+)",\s*([A-Za-z0-9_.]+)\s*\+\s*"([^"]+)"\)')
PLAIN = re.compile(r'B\.ref\("([^"]+)",\s*"(https://[^"]+)"\)')

AGENT = ('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 '
         '(KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36')

found = {}
for path in glob.glob(os.path.join(root, 'core/content/src/main/kotlin/**/*.kt'), recursive=True):
    src = open(path, encoding='utf-8').read()
    name = os.path.basename(path)
    for title, base, tail in JOINED.findall(src):
        if base in BASES:
            found.setdefault(BASES[base] + tail, (title, name))
    for title, url in PLAIN.findall(src):
        found.setdefault(url, (title, name))

urls = sorted(u for u in found if needle in u)
print('checking %d url(s)' % len(urls))


def fetch(url):
    req = urllib.request.Request(url, headers={
        'User-Agent': AGENT,
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Accept-Language': 'en-GB,en;q=0.9',
    })
    try:
        with urllib.request.urlopen(req, timeout=30) as r:
            return r.status, r.read(40000).decode('utf-8', 'replace')
    except urllib.error.HTTPError as e:
        return e.code, ''
    except Exception as e:                                  # noqa: BLE001
        return 0, str(e)


dead, unchecked = [], []
for i, url in enumerate(urls):
    if i:
        time.sleep(1.5)                     # ielts.org starts refusing above this rate
    status, body = fetch(url)
    if status == 429:                       # throttled: one slower retry
        time.sleep(15)
        status, body = fetch(url)
    soft = 'could not be found' in body or 'couldn’t find the page' in body
    title, where = found[url]
    if status in (403, 429, 0):
        unchecked.append(url)
        print('  ?    %s  %s' % (status or 'ERR', url))
    elif status in (404, 410) or status >= 500 or soft:
        dead.append(url)
        print('  DEAD %s  %s  (%s, %s)' % (status, url, title, where))

if unchecked:
    print('\n%d url(s) the site would not answer for a script. Open them yourself:' % len(unchecked))
    for u in unchecked:
        print('  %s' % u)
if dead:
    print('\n%d dead reference(s).' % len(dead))
    sys.exit(1)
print('\nno dead references')
PY
