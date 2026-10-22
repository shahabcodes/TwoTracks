"""
Lists the capitalised words in the scripts that the voices have no way to say.

The voices read an acronym however they like: "API" came out as "appy", and
"A P I" as "a pie". render.py respells each one it knows (SAY), joined with
hyphens so it is said letter by letter. This runs every English line through
the same respelling and lists what is still in capitals afterwards, so a new
acronym is caught before a render rather than by a listener. Words meant as
words (SQL keywords, Redis commands, HTTP verbs) are in WORDS.

    python acronyms.py              every episode in podcasts.json
Exits 1 when something needs an entry in render.py's SAY or in WORDS.
"""

import json
import re
import sys
from collections import defaultdict
from pathlib import Path

HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
import render  # noqa: E402  (only numpy and soundfile at import)

# In capitals on purpose, and said as the word they spell.
WORDS = {
    "EXPLAIN", "ANALYZE", "ORDER", "BY", "WHERE", "CREATE", "EXTENSION", "VECTOR", "TOP", "FROM", "SET", "LIMIT",
    "UPDATE", "DELETE", "AND", "VACUUM", "GIN", "FLAT", "GET", "POST", "PUT", "PING", "PONG", "AZURE", "CLIENT",
    "COSMOS", "ENDPOINT", "WAN", "MARS", "REST",
}
STILL_CAPS = re.compile(r"\b[A-Z][A-Z0-9]+s?\b")


def leftovers(lines) -> dict:
    found = defaultdict(set)
    for episode, text in lines:
        spoken = render.say(render.CUE.sub(" ", render.GERMAN.sub(" ", text)))
        for word in STILL_CAPS.findall(spoken):
            if word.rstrip("s") not in WORDS and word not in WORDS:
                found[word].add(episode)
    return found


def main():
    scripts = json.loads(render.SCRIPTS.read_text(encoding="utf-8"))
    lines = [(e["topicId"], l["text"]) for e in scripts for l in e["lines"] if l["host"] != "jonas"]
    found = leftovers(lines)
    for word, episodes in sorted(found.items()):
        print(f"{word}: {', '.join(sorted(episodes)[:4])}{' ...' if len(episodes) > 4 else ''}")
    print(f"{len(found)} capitalised word(s) with no respelling" if found else "Every acronym has a respelling.")
    sys.exit(1 if found else 0)


if __name__ == "__main__":
    main()
