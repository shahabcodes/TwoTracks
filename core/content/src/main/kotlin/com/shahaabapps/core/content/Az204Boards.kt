package com.shahaabapps.core.content

/**
 * The boards for the AZ-204 episodes, one for each. Line numbers are each
 * episode's own, from 0 (Az204Podcasts); the comment over a scene quotes the
 * line it starts on. PodcastBoardTest holds every line, id and edge to the
 * script, and DiagramTest holds the diagrams' geometry.
 *
 * Each board is built by a function of its own: built together in one
 * initialiser, they passed the JVM's 64 KB limit on a single method.
 */
internal object Az204Boards {
    private fun appService() = PodcastBoard(
        "az-app-service",
        listOf(
            // 0: "Hi, and welcome back. Today is about putting a web app on the internet..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Running a website yourself means a server to patch and restart; App Service does that job for you.",
                    nodes = listOf(
                        DNode("site", "Your website", 0.2f, 0.3f, w = 0.32f, h = 0.2f),
                        DNode("chores", "Patches, restarts", 0.2f, 0.78f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("svc", "App Service", 0.76f, 0.3f, w = 0.4f, h = 0.3f, kind = NodeKind.CLOUD, sub = "URL, HTTPS"),
                    ),
                    edges = listOf(
                        DEdge("site", "svc", "your code"),
                        DEdge("chores", "site", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("site")),
                    Beat(1, show = setOf("chores"), light = setOf("chores"), card = BoardCard("Keep it running yourself?", "Updates, patches, restarts. A second job.")),
                    Beat(2, show = setOf("svc"), light = setOf("svc"), pulse = "site" to "svc", card = BoardCard("App Service", "Hand it your code; get a URL, HTTPS, scaling and patching. No server to manage.")),
                ),
            ),
            // 3: "What's a virtual machine, in plain words?"
            Scene(
                3, "The shelf and the cake",
                DiagramSpec(
                    "Every app has two parts: the App Service plan underneath, and the web app that runs on it.",
                    nodes = listOf(
                        DNode("app", "Web app", 0.5f, 0.16f, w = 0.44f, h = 0.2f, sub = "the cake: your code"),
                        DNode("w1", "Worker", 0.2f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w2", "Worker", 0.5f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w3", "Worker", 0.8f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("plan", "App Service plan", 0.5f, 0.84f, w = 0.9f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "region, OS, tier, workers"),
                    ),
                    edges = listOf(
                        DEdge("app", "w2"),
                        DEdge("plan", "w2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, card = BoardCard("A well-run bakery", "You bring the cake, your app. The bakery owns the ovens and the front door.")),
                    Beat(8, show = setOf("plan", "w1", "w2", "w3"), light = setOf("plan"), pulse = "plan" to "w2", card = BoardCard("App Service plan", "The shelf: region, Windows or Linux, pricing tier, and how many workers.")),
                    Beat(10, show = setOf("app"), light = setOf("app"), pulse = "app" to "w2", card = BoardCard("Web app", "Code, settings, domain, slots. A recipe blog: one plan, one web app.")),
                ),
            ),
            // 11: "Can two apps share one shelf?"
            Scene(
                11, "Sharing a plan",
                DiagramSpec(
                    "Apps on one plan share its workers, so apps that must scale differently need separate plans.",
                    nodes = listOf(
                        DNode("store", "Store website", 0.25f, 0.18f, w = 0.4f, h = 0.2f),
                        DNode("admin", "Admin site", 0.75f, 0.18f, w = 0.36f, h = 0.2f, tone = 1),
                        DNode("plan", "One plan", 0.5f, 0.56f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "shared workers"),
                        DNode("two", "Grow differently? Two plans", 0.5f, 0.88f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("store", "plan"),
                        DEdge("admin", "plan"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("store", "admin")),
                    Beat(12, show = setOf("plan"), light = setOf("plan"), pulse = "admin" to "plan", card = BoardCard("A small online shop", "Store site and admin site share one plan. Cheaper, but they share workers.")),
                    Beat(14, show = setOf("two"), light = setOf("two"), card = BoardCard("Workers live on the plan", "Apps that must scale differently need two plans.")),
                ),
            ),
            // 15: "You said pricing tier. What is that?"
            Scene(
                15, "Pricing tiers",
                DiagramSpec(
                    "The pricing tiers from Free to Isolated; slots and auto scale begin at Standard.",
                    nodes = listOf(
                        DNode("free", "Free, Shared", 0.3f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "trying things out"),
                        DNode("basic", "Basic", 0.3f, 0.3f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "scale by hand"),
                        DNode("std", "Standard", 0.3f, 0.5f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, sub = "most real apps"),
                        DNode("prem", "Premium", 0.3f, 0.7f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "more power"),
                        DNode("iso", "Isolated", 0.3f, 0.9f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "your own"),
                        DNode("slots", "Slots, auto scale", 0.8f, 0.5f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "start here"),
                    ),
                    edges = listOf(
                        DEdge("slots", "std"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("free", "basic", "std"), light = setOf("std"), card = BoardCard("A tier is a price level", "Basic: your own workers, scale by hand. Standard: auto scale and slots.")),
                    Beat(20, show = setOf("prem", "iso"), light = setOf("prem", "iso"), card = BoardCard("Which tier?", "A hobby project: Free. A shop taking real orders: Standard.")),
                    Beat(22, show = setOf("slots"), light = setOf("std", "slots"), pulse = "slots" to "std", card = BoardCard("Deployment slot", "A second live copy to test in. Cheapest tier with slots: Standard.")),
                ),
            ),
            // 23: "Got it. Now, my app needs a database password. Where does that go?"
            Scene(
                23, "App settings",
                DiagramSpec(
                    "An app setting is given to the app as an environment variable at start-up, so saving one restarts the app.",
                    nodes = listOf(
                        DNode("set", "App setting", 0.24f, 0.22f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a name and a value"),
                        DNode("env", "Environment variable", 0.5f, 0.58f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("app", "Your app", 0.76f, 0.22f, w = 0.36f, h = 0.2f),
                        DNode("restart", "Save means restart", 0.5f, 0.88f, w = 0.5f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("set", "env"),
                        DEdge("env", "app", "at start-up"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("app")),
                    Beat(24, show = setOf("set"), light = setOf("set"), card = BoardCard("Not in your code", "Put the database password in an app setting, not in a file you commit.")),
                    Beat(26, show = setOf("env"), light = setOf("env"), pulse = "env" to "app", card = BoardCard("A to-do app", "Reads its database address from a setting. Change it in the portal, no code.")),
                    Beat(28, show = setOf("restart"), light = setOf("restart"), card = BoardCard("Saving restarts the app", "So don't change settings at the busiest moment of the day.")),
                ),
            ),
            // 29: "Noted. And when something breaks, where do I look?"
            Scene(
                29, "When something breaks",
                DiagramSpec(
                    "App Service keeps application logs, web server logs and failed request traces; log stream shows them live.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.22f, 0.3f, w = 0.34f, h = 0.2f),
                        DNode("logs", "Logs", 0.76f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("stream", "Log stream", 0.5f, 0.8f, w = 0.4f, kind = NodeKind.PILL, sub = "watch live"),
                    ),
                    edges = listOf(
                        DEdge("app", "logs", "prints"),
                        DEdge("logs", "stream"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("app")),
                    Beat(30, show = setOf("logs", "stream"), light = setOf("logs"), pulse = "app" to "logs", card = BoardCard("File system logging", "Switches itself off after twelve hours, so it never fills the disk.")),
                ),
            ),
            // 31: "Here's one I've hit. The app sits idle overnight, and the first visit ..."
            Scene(
                31, "The slow first visit",
                DiagramSpec(
                    "An idle app is unloaded and must start again; Always On, from Basic up, keeps it loaded.",
                    nodes = listOf(
                        DNode("user", "First visitor", 0.14f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("app", "Sleeping app", 0.72f, 0.3f, w = 0.4f, h = 0.2f, tone = 2),
                        DNode("on", "Always On", 0.72f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "Basic and above"),
                        DNode("big", "Bigger worker? No", 0.26f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("user", "app", "slow start"),
                        DEdge("on", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("user", "app"), pulse = "user" to "app"),
                    Beat(32, show = setOf("on"), light = setOf("on"), pulse = "on" to "app", card = BoardCard("A dentist's booking site", "Quiet all night, so the first patient waits. Always On keeps it loaded.")),
                    Beat(34, show = setOf("big"), light = setOf("big"), card = BoardCard("Not a bigger worker", "Bigger makes a running app faster; it doesn't wake a sleeping one.")),
                ),
            ),
            // 35: "What do people usually get wrong?"
            Scene(
                35, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get App Service wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("secret", "Secrets in a settings file", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("basic", "Slots on Basic", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("restart", "Forgetting the restart", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("case", "Linux names are exact", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("secret", "basic", "restart", "case"), card = BoardCard("Worked on Windows, empty on Linux?", "Match the setting's name exactly, capital letters and all.")),
                ),
            ),
            // 37: "So what should I remember for the exam?"
            Scene(
                37, "For the exam",
                DiagramSpec(
                    "The exam's App Service questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("scale", "Scale differently", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "two plans"),
                        DNode("slots", "Cheapest with slots", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Standard"),
                        DNode("secret", "Secret in the code", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "app setting"),
                        DNode("idle", "Slow after idle", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Always On"),
                        DNode("restart", "Setting changed", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "the app restarts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("scale", "slots", "secret", "idle"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, show = setOf("restart"), light = setOf("restart"), card = BoardCard("Saved a setting?", "Expected: settings are read at start-up, so the app restarts.")),
                    Beat(42, card = BoardCard("Today", "Free Linux web app, setting Hello=World, watch Log stream, browse, delete.")),
                ),
            ),
        ),
    )

    private fun slotsAndScaling() = PodcastBoard(
        "az-slots-scaling",
        listOf(
            // 0: "Hi, and welcome back. Today is about two things every live app needs. ..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Real users are on the live app right now; uploading over it means everyone sees any breakage.",
                    nodes = listOf(
                        DNode("users", "Users", 0.14f, 0.4f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Live app", 0.72f, 0.4f, w = 0.4f, h = 0.22f, sub = "version 1"),
                        DNode("v2", "Version 2?", 0.72f, 0.84f, w = 0.36f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("users", "prod"),
                        DEdge("v2", "prod", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("users", "prod"), pulse = "users" to "prod"),
                    Beat(1, show = setOf("v2"), light = setOf("v2"), card = BoardCard("Upload it fast and hope?", "If it's broken, everyone sees it broken.")),
                ),
            ),
            // 2: "And there's no quick way back. The answer is a deployment slot. A slot..."
            Scene(
                2, "A second live copy",
                DiagramSpec(
                    "A deployment slot is a second live copy of the app, with its own address, for testing the new version.",
                    nodes = listOf(
                        DNode("users", "Users", 0.14f, 0.26f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.7f, 0.26f, w = 0.44f, h = 0.2f, sub = "the stage"),
                        DNode("stage", "Staging slot", 0.7f, 0.74f, w = 0.44f, h = 0.2f, tone = 1, sub = "the rehearsal room"),
                        DNode("you", "You test", 0.14f, 0.74f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                    ),
                    edges = listOf(
                        DEdge("users", "prod"),
                        DEdge("you", "stage"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("users", "prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy with its own web address. New version goes here first.")),
                    Beat(4, show = setOf("you"), light = setOf("you", "stage"), pulse = "you" to "stage", card = BoardCard("A dress rehearsal", "Production is the stage; staging is the rehearsal room next door.")),
                ),
            ),
            // 5: "What does swap actually mean?"
            Scene(
                5, "The swap",
                DiagramSpec(
                    "A swap warms staging, then switches which slot answers the production address; the old version goes to staging.",
                    nodes = listOf(
                        DNode("users", "Users", 0.14f, 0.5f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.66f, 0.2f, w = 0.48f, h = 0.2f, sub = "v1 before, v2 after"),
                        DNode("stage", "Staging slot", 0.66f, 0.8f, w = 0.48f, h = 0.2f, tone = 1, sub = "v2 before, v1 after"),
                    ),
                    edges = listOf(
                        DEdge("users", "prod", "your address"),
                        DEdge("prod", "stage", "swap", dashed = true, bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, light = setOf("stage"), pulse = "stage" to "prod", card = BoardCard("Swap", "Warm up staging, then switch which copy answers. No restart for users.")),
                    Beat(8, light = setOf("prod", "stage"), pulse = "prod" to "stage", card = BoardCard("A shop's broken checkout", "Swap back in seconds, with no redeploy. The routing moves, not the files.")),
                ),
            ),
            // 9: "An undo button for releases. But what about my settings? Do they move ..."
            Scene(
                9, "What moves, what sticks",
                DiagramSpec(
                    "Ordinary settings move with the code in a swap; a sticky deployment slot setting stays with its slot.",
                    nodes = listOf(
                        DNode("prod", "Production", 0.26f, 0.18f, w = 0.44f, h = 0.2f),
                        DNode("stage", "Staging slot", 0.26f, 0.72f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("moves", "App settings", 0.78f, 0.45f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "move with code"),
                        DNode("realdb", "Real database", 0.78f, 0.13f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER),
                        DNode("stays", "Domains, scale, Always On", 0.76f, 0.84f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("prod", "realdb", "sticky"),
                        DEdge("stage", "moves", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("prod", "stage")),
                    Beat(10, show = setOf("moves"), light = setOf("moves"), card = BoardCard("Sticky setting", "A deployment slot setting. It stays with its slot during a swap.")),
                    Beat(12, show = setOf("realdb"), light = setOf("realdb"), pulse = "prod" to "realdb", card = BoardCard("A recipe app's two databases", "Production must keep the real one: mark that connection string sticky.")),
                    Beat(14, show = setOf("stays"), light = setOf("stays"), card = BoardCard("Mark it before the swap", "After is too late: the first swap already carried the staging value.")),
                ),
            ),
            // 15: "Can the swap happen on its own?"
            Scene(
                15, "Auto swap and a test group",
                DiagramSpec(
                    "Auto swap swaps once a deployment to the slot finishes; Traffic percent sends a share of users to staging.",
                    nodes = listOf(
                        DNode("deploy", "Deploy done", 0.2f, 0.2f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("stage", "Staging slot", 0.7f, 0.2f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("users", "Users", 0.14f, 0.7f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.7f, 0.7f, w = 0.44f, h = 0.2f, sub = "9 in 10 visitors"),
                    ),
                    edges = listOf(
                        DEdge("deploy", "stage", "auto swap"),
                        DEdge("users", "prod"),
                        DEdge("users", "stage", "1 in 10", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("deploy", "stage"), light = setOf("deploy"), pulse = "deploy" to "stage", card = BoardCard("Auto swap", "Swaps when a deployment to the slot finishes, after the warm-up.")),
                    Beat(18, show = setOf("users", "prod"), light = setOf("stage"), pulse = "users" to "stage", card = BoardCard("Traffic percent", "Ten percent of visitors try the new version; a cookie keeps them there.")),
                ),
            ),
            // 19: "Nice. Okay, part two. The crowd."
            Scene(
                19, "Up or out",
                DiagramSpec(
                    "Scale up makes each worker bigger; scale out adds more workers behind the same address.",
                    nodes = listOf(
                        DNode("small", "Small worker", 0.22f, 0.2f, w = 0.34f, tone = 2),
                        DNode("big", "Big worker", 0.22f, 0.66f, w = 0.4f, h = 0.28f, sub = "scale up"),
                        DNode("one", "Worker", 0.74f, 0.2f, tone = 2),
                        DNode("o1", "Worker", 0.6f, 0.6f, w = 0.24f),
                        DNode("o2", "Worker", 0.87f, 0.6f, w = 0.24f),
                        DNode("out", "scale out", 0.74f, 0.88f, w = 0.3f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("small", "big", dashed = true),
                        DEdge("one", "o1", dashed = true),
                        DEdge("one", "o2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("small", "big", "one", "o1", "o2", "out"), pulse = "small" to "big", card = BoardCard("Two ways to grow", "Up: a bigger worker, higher tier. Out: more workers, same tier.")),
                    Beat(22, light = setOf("big", "o1", "o2"), pulse = "one" to "o2", card = BoardCard("Photo app or busy blog?", "Heavier requests: scale up. Just more of them: scale out.")),
                ),
            ),
            // 23: "Any catch with scaling out?"
            Scene(
                23, "Keep state shared",
                DiagramSpec(
                    "With several workers, keep state like a shopping basket in a shared cache or database, not in one worker's memory.",
                    nodes = listOf(
                        DNode("user", "Shopper", 0.14f, 0.4f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("w1", "Worker 1", 0.54f, 0.18f, w = 0.3f),
                        DNode("w2", "Worker 2", 0.54f, 0.62f, w = 0.3f),
                        DNode("cache", "Shared cache", 0.86f, 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("user", "w1"),
                        DEdge("user", "w2"),
                        DEdge("w1", "cache"),
                        DEdge("w2", "cache"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("user", "w1", "w2")),
                    Beat(24, show = setOf("cache"), light = setOf("cache"), pulse = "w2" to "cache", card = BoardCard("Lost basket?", "Each worker has its own memory. Keep state in a cache or database.")),
                ),
            ),
            // 25: "That one has bitten me. Can scaling happen on its own too?"
            Scene(
                25, "Scale rules",
                DiagramSpec(
                    "Auto scale rules sit on the plan, with a minimum, a maximum and a cool-down so they do not flap.",
                    nodes = listOf(
                        DNode("rule", "Scale rule", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "CPU over 70 percent"),
                        DNode("plan", "App Service plan", 0.5f, 0.56f, w = 0.8f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "owns the workers"),
                        DNode("cool", "Cool-down", 0.76f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "time between"),
                        DNode("gap", "Out at 70, in at 30", 0.5f, 0.88f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rule", "plan"),
                        DEdge("cool", "plan", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("rule"), light = setOf("rule"), card = BoardCard("Scale rule", "A condition that adds or removes workers: CPU, queue length or a schedule.")),
                    Beat(28, show = setOf("plan", "cool"), light = setOf("plan", "cool"), pulse = "rule" to "plan", card = BoardCard("Rules live on the plan", "Set a minimum, a maximum and a cool-down.")),
                    Beat(30, show = setOf("gap"), light = setOf("gap"), card = BoardCard("Stop the flapping", "Scale in gentler than out. The same number both ways flaps.")),
                ),
            ),
            // 31: "When is a schedule better than a processor rule?"
            Scene(
                31, "A schedule rule",
                DiagramSpec(
                    "When the rush is predictable, a schedule rule adds a worker before it starts and removes it after.",
                    nodes = listOf(
                        DNode("clock", "School days, 4 to 6", 0.28f, 0.26f, w = 0.48f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("plan", "Plan", 0.78f, 0.26f, w = 0.32f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("w1", "Worker", 0.6f, 0.72f),
                        DNode("w2", "Extra worker", 0.86f, 0.72f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("clock", "plan"),
                        DEdge("plan", "w1"),
                        DEdge("plan", "w2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, light = setOf("clock", "w2"), pulse = "plan" to "w2", card = BoardCard("A homework help site", "Busy at four each school day: add a worker before, remove it at six.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's slots and scaling questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("db", "Keep prod's database", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "slot setting"),
                        DNode("auto", "Swap when deployed", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "auto swap"),
                        DNode("out", "Traffic doubles", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "scale out"),
                        DNode("flap", "Rule keeps flapping", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "cool-down"),
                        DNode("swap", "What a swap changes", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "routing, not files"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("db", "auto", "out", "flap"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("swap"), light = setOf("swap"), card = BoardCard("A swap changes routing", "The warmed staging copy answers production. Rules sit on the plan.")),
                    Beat(38, card = BoardCard("Today", "Add a staging slot, change the title there, swap, check, swap back, delete.")),
                ),
            ),
        ),
    )

    private fun functions() = PodcastBoard(
        "az-functions",
        listOf(
            // 0: "Hi, and welcome back. Today is Azure Functions. Here's a question to s..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A whole web app waiting all day for a photo upload; a function runs only when the upload happens.",
                    nodes = listOf(
                        DNode("user", "Uploader", 0.14f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("web", "Web app, waiting", 0.72f, 0.3f, w = 0.44f, h = 0.2f, tone = 2),
                        DNode("fn", "A function", 0.72f, 0.78f, w = 0.44f, h = 0.2f, sub = "runs when needed"),
                    ),
                    edges = listOf(
                        DEdge("user", "web", dashed = true),
                        DEdge("user", "fn", "upload"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("user", "web"), light = setOf("web")),
                    Beat(1, card = BoardCard("Waiting all day?", "A lot of waiting for little work, and you pay for the waiting.")),
                    Beat(2, show = setOf("fn"), light = setOf("fn"), pulse = "user" to "fn", card = BoardCard("Azure Functions", "Small code that runs when something happens. Pay per run, no server.")),
                ),
            ),
            // 3: "So what decides when it runs?"
            Scene(
                3, "One trigger",
                DiagramSpec(
                    "Exactly one trigger starts a function: an HTTP request, a timer, a queue message, a blob, and more.",
                    nodes = listOf(
                        DNode("trig", "Trigger", 0.22f, 0.2f, w = 0.36f, h = 0.2f, kind = NodeKind.PILL, sub = "the doorbell"),
                        DNode("fn", "Your function", 0.74f, 0.2f, w = 0.4f, h = 0.22f),
                        DNode("http", "HTTP", 0.2f, 0.6f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("timer", "Timer", 0.52f, 0.6f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("queue", "Queue", 0.82f, 0.6f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("blob", "Blob", 0.2f, 0.86f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("cosmos", "Cosmos DB", 0.52f, 0.86f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("events", "Events", 0.82f, 0.86f, w = 0.28f, h = 0.14f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("trig", "fn", "starts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("fn")),
                    Beat(4, show = setOf("trig"), light = setOf("trig"), pulse = "trig" to "fn", card = BoardCard("Exactly one trigger", "The one event that starts the function. One doorbell, never two.")),
                    Beat(6, show = setOf("http", "timer", "queue", "blob", "cosmos", "events"), light = setOf("http", "timer", "queue", "blob")),
                ),
            ),
            // 9: "Give me an app for some of those."
            Scene(
                9, "Which trigger when",
                DiagramSpec(
                    "Simple apps and the trigger each one would use.",
                    nodes = listOf(
                        DNode("photo", "Photo uploaded", 0.26f, 0.18f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("blob", "Blob trigger", 0.78f, 0.18f, w = 0.38f, kind = NodeKind.PILL),
                        DNode("form", "Contact form", 0.26f, 0.5f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("http", "HTTP trigger", 0.78f, 0.5f, w = 0.38f, kind = NodeKind.PILL),
                        DNode("remind", "Evening reminder", 0.26f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("timer", "Timer trigger", 0.78f, 0.82f, w = 0.38f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("photo", "blob"),
                        DEdge("form", "http"),
                        DEdge("remind", "timer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, light = setOf("photo", "blob"), pulse = "photo" to "blob", card = BoardCard("A photo sharing app", "Upload a picture; a blob trigger makes a thumbnail.")),
                ),
            ),
            // 11: "And if my function reads from one place and writes to another? Isn't t..."
            Scene(
                11, "Bindings",
                DiagramSpec(
                    "One trigger starts the function; input bindings bring data in and output bindings send results out.",
                    nodes = listOf(
                        DNode("trig", "Trigger", 0.16f, 0.5f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("fn", "Your function", 0.5f, 0.5f, w = 0.32f, h = 0.24f),
                        DNode("in", "Input binding", 0.5f, 0.13f, w = 0.4f, h = 0.18f, tone = 1, sub = "read a Cosmos doc"),
                        DNode("out", "Output binding", 0.84f, 0.84f, w = 0.3f, h = 0.24f, tone = 1, sub = "write to a queue"),
                    ),
                    edges = listOf(
                        DEdge("trig", "fn"),
                        DEdge("in", "fn"),
                        DEdge("fn", "out"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("trig", "fn")),
                    Beat(12, show = setOf("in", "out"), light = setOf("in", "out"), pulse = "in" to "fn", card = BoardCard("Binding", "A ready-made connection to a service. No connection code to write.")),
                    Beat(14, light = setOf("trig", "in", "out"), pulse = "fn" to "out", card = BoardCard("Read Cosmos, write a queue", "Still one trigger: an input binding and an output binding.")),
                ),
            ),
            // 15: "Tell me about the timer. I hear the schedule format is odd."
            Scene(
                15, "The timer's six fields",
                DiagramSpec(
                    "NCRONTAB has six fields, and the first is seconds, not minutes.",
                    nodes = listOf(
                        DNode("sec", "sec", 0.09f, 0.4f, w = 0.15f, h = 0.2f),
                        DNode("min", "min", 0.25f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("hour", "hour", 0.41f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("day", "day", 0.57f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("month", "mon", 0.73f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("week", "wday", 0.9f, 0.4f, w = 0.16f, h = 0.2f, tone = 1),
                        DNode("trap", "Five fields? Wrong", 0.5f, 0.82f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, light = setOf("sec"), card = BoardCard("NCRONTAB", "Six fields: seconds, minutes, hours, day, month, weekday.")),
                    Beat(18, show = setOf("trap"), light = setOf("min", "trap"), card = BoardCard("Every five minutes", "0 */5 * * * *", code = true)),
                ),
            ),
            // 19: "Good trap. What does a real one look like?"
            Scene(
                19, "A reminder sweep",
                DiagramSpec(
                    "A timer starts the sweep; bindings read today's sessions and queue reminders; a queue-triggered function sends them.",
                    nodes = listOf(
                        DNode("timer", "Timer, 4pm", 0.2f, 0.16f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("sweep", "Sweep function", 0.7f, 0.16f, w = 0.4f, h = 0.2f),
                        DNode("queue", "Queue", 0.7f, 0.5f, w = 0.3f, h = 0.22f, kind = NodeKind.CYLINDER),
                        DNode("send", "Send function", 0.24f, 0.5f, w = 0.4f, h = 0.2f),
                        DNode("app", "One function app", 0.5f, 0.86f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "same plan, runtime, settings"),
                    ),
                    edges = listOf(
                        DEdge("timer", "sweep", "trigger"),
                        DEdge("sweep", "queue", "output"),
                        DEdge("queue", "send", "trigger"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("timer", "sweep", "queue", "send"), pulse = "sweep" to "queue", card = BoardCard("Timer, then queue", "Timer triggers the sweep; reminders go on a queue; a second function sends them.")),
                    Beat(22, show = setOf("app"), light = setOf("app"), card = BoardCard("Function app", "The box you deploy: many functions sharing plan, runtime and settings.")),
                ),
            ),
            // 23: "You said plan. Which plans are there?"
            Scene(
                23, "Consumption",
                DiagramSpec(
                    "Consumption scales to zero and bills per run, at the price of cold starts and a ten-minute cap.",
                    nodes = listOf(
                        DNode("plan", "Consumption", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "pay per run"),
                        DNode("zero", "Scales to zero", 0.26f, 0.5f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("cold", "Cold start", 0.76f, 0.5f, w = 0.4f, h = 0.18f, tone = 1, sub = "a cold car"),
                        DNode("cap", "Ten minutes max", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("plan", "zero"),
                        DEdge("plan", "cold"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("plan", "zero", "cold"), light = setOf("cold"), pulse = "plan" to "cold", card = BoardCard("Cold start", "A delay while Azure loads the function after a quiet spell.")),
                    Beat(26, show = setOf("cap"), light = setOf("plan", "cap"), card = BoardCard("A small newsletter tool", "Runs a few times a day: Consumption. Pennies, and a short wait is fine.")),
                ),
            ),
            // 27: "And when the wait does matter?"
            Scene(
                27, "Premium and Dedicated",
                DiagramSpec(
                    "Premium pre-warms and allows private networks; Dedicated runs on an App Service plan and needs Always On for timers.",
                    nodes = listOf(
                        DNode("prem", "Premium", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "pre-warmed"),
                        DNode("pay", "Payments app", 0.26f, 0.62f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 1),
                        DNode("ded", "Dedicated", 0.76f, 0.2f, w = 0.42f, h = 0.22f, sub = "App Service plan"),
                        DNode("on", "Always On", 0.76f, 0.62f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "for timers"),
                    ),
                    edges = listOf(
                        DEdge("pay", "prem"),
                        DEdge("on", "ded"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("prem", "pay"), light = setOf("prem"), pulse = "pay" to "prem", card = BoardCard("Premium", "No cold start, long runs, private network. A payments app: Premium.")),
                    Beat(30, show = setOf("ded", "on"), light = setOf("ded", "on"), pulse = "on" to "ded", card = BoardCard("Dedicated", "Cost is the plan, not the runs. Timers need Always On.")),
                ),
            ),
            // 31: "Asleep on the job."
            Scene(
                31, "Two files",
                DiagramSpec(
                    "The host file configures the whole function app; the local settings file is for your laptop only.",
                    nodes = listOf(
                        DNode("host", "host.json", 0.26f, 0.3f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "whole function app"),
                        DNode("local", "local.settings.json", 0.74f, 0.3f, w = 0.48f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "laptop only"),
                        DNode("never", "Never commit it", 0.74f, 0.78f, w = 0.48f, h = 0.18f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("local", "never", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("host", "local", "never"), light = setOf("local", "never"), card = BoardCard("Local settings hold secrets", "Never deployed. In Azure the same keys live in app settings.")),
                ),
            ),
            // 33: "What do people usually get wrong?"
            Scene(
                33, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Azure Functions wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("cron", "A five-field CRON", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("long", "20 minutes on Consumption", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("commit", "Committing local settings", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("on", "Timer without Always On", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("cron", "long", "commit", "on"), card = BoardCard("A long job?", "Consumption stops it at ten minutes. Use Premium or Durable Functions.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("one", "How many triggers", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "exactly one"),
                        DNode("cold", "No cold start", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("cheap", "Few runs, cheapest", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Consumption"),
                        DNode("long", "Runs 30 minutes", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "not Consumption"),
                        DNode("cron", "A timer schedule", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "six fields, seconds first"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("one", "cold", "cheap", "long"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("cron"), light = setOf("cron"), card = BoardCard("Count the fields", "Six, seconds first: NCRONTAB. Reading or writing is bindings.")),
                    Beat(40, card = BoardCard("Today", "Consumption app, HTTP trigger from the browser, a timer every minute, Monitor.")),
                ),
            ),
        ),
    )

    private fun durable() = PodcastBoard(
        "az-durable",
        listOf(
            // 0: "Hi, and welcome back. Today is about Durable Functions, for work that ..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A workflow asks a manager to approve something, then waits three days for the answer.",
                    nodes = listOf(
                        DNode("wf", "Your workflow", 0.22f, 0.35f, w = 0.34f, h = 0.2f),
                        DNode("mgr", "Manager", 0.78f, 0.35f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("wait", "Three days: doing what?", 0.5f, 0.8f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("wf", "mgr", "approve?"),
                        DEdge("wait", "wf", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("wf", "mgr"), pulse = "wf" to "mgr"),
                    Beat(2, show = setOf("wait"), light = setOf("wait"), card = BoardCard("Sleeping in memory?", "No. It isn't running at all, and when it wakes it starts from the top.")),
                ),
            ),
            // 3: "Okay, back up. What's a Durable Function, in plain words?"
            Scene(
                3, "A function with a memory",
                DiagramSpec(
                    "A plain function does one short job and forgets; a durable one runs a workflow for days and remembers its place.",
                    nodes = listOf(
                        DNode("plain", "Plain function", 0.26f, 0.35f, w = 0.42f, h = 0.24f, tone = 2, sub = "short, then forgets"),
                        DNode("durable", "Durable Functions", 0.74f, 0.35f, w = 0.42f, h = 0.24f, sub = "remembers its place"),
                        DNode("note", "Hours or days, survives restarts", 0.5f, 0.8f, w = 0.66f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("plain")),
                    Beat(4, show = setOf("durable", "note"), light = setOf("durable"), card = BoardCard("Durable Functions", "A workflow of steps that can run for days and survive a restart.")),
                ),
            ),
            // 5: "So a function with a memory. What are the moving parts?"
            Scene(
                5, "Three kinds of function",
                DiagramSpec(
                    "The client starts the workflow, the orchestrator decides the order, and activities do the real work.",
                    nodes = listOf(
                        DNode("client", "Client", 0.18f, 0.2f, w = 0.3f, h = 0.22f, tone = 1, sub = "the waiter"),
                        DNode("orch", "Orchestrator", 0.64f, 0.2f, w = 0.4f, h = 0.22f, sub = "the head chef"),
                        DNode("a1", "Check order", 0.18f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("a2", "Charge card", 0.5f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("a3", "Email receipt", 0.82f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("client", "orch", "start"),
                        DEdge("orch", "a1"),
                        DEdge("orch", "a2"),
                        DEdge("orch", "a3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("client"), light = setOf("client"), card = BoardCard("Client function", "Takes the order and starts the workflow. You get back an instance id.")),
                    Beat(8, show = setOf("orch", "a1", "a2", "a3"), light = setOf("orch"), pulse = "client" to "orch", card = BoardCard("Orchestrator and activities", "The chef decides the order. Activities, the cooks, do the real work.")),
                    Beat(10, light = setOf("a1", "a2", "a3"), pulse = "orch" to "a2", card = BoardCard("Say you're building a shop", "Check the order, charge the card, email a receipt: three activities.")),
                ),
            ),
            // 11: "And where does the chef keep its notes?"
            Scene(
                11, "Where the notes live",
                DiagramSpec(
                    "The orchestrator writes its progress to the task hub, and on replay reads finished steps back from that history.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.2f),
                        DNode("hub", "Task hub", 0.8f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1, sub = "the history"),
                        DNode("replay", "Replay: run again from line one", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "hub", "progress", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("orch")),
                    Beat(12, show = setOf("hub"), light = setOf("hub"), pulse = "orch" to "hub", card = BoardCard("Task hub", "Storage tables and queues: a notebook where progress is written down.")),
                    Beat(14, show = setOf("replay"), light = setOf("replay"), pulse = "hub" to "orch", card = BoardCard("Replay", "Runs from the top, but reads finished steps from the history. Fast.")),
                ),
            ),
            // 15: "That's clever. But I can see a trap coming."
            Scene(
                15, "Same answer every time",
                DiagramSpec(
                    "An orchestrator must be deterministic: the context gives it a safe clock and a safe way to wait.",
                    nodes = listOf(
                        DNode("bad1", "DateTime.Now", 0.25f, 0.28f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "differs each replay"),
                        DNode("good1", "CurrentUtcDateTime", 0.75f, 0.28f, w = 0.46f, h = 0.22f, sub = "the context clock"),
                        DNode("bad2", "Thread.Sleep", 0.25f, 0.74f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "not in the history"),
                        DNode("good2", "Durable timer", 0.75f, 0.74f, w = 0.46f, h = 0.22f, sub = "CreateTimer"),
                    ),
                    edges = listOf(
                        DEdge("bad1", "good1", "instead"),
                        DEdge("bad2", "good2", "instead"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, card = BoardCard("Deterministic", "Same decisions on every replay: no random numbers, no clock, no calls.")),
                    Beat(18, show = setOf("bad1", "good1"), light = setOf("good1"), pulse = "bad1" to "good1", card = BoardCard("C#", "var now = ctx.CurrentUtcDateTime;", code = true)),
                    Beat(20, show = setOf("bad2", "good2"), light = setOf("good2"), pulse = "bad2" to "good2", card = BoardCard("C#", "await ctx.CreateTimer(now.AddDays(3), token);", code = true)),
                ),
            ),
            // 21: "What about charging the card? Could the orchestrator just do that itse..."
            Scene(
                21, "Real work in activities",
                DiagramSpec(
                    "Side effects like a payment belong in an activity, and an activity should be safe to run twice.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.22f, tone = 1, sub = "replays"),
                        DNode("act", "Charge card", 0.78f, 0.3f, w = 0.36f, h = 0.22f, sub = "an activity"),
                        DNode("idem", "Idempotent: twice does no harm", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "act", "call"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("orch", "act")),
                    Beat(22, show = setOf("idem"), light = setOf("act", "idem"), pulse = "orch" to "act", card = BoardCard("A payment call?", "In an activity. In the orchestrator it would run again on every replay.")),
                ),
            ),
            // 24: "It would. Now, the exam names five patterns, and you can tell them by ..."
            Scene(
                24, "Chaining and fan-out",
                DiagramSpec(
                    "Chaining runs steps one after another; fan-out, fan-in runs many at once and combines the results.",
                    nodes = listOf(
                        DNode("o", "Orchestrator", 0.5f, 0.12f, w = 0.42f, h = 0.18f),
                        DNode("r1", "Resize", 0.18f, 0.5f, tone = 1),
                        DNode("r2", "Resize", 0.5f, 0.5f, tone = 1),
                        DNode("r3", "Resize", 0.82f, 0.5f, tone = 1),
                        DNode("gal", "Gallery", 0.5f, 0.87f, w = 0.34f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("o", "r1"),
                        DEdge("o", "r2"),
                        DEdge("o", "r3"),
                        DEdge("r1", "gal"),
                        DEdge("r2", "gal"),
                        DEdge("r3", "gal"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("o"), card = BoardCard("Function chaining", "A, then B, then C. The shop: check the order, charge, email.")),
                    Beat(26, show = setOf("r1", "r2", "r3"), light = setOf("r1", "r2", "r3"), pulse = "o" to "r2", card = BoardCard("Fan-out, fan-in", "A photo app resizes 100 photos at once, waits for all, builds a gallery.")),
                    Beat(27, show = setOf("gal"), light = setOf("gal"), pulse = "r2" to "gal"),
                ),
            ),
            // 28: "The async HTTP API. Picture a small accounting app where a report take..."
            Scene(
                28, "Long work, check back later",
                DiagramSpec(
                    "In the async HTTP API pattern the caller gets an answer at once, plus a status address to poll.",
                    nodes = listOf(
                        DNode("caller", "Caller", 0.14f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("client", "Client", 0.5f, 0.3f, w = 0.28f, h = 0.2f),
                        DNode("work", "Report", 0.85f, 0.3f, h = 0.2f, tone = 1, sub = "ten minutes"),
                        DNode("status", "Status address", 0.5f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "check back later"),
                    ),
                    edges = listOf(
                        DEdge("caller", "client", "start"),
                        DEdge("client", "work"),
                        DEdge("status", "caller", "poll", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("caller", "client", "work", "status"), light = setOf("status"), pulse = "caller" to "client", card = BoardCard("Async HTTP API", "An accounting app's ten-minute report: an answer now, a status to check.")),
                ),
            ),
            // 30: "Monitor is a loop with a durable timer between checks, like looking at..."
            Scene(
                30, "Waiting for a person",
                DiagramSpec(
                    "Human interaction: an external event raced against a durable timer, so the wait cannot last for ever.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.5f, 0.13f, w = 0.42f, h = 0.18f),
                        DNode("event", "External event", 0.23f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "Approved"),
                        DNode("timer", "Durable timer", 0.77f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "three days"),
                        DNode("grant", "Grant", 0.23f, 0.87f, w = 0.3f),
                        DNode("esc", "Escalate", 0.77f, 0.87f, w = 0.3f),
                    ),
                    edges = listOf(
                        DEdge("event", "orch"),
                        DEdge("timer", "orch"),
                        DEdge("event", "grant", "first"),
                        DEdge("timer", "esc", "first"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("orch", "event"), light = setOf("event"), pulse = "event" to "orch", card = BoardCard("Monitor and human interaction", "Monitor: check, wait, repeat. Human: wait for an external event.")),
                    Beat(32, show = setOf("timer", "grant", "esc"), light = setOf("timer"), pulse = "event" to "grant", card = BoardCard("Race it against a timer", "Holiday app: approved first, grant it. Three days first, escalate.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Durable Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("person", "Wait for a person", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "event vs timer"),
                        DNode("many", "Many in parallel", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fan-out, fan-in"),
                        DNode("time", "Needs the time", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "CurrentUtcDateTime"),
                        DNode("wait", "Needs to wait", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "durable timer"),
                        DNode("side", "Payment or email", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "an activity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("person", "many"), card = BoardCard("Listen for the shape", "A person with a time-out, or many at once: each points to one pattern.")),
                    Beat(36, show = setOf("time", "wait", "side"), light = setOf("side"), card = BoardCard("The code traps", "Context clock, durable timers, and real work only in activities.")),
                    Beat(38, card = BoardCard("Today", "Template, POST to HttpStart, read the status, add a 30-second timer.")),
                ),
            ),
        ),
    )

    private fun containers() = PodcastBoard(
        "az-containers",
        listOf(
            // 0: "Hi, and welcome back. Today is containers on Azure. Here's a puzzle to..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "One in ten users should reach the new version while the rest stay on the old one.",
                    nodes = listOf(
                        DNode("users", "Users", 0.14f, 0.45f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("v1", "Old version", 0.72f, 0.22f, w = 0.4f, h = 0.22f, sub = "90 percent"),
                        DNode("v2", "New version", 0.72f, 0.7f, w = 0.4f, h = 0.22f, tone = 1, sub = "10 percent"),
                    ),
                    edges = listOf(
                        DEdge("users", "v1"),
                        DEdge("users", "v2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("users", "v1", "v2"), light = setOf("v2"), pulse = "users" to "v2", card = BoardCard("Which service?", "One in ten users on the new version, the rest on the old one.")),
                ),
            ),
            // 1: "No idea yet. But back up. I know my code, the server it's deployed on,..."
            Scene(
                1, "Without containers",
                DiagramSpec(
                    "Without containers, someone builds a server by hand and copies your build onto it, and it may not match your laptop.",
                    nodes = listOf(
                        DNode("code", "Your build", 0.2f, 0.2f, w = 0.32f, h = 0.18f, tone = 1),
                        DNode("vm", "Server: a VM", 0.7f, 0.3f, w = 0.44f, h = 0.36f, sub = "OS, runtime, libraries"),
                        DNode("laptop", "Your laptop", 0.2f, 0.62f, w = 0.32f, h = 0.18f),
                        DNode("note", "Works on my machine", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("code", "vm", "copy"),
                        DEdge("laptop", "vm", "not the same", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("code", "vm"), light = setOf("vm"), pulse = "code" to "vm", card = BoardCard("By hand", "A VM with an OS, the runtime and libraries installed, then your build.")),
                    Beat(3, show = setOf("laptop", "note"), light = setOf("note"), card = BoardCard("Works on my machine", "The server differs from your laptop, so the same code breaks.")),
                ),
            ),
            // 4: "Everyone has. A container image fixes it. It packs your built code, th..."
            Scene(
                4, "Inside an image",
                DiagramSpec(
                    "An image stacks your code, the runtime and libraries on a slim base image of OS files; the kernel is the host's, shared.",
                    nodes = listOf(
                        DNode("app", "Your built code", 0.5f, 0.1f, w = 0.7f, h = 0.13f, tone = 1),
                        DNode("rt", "Runtime and libraries", 0.5f, 0.26f, w = 0.7f, h = 0.13f, tone = 1),
                        DNode("base", "Base image: OS files", 0.5f, 0.42f, w = 0.7f, h = 0.13f, tone = 1),
                        DNode("kernel", "Host kernel, shared", 0.5f, 0.72f, w = 0.8f, h = 0.14f, tone = 2),
                        DNode("vmnote", "A VM brings its own kernel", 0.5f, 0.92f, w = 0.8f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("base", "kernel", "runs on", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("app", "rt", "base"), light = setOf("base"), card = BoardCard("Container image", "Your built code, the runtime, libraries and a slim OS base image.")),
                    Beat(6, show = setOf("kernel"), light = setOf("kernel"), pulse = "base" to "kernel", card = BoardCard("No kernel inside", "Every container on a machine shares the host's kernel.")),
                    Beat(8, show = setOf("vmnote"), light = setOf("vmnote"), card = BoardCard("Small and quick", "Megabytes, seconds to start. A Linux image needs a Linux host.")),
                ),
            ),
            // 9: "So a running container is what, under the hood? A tiny virtual machine?"
            Scene(
                9, "Dockerfile to container",
                DiagramSpec(
                    "A Dockerfile is the recipe, docker build makes the image, and each running copy of the image is a container.",
                    nodes = listOf(
                        DNode("df", "Dockerfile", 0.22f, 0.18f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "the recipe"),
                        DNode("img", "Image", 0.76f, 0.18f, w = 0.36f, h = 0.2f, sub = "like an APK"),
                        DNode("c1", "Container", 0.25f, 0.75f, w = 0.36f, h = 0.2f, tone = 1, sub = "a process"),
                        DNode("c2", "Container", 0.75f, 0.75f, w = 0.36f, h = 0.2f, tone = 1, sub = "a process"),
                    ),
                    edges = listOf(
                        DEdge("df", "img", "docker build"),
                        DEdge("img", "c1", "run"),
                        DEdge("img", "c2", "run"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("df", "img"), light = setOf("df"), pulse = "df" to "img", card = BoardCard("Dockerfile", "FROM a base image, copy your code, install, set the start command.")),
                    Beat(11, show = setOf("c1", "c2"), light = setOf("c1", "c2"), pulse = "img" to "c1", card = BoardCard("One image, many containers", "Each running copy is a container: one walled-off process.")),
                ),
            ),
            // 12: "That's a good picture. One image, as many containers as you like. Azur..."
            Scene(
                12, "The registry",
                DiagramSpec(
                    "You push images into ACR, or an ACR Task builds them there; whatever runs them pulls them out.",
                    nodes = listOf(
                        DNode("pipe", "Laptop or pipeline", 0.22f, 0.12f, w = 0.4f, h = 0.18f, tone = 1),
                        DNode("task", "ACR Task", 0.78f, 0.12f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("acr", "Container Registry", 0.5f, 0.45f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "ACR"),
                        DNode("runs", "App Service, ACI, Container Apps", 0.5f, 0.76f, w = 0.84f, h = 0.14f),
                        DNode("tiers", "Premium: geo-replication", 0.5f, 0.93f, w = 0.64f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("pipe", "acr", "push"),
                        DEdge("task", "acr", "build"),
                        DEdge("acr", "runs", "pull"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("acr"), light = setOf("acr")),
                    Beat(14, card = BoardCard("Container Registry", "A private, Azure-hosted store for images, like a private Maven repo.")),
                    Beat(16, show = setOf("pipe", "runs"), light = setOf("pipe", "runs"), pulse = "pipe" to "acr", card = BoardCard("Push in, pull out", "Push uploads an image into the registry. Whatever runs it pulls it down.")),
                    Beat(18, show = setOf("task"), light = setOf("task"), pulse = "task" to "acr", card = BoardCard("ACR Task", "az acr build builds the image in Azure and stores it. No Docker needed.")),
                    Beat(20, show = setOf("tiers"), light = setOf("tiers"), card = BoardCard("Three tiers", "Basic 10 GB, Standard 100 GB, Premium 500 GB with geo-replication.")),
                ),
            ),
            // 22: "Premium, and only Premium. Starting on Basic and then needing geo-repl..."
            Scene(
                22, "Run one, right now",
                DiagramSpec(
                    "Container Instances pulls an image and runs it at once, billed by the second.",
                    nodes = listOf(
                        DNode("acr", "Registry", 0.2f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("aci", "Container Instances", 0.72f, 0.3f, w = 0.46f, h = 0.24f, sub = "by the second"),
                        DNode("job", "Nightly recipe export", 0.5f, 0.8f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("acr", "aci", "pull"),
                        DEdge("aci", "job", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("acr", "aci"), light = setOf("aci"), pulse = "acr" to "aci"),
                    Beat(24, show = setOf("job"), light = setOf("job"), pulse = "aci" to "job", card = BoardCard("Say you're building a recipe app", "Its nightly export to a file runs on ACI: start, work, stop.")),
                ),
            ),
            // 25: "Can it run more than one container together?"
            Scene(
                25, "Groups and restarts",
                DiagramSpec(
                    "A container group shares one host, network and storage; the restart policy says what happens when a container stops.",
                    nodes = listOf(
                        DNode("c1", "Container", 0.25f, 0.13f, w = 0.36f, tone = 1),
                        DNode("c2", "Container", 0.75f, 0.13f, w = 0.36f, tone = 1),
                        DNode("shared", "Shared network and storage", 0.5f, 0.42f, w = 0.72f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("always", "Always", 0.18f, 0.78f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("onf", "OnFailure", 0.5f, 0.78f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("never", "Never", 0.82f, 0.78f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("c1", "shared"),
                        DEdge("c2", "shared"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("c1", "c2", "shared"), light = setOf("shared"), card = BoardCard("Container group", "Several containers sharing one machine, network and storage.")),
                    Beat(28, show = setOf("always", "onf", "never"), light = setOf("onf"), card = BoardCard("Restart policy", "Always for services, OnFailure to retry a crash, Never for one-shots.")),
                    Beat(30, card = BoardCard("Keep files, hide secrets", "Mount an Azure Files share. Pass secrets as secure environment variables.")),
                ),
            ),
            // 31: "Okay. Third service. Running many."
            Scene(
                31, "Many services, managed",
                DiagramSpec(
                    "Container Apps runs a set of small services with Kubernetes hidden underneath.",
                    nodes = listOf(
                        DNode("aca", "Container Apps", 0.5f, 0.2f, w = 0.5f, h = 0.24f, sub = "Kubernetes hidden"),
                        DNode("m1", "Menus", 0.18f, 0.72f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("m2", "Orders", 0.5f, 0.72f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("m3", "Payments", 0.82f, 0.72f, w = 0.28f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("aca", "m1"),
                        DEdge("aca", "m2"),
                        DEdge("aca", "m3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("aca", "m1", "m2", "m3"), light = setOf("m1", "m2", "m3"), pulse = "aca" to "m2", card = BoardCard("Picture a food delivery app", "Menus, orders and payments: small services in one managed home.")),
                    Beat(34, light = setOf("aca"), card = BoardCard("What you get", "Scale to zero, HTTPS from the internet, and revisions. No cluster.")),
                ),
            ),
            // 35: "And what's a revision?"
            Scene(
                35, "Revisions and traffic",
                DiagramSpec(
                    "Each deployment is a revision that never changes; traffic can be split across revisions by percentage.",
                    nodes = listOf(
                        DNode("users", "Users", 0.14f, 0.45f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("r1", "Revision 1", 0.72f, 0.22f, w = 0.4f, h = 0.22f, sub = "90 percent"),
                        DNode("r2", "Revision 2", 0.72f, 0.7f, w = 0.4f, h = 0.22f, tone = 1, sub = "10 percent"),
                    ),
                    edges = listOf(
                        DEdge("users", "r1"),
                        DEdge("users", "r2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("r1", "r2"), light = setOf("r1", "r2"), card = BoardCard("Revision", "A frozen snapshot of one deployment. It never changes.")),
                    Beat(37, show = setOf("users"), light = setOf("r2"), pulse = "users" to "r2", card = BoardCard("The puzzle solved", "Container Apps, multiple revision mode, traffic split 90 and 10.")),
                    Beat(38, light = setOf("r1"), pulse = "users" to "r1", card = BoardCard("Rollback", "Point traffic back at the previous revision. Nothing to rebuild.")),
                ),
            ),
            // 39: "How does it scale, when lots of users show up?"
            Scene(
                39, "Scaling with KEDA",
                DiagramSpec(
                    "KEDA watches traffic or a queue and adds or removes replicas between your minimum and maximum.",
                    nodes = listOf(
                        DNode("rules", "HTTP, CPU, memory or a queue", 0.5f, 0.1f, w = 0.74f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("keda", "KEDA", 0.5f, 0.4f, w = 0.36f, h = 0.18f, sub = "the scaler"),
                        DNode("r1", "Replica", 0.18f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Replica", 0.5f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Replica", 0.82f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("min", "Minimum: 0 or 1", 0.5f, 0.92f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rules", "keda"),
                        DEdge("keda", "r1"),
                        DEdge("keda", "r2"),
                        DEdge("keda", "r3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(40, show = setOf("rules", "keda", "r1", "r2", "r3"), light = setOf("keda"), pulse = "keda" to "r2", card = BoardCard("KEDA", "Adds replicas, extra copies of your app, from traffic or a queue.")),
                    Beat(42, show = setOf("min"), light = setOf("min"), card = BoardCard("Minimum replicas", "Zero for a hobby blog. One for a shop's checkout: no cold start.")),
                ),
            ),
            // 43: "And how does the app get its image out of the registry? With a passwor..."
            Scene(
                43, "No password to pull",
                DiagramSpec(
                    "The app pulls its image with a managed identity that holds a pull role on the registry.",
                    nodes = listOf(
                        DNode("app", "Container App", 0.2f, 0.3f, w = 0.34f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, sub = "the app's badge"),
                        DNode("acr", "Registry", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("app", "id", "wears"),
                        DEdge("id", "acr", "AcrPull"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(43, show = setOf("app", "acr")),
                    Beat(44, show = setOf("id"), light = setOf("id"), pulse = "id" to "acr", card = BoardCard("Managed identity, AcrPull", "A badge holding AcrPull: download only. A pipeline that pushes gets AcrPush.")),
                ),
            ),
            // 45: "So what should I remember for the exam?"
            Scene(
                45, "For the exam",
                DiagramSpec(
                    "The exam's container questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("now", "Run it now", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Instances"),
                        DNode("micro", "Revisions, scale to 0", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
                        DNode("build", "Build, no Docker", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "ACR Task"),
                        DNode("retry", "Retry on a crash", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "OnFailure"),
                        DNode("geo", "Geo-replication", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium registry"),
                        DNode("cold", "No cold starts", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "min replicas 1"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(46, show = setOf("now", "micro", "build"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one service or setting.")),
                    Beat(48, show = setOf("retry", "geo", "cold"), light = setOf("retry", "geo", "cold")),
                    Beat(50, card = BoardCard("Today", "Basic registry, build a Dockerfile, run it with Never, read logs, delete.")),
                ),
            ),
        ),
    )

    private fun apim() = PodcastBoard(
        "az-apim",
        listOf(
            // 0: "Hi, and welcome back. Today is API Management. Here's a puzzle to star..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A caller sends too many requests, and the front door has been told to calm them down.",
                    nodes = listOf(
                        DNode("caller", "Busy caller", 0.14f, 0.35f, w = 0.22f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("gw", "Front door", 0.5f, 0.35f, w = 0.3f, h = 0.22f),
                        DNode("api", "Your API", 0.84f, 0.35f, w = 0.28f, h = 0.22f, tone = 1),
                        DNode("q", "Which answer comes back?", 0.5f, 0.8f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("caller", "gw", "too many"),
                        DEdge("gw", "api"),
                        DEdge("q", "gw", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("caller", "gw", "api"), pulse = "caller" to "gw"),
                    Beat(1, show = setOf("q"), light = setOf("q"), card = BoardCard("Go away?", "Not the official wording. The real answer comes later on.")),
                ),
            ),
            // 2: "Not quite. We'll get there. First, an API, in plain words, is a set of..."
            Scene(
                2, "One front door",
                DiagramSpec(
                    "API Management stands in front of your APIs: its gateway takes every call, and a portal documents them.",
                    nodes = listOf(
                        DNode("p1", "Partner", 0.12f, 0.25f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("p2", "Mobile app", 0.12f, 0.68f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("gw", "API Management", 0.48f, 0.46f, w = 0.34f, h = 0.28f, sub = "the gateway"),
                        DNode("be1", "Weather API", 0.85f, 0.25f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("be2", "Users API", 0.85f, 0.68f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("portal", "Developer portal", 0.48f, 0.9f, w = 0.48f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("p1", "gw"),
                        DEdge("p2", "gw"),
                        DEdge("gw", "be1"),
                        DEdge("gw", "be2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("gw", "be1", "be2"), light = setOf("gw"), card = BoardCard("API Management", "One front door in front of all your APIs.")),
                    Beat(4, show = setOf("p1", "p2", "portal"), light = setOf("portal"), pulse = "p1" to "gw", card = BoardCard("Say you're building a weather app", "Partners call the front door, never your servers directly.")),
                    Beat(6, light = setOf("gw"), pulse = "gw" to "be1", card = BoardCard("Gateway", "The doorman: receives every call and runs your policies.")),
                ),
            ),
            // 7: "Okay. I keep tripping on products and subscriptions. What are they?"
            Scene(
                7, "Products and keys",
                DiagramSpec(
                    "A product bundles APIs with its own terms; a subscription gives a caller keys, sent to the gateway.",
                    nodes = listOf(
                        DNode("free", "Free product", 0.26f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "open to anyone"),
                        DNode("partner", "Partner product", 0.76f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "needs approval"),
                        DNode("key", "Subscription key", 0.5f, 0.54f, w = 0.5f, kind = NodeKind.PILL),
                        DNode("gw", "Gateway", 0.5f, 0.86f, w = 0.36f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("partner", "key", "gives", dashed = true),
                        DEdge("key", "gw", "in a header"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("free", "partner"), light = setOf("free", "partner"), card = BoardCard("For example, a fitness app", "A Free product anyone can use, and a Partner one that needs approval.")),
                    Beat(10, show = setOf("key", "gw"), light = setOf("key"), pulse = "key" to "gw", card = BoardCard("Subscription key", "Tells the gateway who the caller is. Proves nothing to your back end.")),
                ),
            ),
            // 12: "Exactly. Now tiers, which are price levels. Consumption is serverless,..."
            Scene(
                12, "Tiers",
                DiagramSpec(
                    "Consumption is pay per call; Premium adds regions, private networks and the self-hosted gateway.",
                    nodes = listOf(
                        DNode("cons", "Consumption", 0.26f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "pay per call"),
                        DNode("dev", "Developer", 0.76f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "testing only"),
                        DNode("std", "Basic, Standard", 0.26f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 1, sub = "real apps"),
                        DNode("prem", "Premium", 0.76f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "regions, networks"),
                        DNode("sh", "Self-hosted gateway", 0.76f, 0.84f, w = 0.44f, h = 0.2f, sub = "on your machines"),
                    ),
                    edges = listOf(
                        DEdge("prem", "sh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("cons", "dev", "std", "prem"), light = setOf("cons", "prem"), card = BoardCard("Consumption", "Serverless, pay per call, quick to start. No portal, no private network.")),
                    Beat(16, show = setOf("sh"), light = setOf("sh"), pulse = "prem" to "sh", card = BoardCard("Self-hosted gateway", "The gateway as a container, on-premises or in another cloud. Premium.")),
                ),
            ),
            // 17: "Okay. Policies. Where the rules live."
            Scene(
                17, "Four sections",
                DiagramSpec(
                    "A policy runs inbound, then backend, then outbound, with on-error when something breaks; base pulls in the wider rules.",
                    nodes = listOf(
                        DNode("in", "Inbound", 0.26f, 0.18f, w = 0.42f, h = 0.22f, sub = "before the back end"),
                        DNode("be", "Backend", 0.76f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "the call itself"),
                        DNode("out", "Outbound", 0.76f, 0.58f, w = 0.42f, h = 0.22f, sub = "on the way back"),
                        DNode("err", "On-error", 0.26f, 0.58f, w = 0.42f, h = 0.22f, tone = 2, sub = "when it breaks"),
                        DNode("base", "base: run the wider rules too", 0.5f, 0.9f, w = 0.74f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("in", "be"),
                        DEdge("be", "out"),
                        DEdge("be", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("in", "be", "out", "err"), light = setOf("in"), pulse = "in" to "be", card = BoardCard("Policy", "A small rule in XML, run in four sections, in order.")),
                    Beat(20, show = setOf("base"), light = setOf("base"), card = BoardCard("XML", "<inbound><base /><rate-limit calls=\"60\" renewal-period=\"60\" /></inbound>", code = true)),
                ),
            ),
            // 22: "Now the pair everyone mixes up. Rate limit and quota. A rate limit sto..."
            Scene(
                22, "Slow down, or go away",
                DiagramSpec(
                    "A rate limit stops short bursts with 429; a quota is a long allowance, and past it the answer is 403.",
                    nodes = listOf(
                        DNode("rl", "Rate limit", 0.26f, 0.22f, w = 0.44f, h = 0.24f, sub = "short window"),
                        DNode("q", "Quota", 0.76f, 0.22f, w = 0.44f, h = 0.24f, sub = "long allowance"),
                        DNode("r429", "429 Too Many Requests", 0.26f, 0.72f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("r403", "403 Forbidden", 0.76f, 0.72f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("rl", "r429", "slow down"),
                        DEdge("q", "r403", "go away"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("rl", "r429"), light = setOf("r429"), pulse = "rl" to "r429", card = BoardCard("Rate limit", "60 calls a minute. Over it: 429, slow down. The puzzle's answer.")),
                    Beat(24, show = setOf("q", "r403"), light = setOf("r403"), pulse = "q" to "r403", card = BoardCard("Ticket rush or monthly plan?", "A sales rush wants a rate limit. A monthly partner plan wants a quota.")),
                    Beat(26, light = setOf("rl", "q"), card = BoardCard("By key", "Count per caller address, per subscription, or any value you like.")),
                ),
            ),
            // 27: "What other policies are worth knowing?"
            Scene(
                27, "Handy policies",
                DiagramSpec(
                    "A few more policies the topic lists, plus policy expressions for a bit of logic.",
                    nodes = listOf(
                        DNode("jwt", "Validate JWT", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "check a token"),
                        DNode("ip", "IP filter", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "allow or block"),
                        DNode("hdr", "Set header", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "add or remove"),
                        DNode("cache", "Caching", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "keep a reply"),
                        DNode("mock", "Mock response", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pretend reply"),
                        DNode("expr", "Policy expression", 0.76f, 0.8f, w = 0.44f, h = 0.2f, sub = "C# with context"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("jwt", "ip", "hdr", "cache", "mock"), light = setOf("mock"), card = BoardCard("Picture a mobile team", "No back end yet? Mock response lets them build their screens now.")),
                    Beat(32, show = setOf("expr"), light = setOf("expr"), card = BoardCard("Policy expression", "@(context.Request.Headers.GetValueOrDefault(\"x-client\",\"\"))", code = true)),
                ),
            ),
            // 33: "You said the back end needs protecting separately."
            Scene(
                33, "Lock the back door",
                DiagramSpec(
                    "The back end accepts only the gateway, which signs in to it with a managed identity.",
                    nodes = listOf(
                        DNode("caller", "Caller", 0.12f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("gw", "Gateway", 0.48f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("be", "Back end", 0.84f, 0.3f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("id", "Managed identity", 0.6f, 0.8f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, sub = "the gateway's badge"),
                    ),
                    edges = listOf(
                        DEdge("caller", "gw"),
                        DEdge("gw", "be", "only path"),
                        DEdge("gw", "id", dashed = true),
                        DEdge("id", "be", "sign in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("caller", "gw", "be"), pulse = "caller" to "gw"),
                    Beat(34, show = setOf("id"), light = setOf("be", "id"), pulse = "gw" to "be", card = BoardCard("Lock the back end", "Accept only the gateway: IP restriction, private network or certificate.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's API Management questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("burst", "Bursts, or 429", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "rate limit"),
                        DNode("allow", "Allowance, or 403", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "quota"),
                        DNode("onprem", "Gateway on-premises", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "self-hosted, Premium"),
                        DNode("base", "Parent rules skipped", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "missing base"),
                        DNode("skip", "Callers skip the gateway", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "lock the back end"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("burst", "allow", "onprem"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one policy or tier.")),
                    Beat(38, show = setOf("base", "skip"), light = setOf("base", "skip"), card = BoardCard("Two more traps", "Keep the base element, and let only the gateway reach the back end.")),
                    Beat(40, card = BoardCard("Today", "Consumption tier, import the sample, rate limit 3 per 60, read the 429.")),
                ),
            ),
        ),
    )

    private fun eventGrid() = PodcastBoard(
        "az-eventgrid",
        listOf(
            // 0: "Hi, and welcome back. Today is about reacting to things that happen. H..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A photo app wants a thumbnail for each upload, but its code would have to keep asking storage for news.",
                    nodes = listOf(
                        DNode("app", "Photo app", 0.2f, 0.25f, w = 0.32f, h = 0.2f),
                        DNode("store", "Storage", 0.78f, 0.25f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("thumb", "Thumbnail code", 0.22f, 0.75f, w = 0.36f, h = 0.2f),
                    ),
                    edges = listOf(
                        DEdge("app", "store", "upload"),
                        DEdge("thumb", "store", "anything new?", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "store"), pulse = "app" to "store"),
                    Beat(1, show = setOf("thumb"), light = setOf("thumb"), pulse = "thumb" to "store", card = BoardCard("Polling", "Asking again and again: anything new? Mostly for nothing.")),
                ),
            ),
            // 2: "Exactly. That's called polling. Asking again and again, anything new? ..."
            Scene(
                2, "Push, not polling",
                DiagramSpec(
                    "Storage tells Event Grid that something happened, and Event Grid pushes the note straight to your code.",
                    nodes = listOf(
                        DNode("store", "Storage", 0.15f, 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("grid", "Event Grid", 0.5f, 0.4f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("thumb", "Thumbnail code", 0.85f, 0.4f, w = 0.28f, h = 0.22f),
                        DNode("note", "An event: this happened", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("store", "grid", "event"),
                        DEdge("grid", "thumb", "push"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("store", "grid", "thumb"), light = setOf("grid")),
                    Beat(4, show = setOf("note"), light = setOf("note"), pulse = "grid" to "thumb", card = BoardCard("Event Grid", "A postman for news. It pushes each this-happened note right away.")),
                ),
            ),
            // 5: "Where do the notes come from?"
            Scene(
                5, "Sources and topics",
                DiagramSpec(
                    "Sources post events to a topic: a system topic for an Azure service, a custom topic for your own app.",
                    nodes = listOf(
                        DNode("src1", "Storage account", 0.22f, 0.25f, w = 0.4f, h = 0.22f, tone = 1, sub = "system topic"),
                        DNode("src2", "Learning app", 0.22f, 0.72f, w = 0.4f, h = 0.22f, tone = 1, sub = "custom topic"),
                        DNode("topic", "Topic", 0.75f, 0.48f, w = 0.42f, h = 0.36f, kind = NodeKind.CLOUD, sub = "a noticeboard"),
                    ),
                    edges = listOf(
                        DEdge("src1", "topic"),
                        DEdge("src2", "topic", "lesson finished"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("topic"), light = setOf("topic"), card = BoardCard("Topic", "The address where events are posted, like a noticeboard.")),
                    Beat(8, show = setOf("src1", "src2"), light = setOf("src1", "src2"), pulse = "src2" to "topic", card = BoardCard("System or custom", "Azure makes system topics. Your own app publishes to a custom topic.")),
                ),
            ),
            // 9: "And how does a note get from the noticeboard to my code?"
            Scene(
                9, "Routes to handlers",
                DiagramSpec(
                    "An event subscription is a route from a topic to one handler: a Function, a webhook or a queue.",
                    nodes = listOf(
                        DNode("topic", "Topic", 0.17f, 0.5f, w = 0.3f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("sub", "Event subscription", 0.5f, 0.5f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL),
                        DNode("h1", "Function", 0.84f, 0.18f, w = 0.3f, tone = 1),
                        DNode("h2", "Webhook", 0.84f, 0.5f, w = 0.3f, tone = 1),
                        DNode("h3", "Queue", 0.84f, 0.82f, w = 0.3f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("topic", "sub"),
                        DEdge("sub", "h1"),
                        DEdge("sub", "h2"),
                        DEdge("sub", "h3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("topic")),
                    Beat(10, show = setOf("sub", "h1", "h2", "h3"), light = setOf("sub"), pulse = "sub" to "h1", card = BoardCard("Event subscription", "A route from a topic to one handler that reacts.")),
                ),
            ),
            // 11: "Does my handler get every single note?"
            Scene(
                11, "Only what you ask for",
                DiagramSpec(
                    "A subject filter on the subscription lets PNG files in the photos folder through and stops the rest.",
                    nodes = listOf(
                        DNode("png", "cat.png", 0.2f, 0.14f, w = 0.3f, tone = 1),
                        DNode("txt", "notes.txt", 0.8f, 0.14f, w = 0.3f, tone = 2),
                        DNode("filter", "Subject filter", 0.5f, 0.5f, w = 0.56f, h = 0.24f, kind = NodeKind.NOTE, sub = "begins: photos, ends: .png"),
                        DNode("thumb", "Thumbnail code", 0.5f, 0.87f, w = 0.42f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("png", "filter"),
                        DEdge("txt", "filter", "skipped", dashed = true),
                        DEdge("filter", "thumb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, light = setOf("filter"), card = BoardCard("Subject filter", "Filter on how the event's path begins and ends. Or by event type.")),
                    Beat(14, show = setOf("png", "txt", "thumb"), light = setOf("png", "thumb"), pulse = "filter" to "thumb", card = BoardCard("The photo app", "Begins with the photos folder, ends with .png. A text file never arrives.")),
                ),
            ),
            // 16: "Much better. Now, a question for you. How many times does a note arriv..."
            Scene(
                16, "At least once",
                DiagramSpec(
                    "Event Grid retries for up to 24 hours, so a handler may see an event twice and must be idempotent.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Your handler", 0.78f, 0.3f, w = 0.36f, h = 0.22f),
                        DNode("idem", "Idempotent", 0.5f, 0.8f, w = 0.5f, h = 0.22f, kind = NodeKind.NOTE, sub = "twice = once"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "retry up to 24h"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, pulse = "grid" to "handler", card = BoardCard("At least once", "Retries for up to 24 hours, so the same event may arrive twice.")),
                    Beat(20, show = setOf("idem"), light = setOf("idem", "handler"), card = BoardCard("Idempotent", "Doing it twice gives the same result as once. Check the event id.")),
                ),
            ),
            // 21: "And notes that never get through at all?"
            Scene(
                21, "Lost property",
                DiagramSpec(
                    "Events that still cannot be delivered after the retries are dead-lettered to a storage container.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Your handler", 0.78f, 0.3f, w = 0.36f, h = 0.24f, tone = 2, sub = "not answering"),
                        DNode("dl", "Dead-letter", 0.5f, 0.78f, w = 0.44f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "a storage container"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "failed", dashed = true),
                        DEdge("grid", "dl", "after retries"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("dl"), light = setOf("dl"), pulse = "grid" to "dl", card = BoardCard("Dead-letter", "Undeliverable events are kept as files, like a lost-property box.")),
                ),
            ),
            // 23: "Can I point Event Grid at any web address I like?"
            Scene(
                23, "The handshake",
                DiagramSpec(
                    "Before trusting a webhook, Event Grid sends a validation code that the webhook must send back.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.35f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("hook", "Your webhook", 0.78f, 0.35f, w = 0.36f, h = 0.22f),
                        DNode("fn", "Function trigger", 0.5f, 0.82f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "does it for you"),
                    ),
                    edges = listOf(
                        DEdge("grid", "hook", "validation code", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("grid", "hook")),
                    Beat(24, show = setOf("fn"), light = setOf("hook", "fn"), pulse = "grid" to "hook", card = BoardCard("Validation handshake", "Event Grid sends a code; your webhook sends it back. Like confirming an email.")),
                ),
            ),
            // 25: "Okay. Now the part that confuses everyone. Event Grid, Event Hubs, Ser..."
            Scene(
                25, "Which one when",
                DiagramSpec(
                    "One small online shop, three services: Event Grid reacts, Event Hubs streams, Service Bus handles jobs.",
                    nodes = listOf(
                        DNode("shop", "Online shop", 0.5f, 0.14f, w = 0.4f, h = 0.18f),
                        DNode("eg", "Event Grid", 0.18f, 0.62f, w = 0.32f, h = 0.28f, sub = "photo uploaded"),
                        DNode("eh", "Event Hubs", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "every click"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "an order"),
                    ),
                    edges = listOf(
                        DEdge("shop", "eg"),
                        DEdge("shop", "eh"),
                        DEdge("shop", "sb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("eg", "eh", "sb"), card = BoardCard("Three different jobs", "Grid: something happened. Hubs: a huge stream. Bus: jobs handled with care.")),
                    Beat(28, light = setOf("eg"), card = BoardCard("Push or pull", "Event Grid pushes to your handler. With Hubs and Service Bus, your code pulls.")),
                    Beat(30, show = setOf("shop"), light = setOf("shop", "eg"), pulse = "shop" to "eg", card = BoardCard("A small online shop", "Photo uploaded: Grid. Every click: Hubs. An order to charge and ship: Bus.")),
                    Beat(32, light = setOf("eh"), card = BoardCard("A flood of clicks?", "Not Event Grid. High-volume telemetry is Event Hubs.")),
                ),
            ),
            // 33: "What else do people get wrong?"
            Scene(
                33, "Mistakes to avoid",
                DiagramSpec(
                    "Three ways to get Event Grid wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("hs", "Yes without the code", 0.26f, 0.24f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE),
                        DNode("once", "Expecting exactly once", 0.76f, 0.24f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE),
                        DNode("late", "Filtering in the handler", 0.5f, 0.74f, w = 0.6f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("hs", "once", "late"), card = BoardCard("Filter early", "A subject filter stops the delivery, so you never pay for it.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's Event Grid questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("react", "React when it happens", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Grid"),
                        DNode("files", "Only certain files", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "subject filter"),
                        DNode("keep", "Keep undelivered", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("dup", "Duplicates", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idempotent"),
                        DNode("hook", "A webhook", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "validation handshake"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("react", "files", "keep", "dup"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("hook"), light = setOf("hook"), card = BoardCard("A webhook question?", "Send the code back, or use a Function with an Event Grid trigger.")),
                    Beat(40, card = BoardCard("Today", "Blob upload to a Function, read the JSON, then a .png filter skips a .txt.")),
                ),
            ),
        ),
    )

    private fun eventHubs() = PodcastBoard(
        "az-eventhubs",
        listOf(
            // 0: "Hi, and welcome back. Today is about huge streams of data. Here's a pu..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Thousands of phones send step counts every few seconds; a plain database struggles with that flood.",
                    nodes = listOf(
                        DNode("phones", "Phones", 0.16f, 0.4f, w = 0.24f, h = 0.4f, kind = NodeKind.USER, tone = 1, sub = "thousands"),
                        DNode("db", "Database?", 0.78f, 0.4f, w = 0.32f, h = 0.32f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phones", "db", "steps"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phones", "db"), pulse = "phones" to "db"),
                    Beat(1, light = setOf("db"), card = BoardCard("Thousands of writes a second", "A plain database struggles. You want something built for a flood.")),
                ),
            ),
            // 2: "It does. You want something built to swallow a flood, and hold it whil..."
            Scene(
                2, "A tape that keeps playing",
                DiagramSpec(
                    "Many senders write into the event hub; it keeps the events, and many readers can play them back.",
                    nodes = listOf(
                        DNode("senders", "Senders", 0.13f, 0.45f, w = 0.22f, h = 0.4f, kind = NodeKind.USER, tone = 1),
                        DNode("hub", "Event hub", 0.5f, 0.45f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "kept for days"),
                        DNode("r1", "Reader 1", 0.86f, 0.25f, h = 0.18f, tone = 1),
                        DNode("r2", "Reader 2", 0.86f, 0.68f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("senders", "hub"),
                        DEdge("hub", "r1"),
                        DEdge("hub", "r2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("senders", "hub")),
                    Beat(4, light = setOf("hub"), pulse = "senders" to "hub", card = BoardCard("Event Hubs", "A tape recorder that never stops: millions of events in, kept for days.")),
                    Beat(6, show = setOf("r1", "r2"), light = setOf("r1", "r2"), pulse = "hub" to "r2", card = BoardCard("Reading doesn't remove", "Many readers, same stream, and they can rewind. Not like a queue.")),
                ),
            ),
            // 7: "How is it organised inside?"
            Scene(
                7, "Lanes on a motorway",
                DiagramSpec(
                    "A hub is split into partitions; order holds only inside one, and the same partition key always lands in the same one.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.5f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("p0", "Partition 0", 0.74f, 0.18f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p1", "Partition 1", 0.74f, 0.5f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p2", "Partition 2", 0.74f, 0.82f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("hub", "p0"),
                        DEdge("hub", "p1", "user id"),
                        DEdge("hub", "p2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("hub")),
                    Beat(8, show = setOf("p0", "p1", "p2"), light = setOf("p0", "p1", "p2"), card = BoardCard("Partition", "One lane: in order inside it. More lanes, more work in parallel.")),
                    Beat(12, light = setOf("p1"), pulse = "hub" to "p1", card = BoardCard("Partition key", "Same key, same lane. One phone's steps stay in order.")),
                ),
            ),
            // 13: "Okay. Now, who reads all this?"
            Scene(
                13, "Each reader its own view",
                DiagramSpec(
                    "A leaderboard and a daily report each read the whole stream through their own consumer group.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.45f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("cg1", "Leaderboard", 0.75f, 0.2f, w = 0.44f, h = 0.24f, tone = 1, sub = "consumer group 1"),
                        DNode("cg2", "Daily report", 0.75f, 0.7f, w = 0.44f, h = 0.24f, tone = 1, sub = "consumer group 2"),
                    ),
                    edges = listOf(
                        DEdge("hub", "cg1"),
                        DEdge("hub", "cg2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("hub")),
                    Beat(14, show = setOf("cg1", "cg2"), light = setOf("cg1", "cg2"), pulse = "hub" to "cg2", card = BoardCard("Consumer group", "Each app's own view of the whole stream, with its own bookmark.")),
                    Beat(16, card = BoardCard("Don't share one", "Two apps on one consumer group steal each other's partitions.")),
                ),
            ),
            // 17: "So partitions are lanes, and consumer groups are viewers. What's the b..."
            Scene(
                17, "Bookmarks",
                DiagramSpec(
                    "A reader saves checkpoints in blob storage; EventProcessorClient or a Function trigger does it for you.",
                    nodes = listOf(
                        DNode("reader", "Reader", 0.2f, 0.3f, w = 0.32f, h = 0.2f),
                        DNode("blob", "Blob storage", 0.78f, 0.3f, w = 0.36f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "checkpoints"),
                        DNode("epc", "EventProcessorClient", 0.5f, 0.8f, w = 0.64f, h = 0.18f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("reader", "blob", "checkpoint"),
                        DEdge("epc", "blob", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("reader", "blob")),
                    Beat(18, light = setOf("blob"), pulse = "reader" to "blob", card = BoardCard("Checkpoint", "A saved position in blob storage. Save after a batch, not every event.")),
                    Beat(20, show = setOf("epc"), light = setOf("epc"), card = BoardCard("Or let it be done for you", "EventProcessorClient, or a Function with the Event Hub trigger.")),
                ),
            ),
            // 21: "And how do I make it take more?"
            Scene(
                21, "Two dials",
                DiagramSpec(
                    "Throughput units buy capacity and auto-inflate adds them; more partitions mean more readers at once.",
                    nodes = listOf(
                        DNode("tu", "Throughput unit", 0.26f, 0.3f, w = 0.46f, h = 0.26f, sub = "1 MB/s in, 2 out"),
                        DNode("parts", "Partitions", 0.76f, 0.3f, w = 0.44f, h = 0.26f, tone = 1, sub = "more readers"),
                        DNode("auto", "Auto-inflate: grows, never shrinks", 0.5f, 0.78f, w = 0.72f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("tu", "parts")),
                    Beat(22, show = setOf("auto"), light = setOf("tu", "auto"), card = BoardCard("Throughput unit", "One slice of capacity: 1 MB a second in, 2 out.")),
                    Beat(24, light = setOf("parts"), card = BoardCard("Retention", "Kept 1 to 7 days on Standard, up to 90 on Premium.")),
                ),
            ),
            // 25: "What if I want every event saved for good?"
            Scene(
                25, "Capture",
                DiagramSpec(
                    "Capture writes events in batches to Blob Storage or a data lake as Avro files, with no code.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.4f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("store", "Blob or data lake", 0.76f, 0.4f, w = 0.4f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "Avro files"),
                    ),
                    edges = listOf(
                        DEdge("hub", "store", "Capture"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("hub")),
                    Beat(26, show = setOf("store"), light = setOf("store"), pulse = "hub" to "store", card = BoardCard("Capture", "Archive every event to storage as Avro files, with no code.")),
                ),
            ),
            // 27: "Okay, the confusing bit. Event Hubs, Event Grid, Service Bus. Which on..."
            Scene(
                27, "Which one when",
                DiagramSpec(
                    "One thermostat company, three services: Event Hubs streams, Event Grid reacts, Service Bus handles orders.",
                    nodes = listOf(
                        DNode("co", "Thermostat company", 0.5f, 0.14f, w = 0.52f, h = 0.18f),
                        DNode("eh", "Event Hubs", 0.18f, 0.62f, w = 0.32f, h = 0.28f, sub = "a reading a minute"),
                        DNode("eg", "Event Grid", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a file uploaded"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "an order"),
                    ),
                    edges = listOf(
                        DEdge("co", "eh"),
                        DEdge("co", "eg"),
                        DEdge("co", "sb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("eh", "eg", "sb"), card = BoardCard("Three different jobs", "Hubs: a stream. Grid: one this-happened note. Bus: a job handled carefully.")),
                    Beat(30, show = setOf("co"), light = setOf("co", "eh"), pulse = "co" to "eh", card = BoardCard("A thermostat company", "Readings every minute: Hubs. Firmware upload: Grid. A new order: Bus.")),
                    Beat(32, light = setOf("sb"), card = BoardCard("Each message handled with care?", "One-by-one work with dead-lettering is Service Bus, not Event Hubs.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Event Hubs questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("two", "Two apps, every event", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a consumer group each"),
                        DNode("order", "One device in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "partition key"),
                        DNode("arch", "Archive, no code", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Capture"),
                        DNode("mill", "Millions a second", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Hubs"),
                        DNode("replay", "Replay yesterday", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "retention and a new consumer group"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("two", "order", "arch", "mill"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("replay"), light = setOf("replay"), card = BoardCard("Replay yesterday?", "Retention, plus a new consumer group from an earlier position.")),
                    Beat(38, card = BoardCard("Today", "Basic namespace, 2 partitions, send 10 events, read them, check Metrics.")),
                ),
            ),
        ),
    )

    private fun serviceBus() = PodcastBoard(
        "az-servicebus",
        listOf(
            // 0: "Hi, and welcome back. Today is about messages that must be handled. Se..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A pizza app calls the email service directly, so when that service is down the receipt waits or is lost.",
                    nodes = listOf(
                        DNode("app", "Pizza app", 0.2f, 0.35f, w = 0.32f, h = 0.2f),
                        DNode("email", "Email service", 0.78f, 0.35f, w = 0.38f, h = 0.26f, tone = 2, sub = "down right now"),
                    ),
                    edges = listOf(
                        DEdge("app", "email", "receipt", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "email"), pulse = "app" to "email"),
                    Beat(1, light = setOf("email"), card = BoardCard("Wait, or lose it?", "Calling the email service directly fails when it is down.")),
                ),
            ),
            // 2: "Right. So instead, the app drops a message in a queue, and carries on...."
            Scene(
                2, "A queue in between",
                DiagramSpec(
                    "The app drops a message in a queue and carries on; a worker takes it off when it is ready.",
                    nodes = listOf(
                        DNode("app", "Pizza app", 0.15f, 0.4f, h = 0.2f),
                        DNode("q", "Queue", 0.5f, 0.4f, w = 0.32f, h = 0.36f, kind = NodeKind.CYLINDER, sub = "jobs wait here"),
                        DNode("worker", "Email worker", 0.85f, 0.4f, w = 0.28f, h = 0.22f),
                    ),
                    edges = listOf(
                        DEdge("app", "q", "message"),
                        DEdge("q", "worker"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("q"), light = setOf("q"), pulse = "app" to "q", card = BoardCard("A message", "A job to be done, like send this email. It waits until a worker is free.")),
                    Beat(4, light = setOf("worker"), pulse = "q" to "worker", card = BoardCard("A queue", "Tickets on a kitchen rail. Many cooks, but each ticket goes to just one.")),
                ),
            ),
            // 5: "And Service Bus is the rail?"
            Scene(
                5, "Queues and topics",
                DiagramSpec(
                    "A queue gives each message to one receiver; a topic gives every subscription its own copy.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.22f, w = 0.32f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "one receiver"),
                        DNode("tier", "Topics: Standard or above", 0.74f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE),
                        DNode("t", "Topic", 0.2f, 0.74f, w = 0.32f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "a copy each"),
                        DNode("s1", "Certificates", 0.74f, 0.6f, w = 0.4f, h = 0.2f, tone = 1, sub = "subscription"),
                        DNode("s2", "Analytics", 0.74f, 0.86f, w = 0.4f, h = 0.2f, tone = 1, sub = "subscription"),
                    ),
                    edges = listOf(
                        DEdge("t", "s1"),
                        DEdge("t", "s2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("q")),
                    Beat(6, light = setOf("q"), card = BoardCard("Service Bus", "Queues and topics in a namespace, with a Basic, Standard or Premium tier.")),
                    Beat(8, show = setOf("t", "s1", "s2", "tier"), light = setOf("t"), pulse = "t" to "s1", card = BoardCard("Topic", "Publish once, each subscription gets a copy. Quiz passed: PDF and analytics.")),
                ),
            ),
            // 9: "Can a subscription choose which messages it wants?"
            Scene(
                9, "Rules on subscriptions",
                DiagramSpec(
                    "Each subscription filters with rules; delete the default rule or it still lets every message in.",
                    nodes = listOf(
                        DNode("t", "Topic", 0.18f, 0.48f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("sql", "SQL filter", 0.74f, 0.16f, w = 0.46f, h = 0.24f, tone = 1, sub = "passed = true"),
                        DNode("corr", "Correlation filter", 0.74f, 0.5f, w = 0.46f, h = 0.24f, tone = 1, sub = "exact match, cheaper"),
                        DNode("def", "Default rule: matches all", 0.74f, 0.84f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("t", "sql"),
                        DEdge("t", "corr"),
                        DEdge("t", "def", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("t")),
                    Beat(10, show = setOf("sql", "corr", "def"), light = setOf("def"), pulse = "t" to "sql", card = BoardCard("Delete the default rule", "Leave it beside yours and every message still matches.")),
                ),
            ),
            // 11: "Sneaky. Now, how does a worker actually take a message?"
            Scene(
                11, "How a worker takes it",
                DiagramSpec(
                    "PeekLock locks a message instead of removing it; ReceiveAndDelete removes it at once and loses it on a crash.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.32f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.32f, w = 0.32f, h = 0.2f),
                        DNode("pl", "PeekLock", 0.26f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, sub = "locked, not removed"),
                        DNode("rad", "ReceiveAndDelete", 0.75f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "gone if you crash"),
                    ),
                    edges = listOf(
                        DEdge("q", "w", "take"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("q", "w")),
                    Beat(12, show = setOf("pl"), light = setOf("pl"), pulse = "q" to "w", card = BoardCard("PeekLock", "Locked, not removed. If the worker crashes, the message comes back.")),
                    Beat(14, show = setOf("rad"), light = setOf("rad"), card = BoardCard("ReceiveAndDelete", "Faster, but a crash loses the message. Use PeekLock for work that can fail.")),
                ),
            ),
            // 15: "When the cook finishes, what then?"
            Scene(
                15, "Settling a message",
                DiagramSpec(
                    "A locked message is settled in one of four ways: complete, abandon, dead-letter or defer.",
                    nodes = listOf(
                        DNode("c", "Complete", 0.26f, 0.25f, w = 0.44f, h = 0.26f, sub = "done, removed"),
                        DNode("a", "Abandon", 0.75f, 0.25f, w = 0.44f, h = 0.26f, tone = 1, sub = "comes back, count +1"),
                        DNode("d", "Dead-letter", 0.26f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "moved aside"),
                        DNode("f", "Defer", 0.75f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "skip, fetch later"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("c", "a", "d", "f"), light = setOf("c"), card = BoardCard("Four ways to settle", "Complete, abandon, dead-letter or defer.")),
                ),
            ),
            // 19: "What's dead-letter, in plain words?"
            Scene(
                19, "The dead-letter queue",
                DiagramSpec(
                    "A message that can never work is dead-lettered; one that fails too often lands there after ten tries.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.28f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.28f, w = 0.32f, h = 0.2f),
                        DNode("dlq", "Dead-letter queue", 0.5f, 0.78f, w = 0.46f, h = 0.36f, kind = NodeKind.CYLINDER, sub = "a side pile"),
                    ),
                    edges = listOf(
                        DEdge("q", "w"),
                        DEdge("w", "dlq", "can never work"),
                        DEdge("q", "dlq", "after 10 tries", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("q", "w")),
                    Beat(20, show = setOf("dlq"), light = setOf("dlq"), pulse = "w" to "dlq", card = BoardCard("Broken for good?", "Dead-letter it. Complete hides the failure; abandon brings it back.")),
                    Beat(22, pulse = "q" to "dlq", card = BoardCard("Max delivery count", "Ten tries by default, then dead-lettered. Read that pile!")),
                    Beat(24, light = setOf("w"), card = BoardCard("Lock duration", "One minute by default, up to 5 minutes. Renew it for long work.")),
                ),
            ),
            // 25: "And if things must happen in a strict order?"
            Scene(
                25, "Order and extras",
                DiagramSpec(
                    "Sessions keep messages with the same session id in order for one receiver; other features come built in.",
                    nodes = listOf(
                        DNode("q", "Session queue", 0.2f, 0.32f, w = 0.34f, h = 0.36f, kind = NodeKind.CYLINDER),
                        DNode("w", "One receiver", 0.78f, 0.32f, w = 0.36f, h = 0.24f, sub = "strictly in order"),
                        DNode("dup", "Duplicate detection", 0.17f, 0.82f, w = 0.32f, h = 0.22f, kind = NodeKind.NOTE, tone = 1),
                        DNode("sched", "Scheduled messages", 0.5f, 0.82f, w = 0.32f, h = 0.22f, kind = NodeKind.NOTE, tone = 1),
                        DNode("tx", "Transactions", 0.83f, 0.82f, w = 0.32f, h = 0.22f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("q", "w", "session id"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("q", "w")),
                    Beat(26, light = setOf("w"), pulse = "q" to "w", card = BoardCard("Session", "Same session id, one receiver, strict order. A shop: use the order id.")),
                    Beat(28, show = setOf("dup", "sched", "tx"), card = BoardCard("Built in", "Drop repeats, deliver later, or do two things all or nothing.")),
                ),
            ),
            // 29: "Okay. So what's Queue Storage for?"
            Scene(
                29, "Queue Storage",
                DiagramSpec(
                    "Storage queues grow far past Service Bus's 80 GB, with a visibility timeout and a pop receipt instead of PeekLock.",
                    nodes = listOf(
                        DNode("sbq", "Service Bus queue", 0.25f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, tone = 1, sub = "up to 80 GB"),
                        DNode("stq", "Storage queue", 0.75f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, sub = "far past 80 GB"),
                        DNode("vt", "Visibility timeout", 0.26f, 0.82f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "hidden while you work"),
                        DNode("pr", "Pop receipt", 0.76f, 0.82f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "proves you hold it"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("sbq", "stq")),
                    Beat(30, light = setOf("stq"), card = BoardCard("Photo thumbnails", "Trivial work, huge volume, no fancy features: a Storage queue.")),
                    Beat(32, show = setOf("vt", "pr"), light = setOf("vt", "pr"), card = BoardCard("No PeekLock here", "Hidden for the visibility timeout; delete it with the pop receipt.")),
                ),
            ),
            // 33: "And how is all this different from Event Grid and Event Hubs?"
            Scene(
                33, "Which one when",
                DiagramSpec(
                    "Event Grid says something happened, Event Hubs takes a stream, Service Bus carries jobs that must be handled.",
                    nodes = listOf(
                        DNode("app", "Pizza app", 0.5f, 0.14f, w = 0.4f, h = 0.18f),
                        DNode("eg", "Event Grid", 0.18f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "it happened"),
                        DNode("eh", "Event Hubs", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a huge stream"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, sub = "a job to handle"),
                    ),
                    edges = listOf(
                        DEdge("app", "sb", "receipt email"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("app", "eg", "eh", "sb"), light = setOf("sb"), pulse = "app" to "sb", card = BoardCard("The pizza app's receipt", "A job that must be handled, with a dead-letter queue: Service Bus.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's messaging questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("never", "Can never be processed", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("order", "One order, in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sessions"),
                        DNode("big", "Over 80 GB or audit log", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Storage queue"),
                        DNode("many", "Several subscribers", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a topic"),
                        DNode("sb", "Duplicates, FIFO, transactions", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "Service Bus"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("never", "order", "big", "many"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("sb"), light = setOf("sb"), card = BoardCard("Service Bus over Storage?", "Duplicate detection, FIFO, transactions or publish and subscribe.")),
                    Beat(40, card = BoardCard("Today", "A topic, two subscriptions, a SQL rule; send two messages, then peek them.")),
                ),
            ),
        ),
    )

    private fun appInsights() = PodcastBoard(
        "az-appinsights",
        listOf(
            // 0: "Hi, and welcome back. Today is about seeing what your app is doing onc..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A customer says checkout is slow, but in production you cannot see what the app is doing.",
                    nodes = listOf(
                        DNode("user", "Customer", 0.14f, 0.4f, w = 0.2f, h = 0.24f, kind = NodeKind.USER),
                        DNode("app", "Shop app", 0.68f, 0.4f, w = 0.34f, h = 0.22f, sub = "live in Azure"),
                        DNode("q", "Why is it slow?", 0.68f, 0.82f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("user", "app", "checkout"),
                        DEdge("q", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("user", "app"), pulse = "user" to "app"),
                    Beat(1, show = setOf("q"), light = setOf("q"), card = BoardCard("Print lines?", "You can't see the server's screen, and by the time you look, it's over.")),
                ),
            ),
            // 2: "Exactly. You need something watching all the time and writing everythi..."
            Scene(
                2, "A flight recorder",
                DiagramSpec(
                    "Application Insights records what the app does; each recorded item is telemetry.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.2f, w = 0.32f, h = 0.2f),
                        DNode("ai", "Application Insights", 0.7f, 0.2f, w = 0.48f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("req", "Requests", 0.26f, 0.6f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "calls coming in"),
                        DNode("dep", "Dependencies", 0.74f, 0.6f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "calls going out"),
                        DNode("exc", "Exceptions", 0.26f, 0.86f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "crashes"),
                        DNode("trc", "Traces", 0.74f, 0.86f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "log lines"),
                    ),
                    edges = listOf(
                        DEdge("app", "ai", "telemetry"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("app", "ai")),
                    Beat(4, light = setOf("ai"), pulse = "app" to "ai", card = BoardCard("Application Insights", "A flight recorder for your app. Each recorded item is telemetry.")),
                    Beat(6, show = setOf("req", "dep", "exc", "trc"), light = setOf("req", "dep", "exc", "trc"), card = BoardCard("What gets recorded", "Requests in, dependencies out, exceptions, traces, and page views.")),
                ),
            ),
            // 9: "Do I have to write code to get all that?"
            Scene(
                9, "Two ways in",
                DiagramSpec(
                    "Auto-instrumentation needs no code; the SDK adds custom events and finds the resource by its connection string.",
                    nodes = listOf(
                        DNode("auto", "Auto-instrumentation", 0.26f, 0.2f, w = 0.48f, h = 0.2f, tone = 1, sub = "no code change"),
                        DNode("sdk", "SDK", 0.26f, 0.58f, w = 0.48f, h = 0.2f, tone = 1, sub = "adds custom events"),
                        DNode("ai", "Application Insights", 0.78f, 0.4f, w = 0.4f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("conn", "Connection string", 0.3f, 0.88f, w = 0.5f, kind = NodeKind.NOTE, sub = "where to send it"),
                    ),
                    edges = listOf(
                        DEdge("auto", "ai"),
                        DEdge("sdk", "ai"),
                        DEdge("conn", "sdk", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("ai")),
                    Beat(10, show = setOf("auto"), light = setOf("auto"), pulse = "auto" to "ai", card = BoardCard("Auto-instrumentation", "Switch it on in the portal: requests, dependencies, exceptions.")),
                    Beat(12, show = setOf("sdk"), light = setOf("sdk"), pulse = "sdk" to "ai", card = BoardCard("The SDK", "A small code library, for your own custom events.")),
                    Beat(14, show = setOf("conn"), light = setOf("conn"), card = BoardCard("Connection string", "The parcel's address label. Use it, not the old instrumentation key.")),
                ),
            ),
            // 15: "Tell me more about custom events."
            Scene(
                15, "Custom events",
                DiagramSpec(
                    "A custom event marks a business moment; its properties are text for filtering, its measurements numbers for charts.",
                    nodes = listOf(
                        DNode("app", "Language app", 0.22f, 0.22f, w = 0.36f, h = 0.2f),
                        DNode("ev", "LessonFinished", 0.72f, 0.22f, w = 0.46f, kind = NodeKind.PILL, sub = "a custom event"),
                        DNode("prop", "topic", 0.26f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "property: text"),
                        DNode("meas", "minutes", 0.74f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "measurement: number"),
                    ),
                    edges = listOf(
                        DEdge("app", "ev", "TrackEvent"),
                        DEdge("ev", "prop"),
                        DEdge("ev", "meas"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("app")),
                    Beat(16, show = setOf("ev", "prop", "meas"), light = setOf("ev", "prop"), pulse = "app" to "ev", card = BoardCard("Lessons per hour, by topic", "A language app sends a custom event, with the topic as a property.")),
                    Beat(18, light = setOf("meas"), card = BoardCard("C#", "telemetry.TrackEvent(\"LessonFinished\", props, measurements);", code = true)),
                ),
            ),
            // 19: "Okay. So where does all this data end up?"
            Scene(
                19, "Asking questions with KQL",
                DiagramSpec(
                    "Telemetry lands in a Log Analytics workspace, one table per kind, and KQL asks it questions step by step.",
                    nodes = listOf(
                        DNode("ai", "Application Insights", 0.26f, 0.2f, w = 0.48f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("la", "Log Analytics workspace", 0.76f, 0.2f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "the filing cabinet"),
                        DNode("kql", "KQL query", 0.5f, 0.62f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("steps", "where, summarize, render", 0.5f, 0.88f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("ai", "la"),
                        DEdge("kql", "la", "asks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("ai")),
                    Beat(20, show = setOf("la"), light = setOf("la"), pulse = "ai" to "la", card = BoardCard("Log Analytics workspace", "A filing cabinet: one table for requests, one for exceptions, and so on.")),
                    Beat(22, show = setOf("kql", "steps"), light = setOf("kql", "steps"), pulse = "kql" to "la", card = BoardCard("KQL", "Start with a table. Where filters, summarize groups, render draws a chart.")),
                    Beat(24, card = BoardCard("KQL", "requests | where success == false | summarize count() by resultCode", code = true)),
                ),
            ),
            // 26: "Now, back to the slow checkout. One click on checkout might make four ..."
            Scene(
                26, "One request, one id",
                DiagramSpec(
                    "Every item one request causes shares its operation id, which the end-to-end view follows.",
                    nodes = listOf(
                        DNode("req", "Checkout request", 0.5f, 0.14f, w = 0.5f, h = 0.18f),
                        DNode("d1", "DB call", 0.14f, 0.5f, w = 0.24f, h = 0.14f, tone = 1),
                        DNode("d2", "DB call", 0.38f, 0.5f, w = 0.22f, h = 0.14f, tone = 1),
                        DNode("d3", "DB call", 0.62f, 0.5f, w = 0.22f, h = 0.14f, tone = 1),
                        DNode("d4", "DB call", 0.86f, 0.5f, w = 0.24f, h = 0.14f, tone = 1),
                        DNode("ex", "Exception", 0.24f, 0.84f, w = 0.4f, tone = 2),
                        DNode("op", "Operation id", 0.74f, 0.84f, w = 0.42f, kind = NodeKind.PILL, sub = "the tracking number"),
                    ),
                    edges = listOf(
                        DEdge("req", "d1"),
                        DEdge("req", "d2"),
                        DEdge("req", "d3"),
                        DEdge("req", "d4"),
                        DEdge("req", "ex"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("req", "d1", "d2", "d3", "d4", "ex"), light = setOf("req")),
                    Beat(27, card = BoardCard("The timestamp?", "Close, but no. Times can clash; something more exact ties them.")),
                    Beat(28, show = setOf("op"), light = setOf("op"), pulse = "req" to "ex", card = BoardCard("Operation id", "One id on every item of a request. The end-to-end view follows it.")),
                ),
            ),
            // 29: "Doesn't recording everything cost a lot?"
            Scene(
                29, "Sampling",
                DiagramSpec(
                    "Adaptive sampling keeps a share of related items under load and scales the counts back up.",
                    nodes = listOf(
                        DNode("all", "All telemetry", 0.2f, 0.3f, w = 0.34f, h = 0.22f, tone = 1),
                        DNode("samp", "Adaptive sampling", 0.72f, 0.3f, w = 0.46f, h = 0.22f, sub = "keeps related items"),
                        DNode("counts", "Counts stay roughly right", 0.5f, 0.8f, w = 0.64f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("all", "samp", "under load"),
                        DEdge("samp", "counts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("all")),
                    Beat(30, show = setOf("samp"), light = setOf("samp"), pulse = "all" to "samp", card = BoardCard("Adaptive sampling", "The default in the dot net SDK. Keeps a share, scales counts back up.")),
                    Beat(32, show = setOf("counts"), light = setOf("counts"), card = BoardCard("A busy photo-sharing app", "Turns sampling on: a smaller bill, and charts still about right.")),
                ),
            ),
            // 33: "And if I want to watch while I deploy?"
            Scene(
                33, "Live Metrics",
                DiagramSpec(
                    "Live Metrics shows requests, failures and CPU with about one second of delay while you deploy.",
                    nodes = listOf(
                        DNode("dep", "New version", 0.2f, 0.3f, w = 0.32f, h = 0.2f, tone = 1),
                        DNode("app", "Food delivery app", 0.72f, 0.3f, w = 0.44f, h = 0.2f),
                        DNode("live", "Live Metrics", 0.5f, 0.8f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "about one second behind"),
                    ),
                    edges = listOf(
                        DEdge("dep", "app", "deploy"),
                        DEdge("app", "live"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("dep", "app"), pulse = "dep" to "app"),
                    Beat(34, show = setOf("live"), light = setOf("live"), pulse = "app" to "live", card = BoardCard("Watch the deploy live", "A food delivery app ships on Friday; watch failures as traffic moves.")),
                ),
            ),
            // 35: "Anything people often get wrong?"
            Scene(
                35, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Application Insights wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("pii", "Personal data in properties", 0.26f, 0.22f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE),
                        DNode("off", "Sampling off under load", 0.76f, 0.22f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE),
                        DNode("ikey", "Instrumentation key alone", 0.26f, 0.72f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE),
                        DNode("table", "Looking in the wrong table", 0.76f, 0.72f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("pii", "off", "ikey", "table"), card = BoardCard("Errors missing?", "Errors written as plain log lines land in traces, not exceptions.")),
                ),
            ),
            // 37: "So what should I remember for the exam?"
            Scene(
                37, "For the exam",
                DiagramSpec(
                    "The exam's Application Insights questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cost", "Cut cost, keep counts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sampling"),
                        DNode("trace", "Follow a slow request", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "operation id"),
                        DNode("live", "Watch a deploy live", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Live Metrics"),
                        DNode("count", "Count business actions", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackEvent"),
                        DNode("kql", "Ask the data a question", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "KQL in Log Analytics"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("cost", "trace", "live"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, show = setOf("count", "kql"), light = setOf("count", "kql"), card = BoardCard("Counting lessons per topic?", "A custom event with TrackEvent, the topic as a property.")),
                    Beat(42, card = BoardCard("Today", "A resource, a sample app, a failing request, then a KQL count by result code.")),
                ),
            ),
        ),
    )

    private fun availability() = PodcastBoard(
        "az-availability",
        listOf(
            // 0: "Hi, and welcome back. Today is about being told when your app breaks. ..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "The app goes down at night, and nobody finds out until a customer complains.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.7f, 0.4f, w = 0.34f, h = 0.22f, tone = 2, sub = "down at 3 a.m."),
                        DNode("user", "Customer", 0.14f, 0.4f, w = 0.2f, h = 0.24f, kind = NodeKind.USER),
                        DNode("mail", "An angry email", 0.5f, 0.84f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("user", "app", "opens it"),
                        DEdge("user", "mail", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "user"), pulse = "user" to "app"),
                    Beat(1, show = setOf("mail"), light = setOf("mail"), pulse = "user" to "mail"),
                    Beat(2, card = BoardCard("Somebody must be told", "Availability tests, alerts and autoscale.")),
                ),
            ),
            // 3: "Start with availability tests. What are they, in plain words?"
            Scene(
                3, "A robot customer",
                DiagramSpec(
                    "A standard availability test visits a public site from up to sixteen regions and checks the answer.",
                    nodes = listOf(
                        DNode("test", "Standard test", 0.24f, 0.24f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "up to 16 regions"),
                        DNode("site", "Salon booking site", 0.76f, 0.24f, w = 0.42f, h = 0.22f),
                        DNode("checks", "Status, content, certificate", 0.5f, 0.76f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("test", "site", "every 5 min"),
                        DEdge("site", "checks", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("test")),
                    Beat(4, show = setOf("site", "checks"), light = setOf("test", "checks"), pulse = "test" to "site", card = BoardCard("Availability test", "A robot customer that visits your site on a schedule.")),
                    Beat(6, light = setOf("site"), card = BoardCard("A public salon booking site", "Standard test: every five minutes, from five regions.")),
                ),
            ),
            // 7: "What if my site isn't public? Like an internal tool for staff."
            Scene(
                7, "A private endpoint",
                DiagramSpec(
                    "A standard test cannot reach a private network; a timer function inside it reports with TrackAvailability.",
                    nodes = listOf(
                        DNode("test", "Standard test", 0.24f, 0.14f, w = 0.42f, kind = NodeKind.PILL, tone = 2),
                        DNode("tool", "Internal tool", 0.24f, 0.56f, w = 0.4f, h = 0.2f),
                        DNode("fn", "Timer function", 0.24f, 0.86f, w = 0.4f, tone = 1, sub = "inside the network"),
                        DNode("ai", "Application Insights", 0.76f, 0.66f, w = 0.42f, h = 0.28f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("test", "tool", "can't reach", dashed = true),
                        DEdge("fn", "tool"),
                        DEdge("fn", "ai", "TrackAvailability"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("tool")),
                    Beat(8, show = setOf("test", "fn", "ai"), light = setOf("fn"), pulse = "fn" to "ai", card = BoardCard("Not public?", "Run a probe inside the network; report with TrackAvailability.")),
                    Beat(10, card = BoardCard("URL ping test", "Retired. Move those to standard tests.")),
                ),
            ),
            // 11: "Okay. Now alerts. What's an alert rule?"
            Scene(
                11, "The smoke detector",
                DiagramSpec(
                    "A signal crosses a condition, the alert rule fires, and the action group tells people.",
                    nodes = listOf(
                        DNode("sig", "Signal", 0.18f, 0.3f, w = 0.32f, h = 0.22f, tone = 1, sub = "metric, log, test"),
                        DNode("rule", "Alert rule", 0.7f, 0.3f, w = 0.4f, h = 0.22f, sub = "the smoke detector"),
                        DNode("ag", "Action group", 0.7f, 0.78f, w = 0.4f, h = 0.2f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("sig", "rule"),
                        DEdge("rule", "ag", "fires"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("rule")),
                    Beat(12, show = setOf("sig", "ag"), light = setOf("sig"), pulse = "sig" to "rule", card = BoardCard("Alert rule", "A smoke detector: watches a signal and goes off on a condition.")),
                ),
            ),
            // 13: "Are there different kinds?"
            Scene(
                13, "Kinds of alert",
                DiagramSpec(
                    "Metric, log search, activity log, smart detection and availability alerts each watch a different signal.",
                    nodes = listOf(
                        DNode("metric", "Metric alert", 0.26f, 0.13f, w = 0.46f, h = 0.2f, sub = "a number vs a line"),
                        DNode("log", "Log search alert", 0.76f, 0.13f, w = 0.44f, h = 0.2f, sub = "KQL on a schedule"),
                        DNode("act", "Activity log alert", 0.26f, 0.47f, w = 0.46f, h = 0.2f, tone = 1, sub = "management actions"),
                        DNode("smart", "Smart detection", 0.76f, 0.47f, w = 0.44f, h = 0.2f, tone = 1, sub = "unusual patterns"),
                        DNode("avail", "Availability alert", 0.26f, 0.83f, w = 0.46f, h = 0.2f, tone = 1, sub = "3 of 5 regions fail"),
                        DNode("dyn", "Dynamic threshold", 0.76f, 0.83f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "learns normal"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("metric", "log"), light = setOf("metric", "log"), card = BoardCard("Metric or log search?", "A number crossing a line, or a KQL query run on a schedule.")),
                    Beat(18, show = setOf("act", "smart", "avail"), light = setOf("act", "smart", "avail")),
                    Beat(20, show = setOf("dyn"), light = setOf("log", "dyn"), card = BoardCard("Dynamic threshold", "Learns what normal looks like, instead of a fixed number.")),
                ),
            ),
            // 21: "Who actually gets told when it goes off?"
            Scene(
                21, "Who gets told",
                DiagramSpec(
                    "Many alert rules point at one action group, a reusable list of who to tell and how.",
                    nodes = listOf(
                        DNode("r1", "Rule", 0.14f, 0.16f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r2", "Rule", 0.14f, 0.4f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r3", "Rule", 0.14f, 0.64f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("ag", "Action group", 0.5f, 0.4f, w = 0.36f, h = 0.24f, sub = "the phone tree"),
                        DNode("email", "Email", 0.86f, 0.16f, h = 0.14f, tone = 2),
                        DNode("push", "Push", 0.86f, 0.4f, h = 0.14f, tone = 2),
                        DNode("auto", "Function", 0.86f, 0.64f, h = 0.14f, tone = 2),
                        DNode("mute", "Maintenance: muted", 0.5f, 0.9f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("r1", "ag"),
                        DEdge("r2", "ag"),
                        DEdge("r3", "ag"),
                        DEdge("ag", "email"),
                        DEdge("ag", "push"),
                        DEdge("ag", "auto"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("r1")),
                    Beat(22, show = setOf("ag", "email", "push", "auto"), light = setOf("ag"), pulse = "ag" to "push", card = BoardCard("Action group", "A phone tree: who to tell and how. Email, text, push, automation.")),
                    Beat(24, show = setOf("r2", "r3"), light = setOf("r1", "r2", "r3"), pulse = "r2" to "ag", card = BoardCard("A food delivery team", "Ten rules, one action group. A new person? Update the group once.")),
                    Beat(26, show = setOf("mute"), light = setOf("mute"), card = BoardCard("Alert processing rules", "Mute notifications during a maintenance window.")),
                ),
            ),
            // 27: "Good. Now autoscale."
            Scene(
                27, "Autoscale",
                DiagramSpec(
                    "Autoscale in Azure Monitor adds and removes instances between a minimum and a maximum, following its rules.",
                    nodes = listOf(
                        DNode("cpu", "CPU over 70%", 0.22f, 0.2f, w = 0.4f, kind = NodeKind.PILL, sub = "for 10 minutes"),
                        DNode("as", "Autoscale", 0.74f, 0.2f, w = 0.4f, h = 0.2f, sub = "min, max, default"),
                        DNode("i1", "Instance", 0.18f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i2", "Instance", 0.5f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i3", "Instance", 0.82f, 0.62f, w = 0.28f, tone = 1),
                        DNode("other", "Schedule or queue rules", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("cpu", "as"),
                        DEdge("as", "i3", "add one"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("as", "i1", "i2"), light = setOf("as"), card = BoardCard("Autoscale", "Adds or removes copies of your app, called instances.")),
                    Beat(30, show = setOf("cpu", "i3", "other"), light = setOf("cpu", "i3"), pulse = "as" to "i3", card = BoardCard("Rules", "Out above 70%, in below 30%, a schedule, or a Service Bus queue.")),
                ),
            ),
            // 33: "Here's a trick one. Add one above seventy, remove one below seventy?"
            Scene(
                33, "Flapping",
                DiagramSpec(
                    "Thresholds that meet make the rules fight; a gap between them and a cooldown keep it steady.",
                    nodes = listOf(
                        DNode("out", "Out above 70%", 0.26f, 0.16f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("in", "In below 70%", 0.76f, 0.16f, w = 0.42f, h = 0.18f, tone = 2),
                        DNode("flap", "Add, remove, add...", 0.5f, 0.46f, w = 0.56f, kind = NodeKind.NOTE),
                        DNode("fix", "In below 30%", 0.26f, 0.8f, w = 0.44f, h = 0.2f, sub = "a gap"),
                        DNode("cool", "Cooldown", 0.76f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "pause, then check"),
                    ),
                    edges = listOf(
                        DEdge("out", "in", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("out", "in"), pulse = "out" to "in"),
                    Beat(34, show = setOf("flap", "fix", "cool"), light = setOf("fix", "cool"), card = BoardCard("It flaps", "Fix: out at 70, in at 30, and a cooldown after each action.")),
                    Beat(36, light = setOf("fix"), card = BoardCard("A ticket-selling site", "Busy on Friday, small again on Monday. Pair out with an in rule.")),
                ),
            ),
            // 37: "So what should I remember for the exam?"
            Scene(
                37, "For the exam",
                DiagramSpec(
                    "The exam's monitoring questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("pub", "Public endpoint", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "standard test"),
                        DNode("priv", "Private endpoint", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackAvailability"),
                        DNode("kql", "KQL on a schedule", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "log search alert"),
                        DNode("thr", "Metric over a line", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "metric alert"),
                        DNode("flap", "Instances flap", 0.26f, 0.83f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "gap and cooldown"),
                        DNode("who", "Who gets told", 0.76f, 0.83f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "an action group"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("pub", "priv", "kql", "thr"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, show = setOf("flap", "who"), light = setOf("flap", "who"), card = BoardCard("Never shrinks?", "Add a scale in rule to match the scale out rule.")),
                    Beat(42, card = BoardCard("Today", "A standard test, an action group, an alert below 100; stop the app, get an email.")),
                ),
            ),
        ),
    )

    private fun redis() = PodcastBoard(
        "az-redis",
        listOf(
            // 0: "Hi, and welcome back. Today is about speed. Here's a puzzle to start. ..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Every visitor makes the database build the same page again from scratch.",
                    nodes = listOf(
                        DNode("users", "Thousands of visitors", 0.14f, 0.4f, w = 0.2f, h = 0.24f, kind = NodeKind.USER),
                        DNode("app", "Your app", 0.5f, 0.4f, w = 0.3f, h = 0.2f),
                        DNode("db", "Database", 0.85f, 0.4f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("q", "Same page, every time", 0.5f, 0.84f, w = 0.56f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("users", "app"),
                        DEdge("app", "db", "build it"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("users", "app", "db", "q"), pulse = "app" to "db"),
                    Beat(1, light = setOf("db"), pulse = "app" to "db", card = BoardCard("A fresh pot for every cup", "The same slow work, over and over.")),
                ),
            ),
            // 2: "Exactly. The fix is to keep a pot ready. That's called a cache, and to..."
            Scene(
                2, "A notepad in memory",
                DiagramSpec(
                    "Redis is an in-memory key-value store next to the app, with reads under a millisecond.",
                    nodes = listOf(
                        DNode("app", "News app", 0.2f, 0.3f, w = 0.32f, h = 0.2f),
                        DNode("redis", "Azure Cache for Redis", 0.74f, 0.3f, w = 0.44f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "in memory"),
                        DNode("kv", "top-stories: the page", 0.5f, 0.8f, w = 0.62f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "a key and its value"),
                    ),
                    edges = listOf(
                        DEdge("app", "redis", "under 1 ms"),
                        DEdge("redis", "kv", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("app", "redis")),
                    Beat(4, show = setOf("kv"), light = setOf("redis", "kv"), pulse = "app" to "redis", card = BoardCard("Redis", "A notepad in memory: save a value under a key, get it back by name.")),
                    Beat(6, light = setOf("app"), card = BoardCard("A small news app", "Top stories are the same for everyone for minutes: cache them.")),
                ),
            ),
            // 7: "So how does my app use it?"
            Scene(
                7, "Cache-aside",
                DiagramSpec(
                    "Check the cache; on a miss read the database, write the cache with a TTL, and return.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.5f, w = 0.3f, h = 0.22f),
                        DNode("redis", "Redis", 0.76f, 0.18f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "TTL 10 min"),
                        DNode("db", "Database", 0.76f, 0.82f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "redis", "1 look"),
                        DEdge("app", "db", "2 miss: read"),
                        DEdge("db", "redis", "3 save", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("app")),
                    Beat(8, show = setOf("redis", "db"), light = setOf("redis"), pulse = "app" to "db", card = BoardCard("Cache-aside", "Look in the cache first. On a miss, read the database and save it.")),
                    Beat(10, light = setOf("redis"), pulse = "db" to "redis", card = BoardCard("TTL: time to live", "A best-before date on the key. Then the next read gets a fresh copy.")),
                    Beat(12, light = setOf("db"), pulse = "app" to "redis", card = BoardCard("Invalidate on write", "A user changes their photo: delete their cached profile key.")),
                ),
            ),
            // 14: "There are two other patterns. Write-through means every write goes to ..."
            Scene(
                14, "Two more patterns",
                DiagramSpec(
                    "Write-through writes the cache and database together; a session store keeps sessions off the web servers.",
                    nodes = listOf(
                        DNode("wt", "Write-through", 0.26f, 0.24f, w = 0.46f, h = 0.24f, sub = "always current, slower writes"),
                        DNode("ss", "Session store", 0.76f, 0.24f, w = 0.44f, h = 0.24f, sub = "add servers freely"),
                        DNode("redis", "Redis", 0.5f, 0.76f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("wt", "redis"),
                        DEdge("ss", "redis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("wt", "ss", "redis"), light = setOf("wt", "ss"), card = BoardCard("Write-through, session store", "Write cache and database together, or keep sessions in Redis.")),
                ),
            ),
            // 15: "How does my code connect to it?"
            Scene(
                15, "One phone line",
                DiagramSpec(
                    "Every request shares one ConnectionMultiplexer, created once for the life of the app.",
                    nodes = listOf(
                        DNode("r1", "Request", 0.16f, 0.14f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Request", 0.16f, 0.4f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Request", 0.16f, 0.66f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("mux", "Connection Multiplexer", 0.52f, 0.4f, w = 0.34f, h = 0.24f, sub = "just one"),
                        DNode("redis", "Redis", 0.86f, 0.4f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("bad", "One per request: out of connections", 0.5f, 0.9f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("r1", "mux"),
                        DEdge("r2", "mux"),
                        DEdge("r3", "mux"),
                        DEdge("mux", "redis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("redis")),
                    Beat(16, show = setOf("mux"), light = setOf("mux"), pulse = "mux" to "redis", card = BoardCard("ConnectionMultiplexer", "The phone line to the cache, in the dot net client.")),
                    Beat(17, show = setOf("r1", "r2", "r3", "bad"), light = setOf("bad")),
                    Beat(18, light = setOf("mux"), pulse = "r2" to "mux", card = BoardCard("C#", "services.AddSingleton(ConnectionMultiplexer.Connect(conn));", code = true)),
                ),
            ),
            // 19: "And what can I actually store?"
            Scene(
                19, "What you can store",
                DiagramSpec(
                    "Strings, counters, hashes, lists and sorted sets each fit a pattern: values, limits, objects, queues, leaderboards.",
                    nodes = listOf(
                        DNode("str", "String", 0.26f, 0.13f, w = 0.46f, h = 0.18f, tone = 1, sub = "a value with expiry"),
                        DNode("cnt", "Counter", 0.76f, 0.13f, w = 0.44f, h = 0.18f, tone = 1, sub = "rate limiting"),
                        DNode("hash", "Hash", 0.26f, 0.43f, w = 0.46f, h = 0.18f, tone = 1, sub = "an object's fields"),
                        DNode("list", "List", 0.76f, 0.43f, w = 0.44f, h = 0.18f, tone = 1, sub = "a simple queue"),
                        DNode("zset", "Sorted set", 0.5f, 0.8f, w = 0.6f, h = 0.24f, sub = "members in order by score"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("str", "cnt", "hash", "list", "zset"), light = setOf("zset")),
                    Beat(24, light = setOf("zset"), card = BoardCard("A fitness app's leaderboard", "Users are members, steps are the score: the top ten comes back fast.")),
                ),
            ),
            // 25: "What happens when the cache fills up?"
            Scene(
                25, "When memory fills",
                DiagramSpec(
                    "The eviction policy drops keys with a TTL first; anything can vanish, so keep the slow path.",
                    nodes = listOf(
                        DNode("redis", "Redis: full", 0.24f, 0.26f, w = 0.4f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("evict", "Eviction policy", 0.74f, 0.26f, w = 0.44f, h = 0.22f, sub = "volatile LRU"),
                        DNode("slow", "Slow path to the real store", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("redis", "evict", "drops keys"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("redis")),
                    Beat(26, show = setOf("evict"), light = setOf("evict"), pulse = "redis" to "evict", card = BoardCard("Eviction policy", "Least recently used keys with a TTL go first. Always set an expiry.")),
                    Beat(28, show = setOf("slow"), light = setOf("slow"), card = BoardCard("Redis is not a database", "Assume anything can disappear. Always keep the slow path.")),
                ),
            ),
            // 29: "Good to know before I learn it the hard way. Are there different sizes..."
            Scene(
                29, "Tiers",
                DiagramSpec(
                    "Basic for testing, Standard for production, Premium for disk and private networks, Enterprise for modules.",
                    nodes = listOf(
                        DNode("basic", "Basic", 0.26f, 0.16f, w = 0.46f, h = 0.22f, tone = 2, sub = "no SLA: testing"),
                        DNode("std", "Standard", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "two copies: production"),
                        DNode("prem", "Premium", 0.26f, 0.52f, w = 0.46f, h = 0.22f, sub = "disk, private network"),
                        DNode("ent", "Enterprise", 0.76f, 0.52f, w = 0.44f, h = 0.22f, sub = "modules, multi-region"),
                        DNode("flash", "Enterprise Flash", 0.5f, 0.86f, w = 0.56f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "very large caches"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("basic", "std", "prem"), light = setOf("std"), card = BoardCard("SLA", "The uptime promise. Basic has none, so it's for testing only.")),
                    Beat(32, show = setOf("ent", "flash"), light = setOf("prem", "ent"), card = BoardCard("A shop on a private network", "Needs Premium. A hobby app can test on Basic.")),
                ),
            ),
            // 33: "Anything else people get wrong?"
            Scene(
                33, "Mistakes to avoid",
                DiagramSpec(
                    "Three ways to get Redis wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("shared", "One user's data under a shared key", 0.5f, 0.16f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("basic", "Basic in production", 0.5f, 0.5f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("keys", "Access keys in code", 0.5f, 0.84f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("shared", "basic", "keys"), card = BoardCard("Instead of access keys", "Managed identity where supported, or the string in Key Vault.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's Redis questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("conn", "How many connections", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one, shared"),
                        DNode("board", "Leaderboard", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sorted set"),
                        DNode("aside", "Cache first, fill on miss", 0.5f, 0.45f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "cache-aside, TTL"),
                        DNode("prem", "Disk or private network", 0.26f, 0.82f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("ent", "RediSearch, multi-region", 0.76f, 0.82f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Enterprise"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("conn", "board", "aside"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("prem", "ent"), light = setOf("prem", "ent"), card = BoardCard("And Basic?", "Never for production: no replica, no SLA.")),
                    Beat(40, card = BoardCard("Today", "SET greeting hello EX 60, GET it, wait, GET again; then ZADD and ZREVRANGE.", code = true)),
                ),
            ),
        ),
    )

    private fun cdn() = PodcastBoard(
        "az-cdn",
        listOf(
            // 0: "Hi, and welcome back. Today is about making your website fast for peop..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Every visitor's request travels all the way to one far-away server, so distant users wait.",
                    nodes = listOf(
                        DNode("user", "Lahore", 0.16f, 0.4f, w = 0.22f, h = 0.2f, kind = NodeKind.USER, tone = 1),
                        DNode("server", "Server in Europe", 0.78f, 0.4f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("slow", "Slow: it's far away", 0.5f, 0.82f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("user", "server", "a long trip"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("user", "server"), pulse = "user" to "server"),
                    Beat(1, show = setOf("slow"), light = setOf("slow"), card = BoardCard("A faster server?", "No. The problem is the distance, not the server.")),
                ),
            ),
            // 2: "Right. So you bring copies of your files closer to them. That's what a..."
            Scene(
                2, "Copies near your users",
                DiagramSpec(
                    "Users hit the nearest edge. A miss goes to the origin once; after that the edge serves hits.",
                    nodes = listOf(
                        DNode("u1", "Doha", 0.12f, 0.25f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("u2", "Lahore", 0.12f, 0.75f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("e1", "Edge", 0.45f, 0.25f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("e2", "Edge", 0.45f, 0.75f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("origin", "Origin", 0.84f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "your real files"),
                    ),
                    edges = listOf(
                        DEdge("u1", "e1"),
                        DEdge("u2", "e2"),
                        DEdge("e1", "origin", "miss"),
                        DEdge("e2", "origin", "miss"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("u1", "u2")),
                    Beat(4, show = setOf("e1", "e2"), light = setOf("e1", "e2"), card = BoardCard("CDN: corner shops", "Copies of pictures, styles and scripts on edges, close to your users.")),
                    Beat(6, show = setOf("origin"), light = setOf("origin"), pulse = "e1" to "origin", card = BoardCard("Origin, miss and hit", "A miss fetches from the origin once. Everyone after gets a hit.")),
                    Beat(7, light = setOf("e1"), pulse = "u1" to "e1"),
                ),
            ),
            // 8: "Exactly. In Azure, the product is called Azure Front Door. Front Door ..."
            Scene(
                8, "Azure Front Door",
                DiagramSpec(
                    "Front Door is the current CDN and global entry point; classic CDN profiles share the same ideas.",
                    nodes = listOf(
                        DNode("std", "Front Door Standard", 0.27f, 0.25f, w = 0.46f, h = 0.22f, sub = "caching, HTTPS, routing"),
                        DNode("prem", "Front Door Premium", 0.75f, 0.25f, w = 0.44f, h = 0.22f, tone = 1, sub = "managed WAF, Private Link"),
                        DNode("folio", "Photo portfolio", 0.27f, 0.62f, w = 0.4f, kind = NodeKind.PILL, tone = 1),
                        DNode("bank", "Bank website", 0.75f, 0.62f, w = 0.4f, kind = NodeKind.PILL, tone = 1),
                        DNode("classic", "Classic CDN: same ideas", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("std", "folio"),
                        DEdge("prem", "bank"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("std"), light = setOf("std"), card = BoardCard("Front Door Standard", "The global front entrance: keeps copies, HTTPS, routing, basic firewall.")),
                    Beat(10, show = setOf("prem", "folio", "bank", "classic"), light = setOf("prem", "bank"), card = BoardCard("Which one?", "A small photo portfolio: Standard. A bank that locks its origin: Premium.")),
                ),
            ),
            // 11: "Okay. How long does a corner shop keep its stock?"
            Scene(
                11, "How long a copy lasts",
                DiagramSpec(
                    "The origin's Cache-Control header sets the TTL, unless a caching rule overrides it.",
                    nodes = listOf(
                        DNode("origin", "Origin", 0.18f, 0.22f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("edge", "Edge", 0.8f, 0.22f, w = 0.3f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("cc", "Cache-Control", 0.5f, 0.5f, w = 0.4f, h = 0.14f, kind = NodeKind.NOTE, sub = "the best-before date"),
                        DNode("honor", "Honor origin", 0.17f, 0.84f, w = 0.3f, kind = NodeKind.PILL, sub = "default"),
                        DNode("over", "Override", 0.5f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "your own TTL"),
                        DNode("bypass", "Bypass cache", 0.83f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 2, sub = "never keep"),
                    ),
                    edges = listOf(
                        DEdge("origin", "edge", "file + label"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("origin", "edge")),
                    Beat(12, show = setOf("cc"), light = setOf("cc"), pulse = "origin" to "edge", card = BoardCard("TTL: time to live", "How long an edge keeps its copy. Set by the Cache-Control header.")),
                    Beat(16, show = setOf("honor", "over", "bypass"), light = setOf("honor"), card = BoardCard("Three caching rules", "Honor origin is the default. Override for files with no label.")),
                ),
            ),
            // 17: "When would I never want a copy?"
            Scene(
                17, "Never cache personal pages",
                DiagramSpec(
                    "Shared files are cached at the edge; personalised pages bypass the cache so nobody sees another user's data.",
                    nodes = listOf(
                        DNode("photos", "Product photos", 0.25f, 0.25f, w = 0.4f, h = 0.2f, sub = "same for everyone"),
                        DNode("basket", "Your basket", 0.75f, 0.25f, w = 0.4f, h = 0.2f, tone = 1, sub = "different per shopper"),
                        DNode("cache", "Cache it", 0.25f, 0.7f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("bypass", "Bypass cache", 0.75f, 0.7f, w = 0.34f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("photos", "cache"),
                        DEdge("basket", "bypass"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("photos", "basket")),
                    Beat(18, show = setOf("cache", "bypass"), light = setOf("basket", "bypass"), pulse = "basket" to "bypass", card = BoardCard("A small online shop", "Cache the product photos. Bypass the cache for each shopper's basket.")),
                ),
            ),
            // 20: "It's the worst mistake in this topic. Now, query strings. A query stri..."
            Scene(
                20, "Query strings",
                DiagramSpec(
                    "Query string caching decides whether different URL parameters make separate cached copies.",
                    nodes = listOf(
                        DNode("small", "map?size=small", 0.25f, 0.2f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("large", "map?size=large", 0.75f, 0.2f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("use", "Use: two copies", 0.5f, 0.5f, w = 0.46f, h = 0.18f),
                        DNode("ignore", "Ignore: one copy", 0.5f, 0.84f, w = 0.46f, h = 0.18f, tone = 2, sub = "for tracking tags"),
                    ),
                    edges = listOf(
                        DEdge("small", "use"),
                        DEdge("large", "use"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("small", "large"), card = BoardCard("Query string", "The bit after the question mark in a web address.")),
                    Beat(22, show = setOf("use", "ignore"), light = setOf("use"), card = BoardCard("A weather app's map", "Small and large really differ: two copies. A tracking tag: ignore it.")),
                ),
            ),
            // 23: "Now the big one. I've changed a file, but users still see the old one...."
            Scene(
                23, "Changing a cached file",
                DiagramSpec(
                    "Purge removes the cached copy before its TTL; a versioned name gives a new URL that cannot be stale.",
                    nodes = listOf(
                        DNode("old", "Old copy", 0.5f, 0.15f, w = 0.5f, h = 0.2f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("purge", "Purge", 0.22f, 0.55f, w = 0.36f, h = 0.2f, sub = "emergency, per path"),
                        DNode("hash", "New file name", 0.78f, 0.55f, w = 0.36f, h = 0.2f, sub = "content hash"),
                        DNode("ttl1", "TTL of one second: no", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("purge", "old", "throws away"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("old"), light = setOf("old")),
                    Beat(24, show = setOf("purge"), light = setOf("purge"), pulse = "purge" to "old", card = BoardCard("Purge", "Removes the copy before its TTL. Takes minutes, per path, every edge.")),
                    Beat(26, show = setOf("hash"), light = setOf("hash"), card = BoardCard("A versioned name", "app.3f2a.js", code = true)),
                    Beat(27, show = setOf("ttl1")),
                    Beat(28, light = setOf("hash"), card = BoardCard("The topic's example", "Hashed diagram names, a five minute TTL on help pages, API bypassed.")),
                ),
            ),
            // 29: "What else do people get wrong?"
            Scene(
                29, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get edge caching wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("personal", "Caching personal answers", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("slow", "A purge is slow, per path", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("blob", "Origin on the blob address", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("qs", "Copies per useless tag", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("personal", "slow", "blob", "qs"), card = BoardCard("Origin address", "Point it at the static website address, not the plain blob one.")),
                ),
            ),
            // 31: "So what should I remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's caching questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("stale", "Users see the old file", 0.26f, 0.15f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "purge, versioned names"),
                        DNode("diff", "Different content per user", 0.76f, 0.15f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "bypass cache"),
                        DNode("ttl", "What sets the TTL", 0.26f, 0.5f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "Cache-Control or a rule"),
                        DNode("fd", "Current CDN", 0.76f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "Front Door"),
                        DNode("prem", "Managed WAF, Private Link", 0.5f, 0.85f, w = 0.7f, h = 0.2f, sub = "Front Door Premium"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("stale", "diff"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, show = setOf("ttl", "fd", "prem"), light = setOf("ttl", "fd")),
                    Beat(36, card = BoardCard("Today", "Static website, Front Door profile, a miss then a hit, then purge.")),
                ),
            ),
        ),
    )

    private fun identity() = PodcastBoard(
        "az-identity",
        listOf(
            // 0: "Hi, and welcome back. Today is about signing people in. Here's a puzzl..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A job runs at night with nobody signed in, yet it must read everyone's calendar.",
                    nodes = listOf(
                        DNode("job", "Nightly job", 0.2f, 0.4f, w = 0.32f, h = 0.22f, sub = "two in the morning"),
                        DNode("cal", "Everyone's calendars", 0.78f, 0.4f, w = 0.34f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("who", "Whose permission?", 0.5f, 0.84f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("job", "cal", "read"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("job", "cal"), pulse = "job" to "cal"),
                    Beat(1, show = setOf("who"), light = setOf("who"), card = BoardCard("Nobody is signed in", "So it can't be acting for a user. Hold that thought.")),
                ),
            ),
            // 2: "Good instinct. We'll come back to it. First, the big picture. Microsof..."
            Scene(
                2, "The front desk",
                DiagramSpec(
                    "Users sign in with the Microsoft identity platform and come back to your app with a token as proof.",
                    nodes = listOf(
                        DNode("user", "User", 0.14f, 0.3f, w = 0.2f, h = 0.2f, kind = NodeKind.USER, tone = 1),
                        DNode("idp", "Microsoft sign-in", 0.68f, 0.3f, w = 0.52f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("app", "Your to-do app", 0.22f, 0.78f, w = 0.36f, h = 0.2f),
                        DNode("token", "Token", 0.72f, 0.78f, w = 0.3f, kind = NodeKind.PILL, sub = "the proof"),
                    ),
                    edges = listOf(
                        DEdge("user", "idp", "signs in"),
                        DEdge("token", "app", "returned"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("idp"), light = setOf("idp"), card = BoardCard("Entra ID", "The new name for Azure Active Directory: an organisation's people and apps.")),
                    Beat(4, show = setOf("user", "app", "token"), light = setOf("token"), pulse = "user" to "idp", card = BoardCard("A hotel front desk", "A team to-do app stores no passwords. Microsoft signs users in.")),
                ),
            ),
            // 5: "How does the front desk know about my app?"
            Scene(
                5, "Registering your app",
                DiagramSpec(
                    "One application object in the home tenant; one service principal per tenant that uses the app, holding its permissions.",
                    nodes = listOf(
                        DNode("obj", "Application object", 0.5f, 0.15f, w = 0.56f, h = 0.2f, tone = 1, sub = "home tenant, client id"),
                        DNode("sp1", "Service principal", 0.24f, 0.58f, w = 0.42f, h = 0.2f, sub = "home tenant"),
                        DNode("sp2", "Service principal", 0.76f, 0.58f, w = 0.42f, h = 0.2f, tone = 2, sub = "another company"),
                        DNode("note", "Roles and consent attach here", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("obj", "sp1", "defines"),
                        DEdge("obj", "sp2", "defines"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("obj"), light = setOf("obj"), card = BoardCard("Application object", "The single record of your app, in your home tenant. A tenant is one building.")),
                    Beat(8, show = setOf("sp1", "sp2", "note"), light = setOf("sp2", "note"), card = BoardCard("Service principal", "Your app's visitor pass in each tenant. Permissions attach to it.")),
                ),
            ),
            // 9: "What goes into a registration?"
            Scene(
                9, "What a registration holds",
                DiagramSpec(
                    "The main settings of an app registration and what each one means.",
                    nodes = listOf(
                        DNode("acct", "Account types", 0.26f, 0.13f, w = 0.46f, h = 0.18f, tone = 1, sub = "who can sign in"),
                        DNode("redir", "Redirect URI", 0.76f, 0.13f, w = 0.44f, h = 0.18f, tone = 1, sub = "the return address"),
                        DNode("perm", "API permissions", 0.26f, 0.47f, w = 0.46f, h = 0.18f, tone = 1, sub = "what it wants to use"),
                        DNode("secret", "Secret or certificate", 0.76f, 0.47f, w = 0.44f, h = 0.18f, tone = 1, sub = "server apps only"),
                        DNode("expose", "Expose an API", 0.5f, 0.83f, w = 0.6f, h = 0.18f, tone = 1, sub = "the scopes you offer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("acct", "redir", "perm"), light = setOf("redir"), card = BoardCard("Redirect URI", "Where the tokens are sent back, like the return address on an envelope.")),
                    Beat(12, show = setOf("secret", "expose"), light = setOf("secret", "expose"), card = BoardCard("Scope", "One named thing an app may do, like read logs.")),
                ),
            ),
            // 13: "Okay, tokens. You said proof. Is it one ticket?"
            Scene(
                13, "Three tokens",
                DiagramSpec(
                    "The ID token is for your app; the access token goes to the API, which checks its audience; the refresh token gets new ones.",
                    nodes = listOf(
                        DNode("idt", "ID token", 0.2f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.PILL, sub = "who the user is"),
                        DNode("at", "Access token", 0.6f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.PILL, sub = "what it may do"),
                        DNode("rt", "Refresh", 0.88f, 0.5f, w = 0.22f, h = 0.18f, kind = NodeKind.PILL, tone = 2, sub = "new tokens"),
                        DNode("app", "Your app", 0.2f, 0.8f, w = 0.32f, h = 0.2f),
                        DNode("api", "The API", 0.6f, 0.8f, w = 0.32f, h = 0.2f, sub = "checks audience"),
                    ),
                    edges = listOf(
                        DEdge("idt", "app", "show name"),
                        DEdge("at", "api", "sent to"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("idt", "at", "rt", "app", "api"), light = setOf("idt", "at"), pulse = "at" to "api"),
                    Beat(16, light = setOf("api"), card = BoardCard("Access token is for the API", "Not for your app's buttons. The API checks the audience it was made for.")),
                ),
            ),
            // 17: "Someone else's concert ticket. Got it. Back to the puzzle, then."
            Scene(
                17, "Two kinds of permission",
                DiagramSpec(
                    "Delegated means a signed-in user acts through the app; application means the app acts alone, with admin consent.",
                    nodes = listOf(
                        DNode("del", "Delegated", 0.26f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "a user is there"),
                        DNode("appp", "Application", 0.75f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "no user at all"),
                        DNode("sara", "Sara's own tasks", 0.26f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "scope claim"),
                        DNode("job", "Nightly report", 0.75f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "roles claim"),
                        DNode("admin", "Admin consent, always", 0.75f, 0.9f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("del", "sara"),
                        DEdge("appp", "job"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("del"), light = setOf("del"), card = BoardCard("Delegated permission", "A friend with your key: acts for you, and only does what you could do.")),
                    Beat(20, show = setOf("appp", "admin"), light = setOf("appp", "admin"), card = BoardCard("Consent", "Saying yes on the screen: this app wants to read your profile. Allow?")),
                    Beat(22, show = setOf("sara", "job"), light = setOf("sara", "job"), card = BoardCard("Is a person there?", "Sara reads her own tasks: delegated. Nightly report: application.")),
                ),
            ),
            // 23: "You said only server apps get a secret. What about a phone app?"
            Scene(
                23, "Public or confidential",
                DiagramSpec(
                    "Public clients can't keep a secret, so they use PKCE; confidential clients on a server hold a secret or certificate.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "mobile, desktop, SPA"),
                        DNode("conf", "Confidential client", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "web app, server job"),
                        DNode("pkce", "PKCE", 0.26f, 0.65f, w = 0.34f, kind = NodeKind.PILL, sub = "ticket + secret word"),
                        DNode("sec", "Secret or certificate", 0.75f, 0.65f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("pub", "pkce"),
                        DEdge("conf", "sec"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("pub", "conf", "sec"), light = setOf("pub"), card = BoardCard("Can it keep a secret?", "Anything on a user's device can be taken apart. A server can keep one.")),
                    Beat(26, show = setOf("pkce"), light = setOf("pkce"), pulse = "pub" to "pkce", card = BoardCard("PKCE: a cloakroom", "Ticket plus a secret word you made up. A copied ticket alone is useless.")),
                ),
            ),
            // 27: "Okay. And then there are flows. What's a flow?"
            Scene(
                27, "The five flows",
                DiagramSpec(
                    "A flow is the steps an app follows to get a token; each scenario has its own.",
                    nodes = listOf(
                        DNode("code", "Auth code + PKCE", 0.26f, 0.13f, w = 0.46f, h = 0.18f, sub = "a user, a browser"),
                        DNode("cc", "Client credentials", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "no user"),
                        DNode("obo", "On-behalf-of", 0.26f, 0.47f, w = 0.46f, h = 0.18f, tone = 1, sub = "API to API as user"),
                        DNode("dev", "Device code", 0.76f, 0.47f, w = 0.44f, h = 0.18f, tone = 1, sub = "no browser: TV, CLI"),
                        DNode("imp", "Implicit", 0.5f, 0.83f, w = 0.5f, h = 0.18f, tone = 2, sub = "old, deprecated"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("code", "cc"), light = setOf("code", "cc"), card = BoardCard("The two big ones", "A user in a browser: auth code with PKCE. No user: client credentials.")),
                    Beat(32, show = setOf("obo", "dev", "imp"), light = setOf("obo", "dev"), card = BoardCard("Implicit is deprecated", "Pick authorization code with PKCE instead.")),
                ),
            ),
            // 33: "Can you put it all together in one app?"
            Scene(
                33, "One app, all together",
                DiagramSpec(
                    "TwoTracks: a public mobile client, an API exposing a scope, Graph for the profile, and a nightly job with client credentials.",
                    nodes = listOf(
                        DNode("mobile", "Mobile app", 0.2f, 0.25f, w = 0.32f, h = 0.2f, sub = "public, PKCE"),
                        DNode("api", "TwoTracks API", 0.78f, 0.25f, w = 0.36f, h = 0.2f, sub = "Logs read-write"),
                        DNode("graph", "Microsoft Graph", 0.78f, 0.72f, w = 0.36f, h = 0.2f, tone = 1),
                        DNode("night", "Nightly report", 0.2f, 0.72f, w = 0.32f, h = 0.2f, tone = 2, sub = "client credentials"),
                    ),
                    edges = listOf(
                        DEdge("mobile", "api", "access token"),
                        DEdge("mobile", "graph", "access token"),
                        DEdge("night", "api", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("mobile", "api", "graph", "night"), light = setOf("mobile"), pulse = "mobile" to "api", card = BoardCard("Usaid signs in once", "An ID token for his name; access tokens for the API and for Graph.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's identity questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("nouser", "Nobody signed in", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "application, admin consent"),
                        DNode("own", "User's own data", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "delegated"),
                        DNode("spa", "Mobile or SPA", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "auth code + PKCE"),
                        DNode("tenant", "Another tenant", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "service principal"),
                        DNode("obo", "API calls API as the user", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "on-behalf-of"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("nouser", "own", "spa"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("tenant", "obo"), light = setOf("tenant", "obo"), card = BoardCard("And always", "The API checks the audience of every access token.")),
                    Beat(40, card = BoardCard("Today", "Register an app, note its ids, add User Read, compare platform settings.")),
                ),
            ),
        ),
    )

    private fun msal() = PodcastBoard(
        "az-msal",
        listOf(
            // 0: "Hi, and welcome back. Last time we met tokens, the proof your app gets..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Your app asks for a token and gets MsalUiRequiredException back. What is it saying?",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.35f, w = 0.32f, h = 0.22f),
                        DNode("idp", "Microsoft sign-in", 0.72f, 0.35f, w = 0.5f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("ex", "MsalUiRequired Exception", 0.5f, 0.82f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("app", "idp", "a token?"),
                        DEdge("idp", "ex", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "idp", "ex"), light = setOf("ex"), pulse = "app" to "idp"),
                    Beat(1, card = BoardCard("Busy, try again?", "Too easy. The name of the error does the work.")),
                ),
            ),
            // 2: "It is too easy. We'll come back to it. First, the tool. It's called MS..."
            Scene(
                2, "What MSAL does",
                DiagramSpec(
                    "MSAL talks to the sign-in service for you: it shows the window, caches tokens and renews them.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("msal", "MSAL", 0.5f, 0.55f, w = 0.34f, h = 0.2f, sub = "the helper"),
                        DNode("idp", "Microsoft sign-in", 0.72f, 0.2f, w = 0.5f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("cache", "Token cache", 0.5f, 0.88f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "msal", "scopes"),
                        DEdge("msal", "idp"),
                        DEdge("msal", "cache", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("msal"), light = setOf("msal")),
                    Beat(4, show = setOf("app", "idp", "cache"), light = setOf("cache"), pulse = "app" to "msal", card = BoardCard("Scope", "One named permission, like reading the user's profile.")),
                ),
            ),
            // 5: "So I never build token requests by hand."
            Scene(
                5, "Two builders",
                DiagramSpec(
                    "The public client builder is for apps on a device; the confidential client builder is for apps on a server.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "mobile, desktop, CLI"),
                        DNode("conf", "Confidential client", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "web app, API, job"),
                        DNode("phone", "Habit tracker app", 0.26f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "no secret"),
                        DNode("server", "Its API", 0.75f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "keeps a secret"),
                    ),
                    edges = listOf(
                        DEdge("pub", "phone"),
                        DEdge("conf", "server"),
                        DEdge("phone", "server", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("pub", "conf"), light = setOf("pub", "conf"), card = BoardCard("A builder", "The starting point in code. One for devices, one for servers.")),
                    Beat(8, show = setOf("phone", "server"), light = setOf("phone"), card = BoardCard("A small habit tracker", "Android app: public client, no secret. Its API: confidential.")),
                ),
            ),
            // 9: "Okay. So how do I actually ask for a token?"
            Scene(
                9, "Silent first",
                DiagramSpec(
                    "Always try silent first; fall back to an interactive prompt only on MsalUiRequiredException.",
                    nodes = listOf(
                        DNode("silent", "AcquireToken Silent", 0.22f, 0.3f, w = 0.4f, h = 0.22f, sub = "cache or refresh"),
                        DNode("tok", "Token", 0.75f, 0.13f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("ex", "MsalUiRequired Exception", 0.75f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("inter", "AcquireToken Interactive", 0.5f, 0.84f, w = 0.48f, h = 0.22f, sub = "sign-in window"),
                    ),
                    edges = listOf(
                        DEdge("silent", "tok", "hit"),
                        DEdge("silent", "ex", "can't"),
                        DEdge("ex", "inter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("silent", "tok"), light = setOf("silent"), pulse = "silent" to "tok", card = BoardCard("AcquireTokenSilent", "Looks in the cache. No prompt. Quietly renews an old token.")),
                    Beat(12, show = setOf("ex"), light = setOf("ex"), pulse = "silent" to "ex", card = BoardCard("The user must be asked", "Agree to a permission, a second check on their phone, or a lapsed session.")),
                    Beat(14, show = setOf("inter"), light = setOf("inter"), pulse = "ex" to "inter", card = BoardCard("C#", "try { AcquireTokenSilent } catch (MsalUiRequiredException) { AcquireTokenInteractive }", code = true)),
                ),
            ),
            // 15: "What if I call interactive every time? Simpler."
            Scene(
                15, "Silent for months",
                DiagramSpec(
                    "Interactive on every call means constant prompts; silent first keeps working for months thanks to the refresh token.",
                    nodes = listOf(
                        DNode("bad", "Interactive every time", 0.5f, 0.13f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("first", "First run", 0.18f, 0.6f, w = 0.3f, h = 0.2f, sub = "sign in once"),
                        DNode("later", "A month later", 0.5f, 0.6f, w = 0.28f, h = 0.2f, sub = "silent"),
                        DNode("refresh", "Refresh token", 0.83f, 0.6f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("first", "later"),
                        DEdge("refresh", "later", "new token"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("bad"), light = setOf("bad"), card = BoardCard("A notes app", "Asks you to sign in on every save. Nobody keeps that app.")),
                    Beat(18, show = setOf("first", "later", "refresh"), light = setOf("later", "refresh"), pulse = "refresh" to "later", card = BoardCard("The TwoTracks Android app", "Signs in once, then silent works for months thanks to the refresh token.")),
                ),
            ),
            // 19: "And a job with nobody signed in at all?"
            Scene(
                19, "No user at all",
                DiagramSpec(
                    "A job with no user uses client credentials: AcquireTokenForClient with the resource's /.default scope.",
                    nodes = listOf(
                        DNode("job", "Nightly report job", 0.24f, 0.2f, w = 0.4f, h = 0.2f, sub = "confidential client"),
                        DNode("for", "AcquireToken ForClient", 0.74f, 0.2f, w = 0.46f, h = 0.2f),
                        DNode("def", "/.default", 0.74f, 0.6f, w = 0.34f, kind = NodeKind.PILL, sub = "everything approved"),
                        DNode("nope", "User Read: fails", 0.24f, 0.6f, w = 0.38f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("job", "for"),
                        DEdge("for", "def", "scope"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("job", "for", "def"), light = setOf("for"), pulse = "job" to "for", card = BoardCard("A small online shop", "A nightly job emails sales reports. No user: client credentials.")),
                    Beat(22, show = setOf("nope"), light = setOf("def"), card = BoardCard("C#", "AcquireTokenForClient(new[] { \"api://tt-api/.default\" })", code = true)),
                ),
            ),
            // 23: "Can I ask for Microsoft Graph and my own API in one call? Saves time."
            Scene(
                23, "One token, one audience",
                DiagramSpec(
                    "Each access token has a single audience, so ask for Graph scopes and your API's scopes in separate calls.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.45f, w = 0.3f, h = 0.2f),
                        DNode("t1", "Token 1", 0.5f, 0.2f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("t2", "Token 2", 0.5f, 0.7f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("graph", "Microsoft Graph", 0.83f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("api", "Your API", 0.83f, 0.7f, w = 0.3f, h = 0.2f),
                        DNode("oa", "offline access: refresh", 0.5f, 0.93f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "t1"),
                        DEdge("app", "t2"),
                        DEdge("t1", "graph", "audience"),
                        DEdge("t2", "api", "audience"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("app", "graph", "api")),
                    Beat(24, show = setOf("t1", "t2"), light = setOf("t1", "t2"), pulse = "t1" to "graph", card = BoardCard("A train ticket for one line", "One audience per token. Graph and your API: two separate calls.")),
                    Beat(26, show = setOf("oa"), light = setOf("oa"), card = BoardCard("Sign-in scopes", "openid and profile come from OpenID Connect. offline_access: a refresh token.")),
                ),
            ),
            // 27: "Any other methods I should know?"
            Scene(
                27, "More methods",
                DiagramSpec(
                    "The other MSAL calls, each for its own scenario, and a shared token cache for web apps on many servers.",
                    nodes = listOf(
                        DNode("obo", "OnBehalfOf", 0.26f, 0.13f, w = 0.46f, h = 0.18f, sub = "API calls API as user"),
                        DNode("dev", "WithDeviceCode", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "no browser"),
                        DNode("code", "ByAuthorizationCode", 0.5f, 0.45f, w = 0.56f, h = 0.18f, sub = "web app signs users in"),
                        DNode("web", "Identity Web", 0.22f, 0.82f, w = 0.34f, h = 0.2f, tone = 1),
                        DNode("redis", "Shared cache", 0.76f, 0.82f, w = 0.34f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "Redis or SQL"),
                    ),
                    edges = listOf(
                        DEdge("web", "redis", "tokens"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("obo", "dev", "code"), light = setOf("obo", "dev"), card = BoardCard("AcquireToken...", "...OnBehalfOf, WithDeviceCode, ByAuthorizationCode")),
                    Beat(30, show = setOf("web", "redis"), light = setOf("redis"), pulse = "web" to "redis", card = BoardCard("Many servers?", "Keep tokens in a shared cache. Memory caches vanish when you add servers.")),
                ),
            ),
            // 31: "So what should I remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's MSAL questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("prompts", "Constant prompts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "silent first"),
                        DNode("daemon", "Daemon, no user", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ForClient, /.default"),
                        DNode("mix", "Two APIs, one call", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one audience"),
                        DNode("nobrowser", "No browser", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "device code"),
                        DNode("builders", "Users or servers?", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "public or confidential builder"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("prompts", "daemon"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, show = setOf("mix", "nobrowser", "builders"), light = setOf("mix", "nobrowser")),
                    Beat(36, card = BoardCard("Today", "Console app, a token interactively, decode aud and scp, then the silent path.")),
                ),
            ),
        ),
    )

    private fun graph() = PodcastBoard(
        "az-graph",
        listOf(
            // 0: "Hi, and welcome back. Today is Microsoft Graph. Here's a puzzle to sta..."
            Scene(
                0, "The puzzle",
                DiagramSpec(
                    "Thirty separate requests to Microsoft Graph: how few calls can carry them?",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("graph", "Microsoft Graph", 0.78f, 0.42f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("thirty", "30 requests", 0.2f, 0.8f, w = 0.34f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("app", "graph", "calls"),
                        DEdge("thirty", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "graph", "thirty"), light = setOf("thirty"), pulse = "app" to "graph"),
                    Beat(1, card = BoardCard("One call?", "A fair guess. Hold that thought until the batch part.")),
                ),
            ),
            // 2: "Good instinct. We'll come back to it. First, what Microsoft Graph actu..."
            Scene(
                2, "One front door",
                DiagramSpec(
                    "Microsoft Graph is one web address in front of users, mail, calendars and files in Microsoft 365.",
                    nodes = listOf(
                        DNode("app", "Team dashboard", 0.2f, 0.45f, w = 0.34f, h = 0.2f),
                        DNode("graph", "Microsoft Graph", 0.52f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, sub = "one front door"),
                        DNode("users", "Users", 0.82f, 0.4f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("mail", "Mail", 0.82f, 0.58f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("cal", "Calendar", 0.82f, 0.76f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("files", "Files, Teams", 0.82f, 0.93f, w = 0.3f, h = 0.12f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("app", "graph", "GET"),
                        DEdge("graph", "users"),
                        DEdge("graph", "mail"),
                        DEdge("graph", "cal"),
                        DEdge("graph", "files"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("graph")),
                    Beat(4, show = setOf("users", "mail", "cal", "files"), light = setOf("graph"), card = BoardCard("Microsoft Graph", "One REST API for Microsoft 365: plain web requests, one address.")),
                    Beat(6, show = setOf("app"), light = setOf("app", "users", "cal"), pulse = "app" to "graph", card = BoardCard("A small team dashboard", "Names, photos and free calendar slots all come through Graph.")),
                ),
            ),
            // 7: "And how does Graph know I'm allowed to ask?"
            Scene(
                7, "The right ticket",
                DiagramSpec(
                    "Each access token has one audience; Graph only accepts a token made for Graph.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("token", "Access token", 0.5f, 0.72f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "audience: Graph"),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("own", "Your own API", 0.82f, 0.92f, w = 0.32f, h = 0.14f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "token", "sends"),
                        DEdge("token", "graph", "accepted"),
                        DEdge("token", "own", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("app", "graph")),
                    Beat(8, show = setOf("token"), light = setOf("token"), pulse = "token" to "graph", card = BoardCard("A ticket with a venue", "The token's audience is the venue printed on it. For Graph: Graph.")),
                    Beat(10, show = setOf("own"), light = setOf("own"), card = BoardCard("One token, one audience", "Your own API's token is turned away by Graph. Get a separate one.")),
                    Beat(12, light = setOf("app"), card = BoardCard("Graph Explorer", "Try calls in the browser; its permissions tab shows each scope.")),
                ),
            ),
            // 13: "Okay, I'm in. How do I ask for exactly what I want?"
            Scene(
                13, "Ask for just what you need",
                DiagramSpec(
                    "OData query parameters at the end of a request shape the answer before it comes back.",
                    nodes = listOf(
                        DNode("req", "GET my messages", 0.5f, 0.13f, w = 0.6f, h = 0.18f),
                        DNode("select", "select", 0.24f, 0.45f, w = 0.4f, kind = NodeKind.PILL, sub = "which fields"),
                        DNode("filter", "filter", 0.76f, 0.45f, w = 0.4f, kind = NodeKind.PILL, sub = "only unread"),
                        DNode("orderby", "orderby", 0.24f, 0.72f, w = 0.4f, kind = NodeKind.PILL, tone = 1, sub = "sort"),
                        DNode("top", "top", 0.76f, 0.72f, w = 0.4f, kind = NodeKind.PILL, tone = 1, sub = "page size"),
                        DNode("more", "expand, count, search", 0.5f, 0.93f, w = 0.66f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("req", "select"),
                        DEdge("req", "filter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("req")),
                    Beat(14, show = setOf("select", "filter", "orderby", "top"), light = setOf("select", "filter"), card = BoardCard("OData query parameters", "Add-ons to a request: select, filter, orderby, top.")),
                    Beat(18, light = setOf("select"), card = BoardCard("A phone mail app", "Needs only subject and sender, so select those. Smaller, faster answer.")),
                    Beat(20, show = setOf("more"), light = setOf("more")),
                ),
            ),
            // 21: "Any catch with those?"
            Scene(
                21, "One extra header",
                DiagramSpec(
                    "Count, search and some filters on users and groups only work with the ConsistencyLevel eventual header.",
                    nodes = listOf(
                        DNode("query", "List users with a count", 0.24f, 0.3f, w = 0.4f, h = 0.22f),
                        DNode("header", "ConsistencyLevel: eventual", 0.4f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "a note sent along"),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("query", "graph"),
                        DEdge("header", "query", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("query", "graph")),
                    Beat(22, show = setOf("header"), light = setOf("header"), pulse = "query" to "graph", card = BoardCard("Count or search?", "Add the header ConsistencyLevel: eventual, or the query fails.")),
                ),
            ),
            // 23: "One tiny note. Okay. What if there are thousands of results?"
            Scene(
                23, "Page after page",
                DiagramSpec(
                    "Big results come in pages; follow each page's next link until a page has none.",
                    nodes = listOf(
                        DNode("p1", "Page 1", 0.2f, 0.3f, w = 0.3f, h = 0.18f),
                        DNode("p2", "Page 2", 0.8f, 0.3f, w = 0.3f, h = 0.18f),
                        DNode("p3", "Last page", 0.8f, 0.75f, w = 0.3f, h = 0.18f, sub = "no next link"),
                        DNode("dir", "Directory app", 0.22f, 0.75f, w = 0.34f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2", "next link"),
                        DEdge("p2", "p3", "next link"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("p1", "p2", "p3"), pulse = "p1" to "p2", card = BoardCard("Next link", "The full web address of the following page. Keep following it.")),
                    Beat(26, show = setOf("dir"), light = setOf("dir", "p3"), pulse = "p2" to "p3", card = BoardCard("A company directory app", "Ignore the next link and it quietly lists only the first page.")),
                ),
            ),
            // 27: "Right. Now, back to the puzzle. Thirty calls."
            Scene(
                27, "Batching",
                DiagramSpec(
                    "A batch is one envelope of up to twenty requests, so thirty requests need two batches.",
                    nodes = listOf(
                        DNode("thirty", "30 requests", 0.2f, 0.45f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("b1", "Batch 1", 0.52f, 0.22f, h = 0.18f, kind = NodeKind.PILL, sub = "20"),
                        DNode("b2", "Batch 2", 0.52f, 0.68f, h = 0.18f, kind = NodeKind.PILL, sub = "10"),
                        DNode("graph", "Graph", 0.86f, 0.45f, h = 0.22f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("thirty", "b1"),
                        DEdge("thirty", "b2"),
                        DEdge("b1", "graph"),
                        DEdge("b2", "graph"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("thirty", "graph")),
                    Beat(28, show = setOf("b1"), light = setOf("b1"), pulse = "b1" to "graph", card = BoardCard("A batch", "One envelope, up to twenty requests, each with its own id and answer.")),
                    Beat(29, show = setOf("b2"), light = setOf("b1", "b2"), pulse = "b2" to "graph"),
                    Beat(30, card = BoardCard("A profile page", "Details, photo and manager: three requests, one batch, one round trip.")),
                ),
            ),
            // 31: "What if Graph thinks I'm asking too much?"
            Scene(
                31, "Slow down",
                DiagramSpec(
                    "A 429 answer means Graph is throttling you; wait as long as its Retry-After header says.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.35f, w = 0.3f, h = 0.2f),
                        DNode("graph", "Microsoft Graph", 0.78f, 0.35f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("retry", "429: Retry-After", 0.5f, 0.8f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "wait, then try again"),
                    ),
                    edges = listOf(
                        DEdge("graph", "app", "429"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("app", "graph")),
                    Beat(32, show = setOf("retry"), light = setOf("retry"), pulse = "graph" to "app", card = BoardCard("Throttled", "Honour Retry-After. The Graph SDKs retry for you.")),
                ),
            ),
            // 33: "Anything else people get wrong?"
            Scene(
                33, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Microsoft Graph wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("tok", "Your own API's token", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("beta", "Beta in production", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("next", "Ignoring the next link", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("hdr", "Count with no header", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("tok", "beta", "next", "hdr"), light = setOf("beta"), card = BoardCard("Real apps", "Use version 1.0. Beta is a preview and can change under you.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's Graph questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("part", "Only part of a list", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "follow next link"),
                        DNode("many", "Many calls, few trips", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "batch, 20 each"),
                        DNode("count", "Count or search", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "ConsistencyLevel"),
                        DNode("429", "A 429 answer", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "wait Retry-After"),
                        DNode("aud", "Which token?", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "audience is Graph, v1.0"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("part", "many", "count", "429"), card = BoardCard("Listen for the phrase", "Thirty calls means two batches. Each phrase points to one answer.")),
                    Beat(38, show = setOf("aud"), light = setOf("aud"), card = BoardCard("The token", "Its audience must be Graph. Production uses version 1.0, not beta.")),
                    Beat(40, card = BoardCard("Today", "Graph Explorer: get me, top three messages, users with a count and header.")),
                ),
            ),
        ),
    )

    private fun keyVault() = PodcastBoard(
        "az-keyvault",
        listOf(
            // 0: "Hi, and welcome back. Today is about keeping secrets safe, things like..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "An app reads a secret from the vault, but to get in it seems to need a secret of its own.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.78f, 0.42f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("cred", "Its own password?", 0.2f, 0.8f, w = 0.34f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("app", "vault", "read a secret"),
                        DEdge("cred", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "vault"), pulse = "app" to "vault"),
                    Beat(1, show = setOf("cred"), light = setOf("cred"), card = BoardCard("In a settings file?", "Then the file is what you must protect. It only moves the problem.")),
                ),
            ),
            // 2: "Exactly. And the surprising answer is, nowhere. The app doesn't keep o..."
            Scene(
                2, "What the vault holds",
                DiagramSpec(
                    "Key Vault holds secrets, keys and certificates; keys never leave it, so data goes in to be encrypted.",
                    nodes = listOf(
                        DNode("vault", "Key Vault", 0.5f, 0.2f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("secrets", "Secrets", 0.17f, 0.62f, w = 0.3f, tone = 1, sub = "readable, versioned"),
                        DNode("keys", "Keys", 0.5f, 0.62f, w = 0.3f, tone = 1, sub = "never leave"),
                        DNode("certs", "Certificates", 0.83f, 0.62f, w = 0.3f, tone = 1, sub = "the padlock"),
                        DNode("data", "Your data", 0.2f, 0.9f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("tier", "Premium: HSM-backed", 0.72f, 0.9f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("vault", "secrets"),
                        DEdge("vault", "keys"),
                        DEdge("vault", "certs"),
                        DEdge("data", "keys", "encrypt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("vault")),
                    Beat(4, light = setOf("vault"), card = BoardCard("Key Vault", "A safe in the cloud. Example: your recipe app's database password.")),
                    Beat(6, show = setOf("secrets", "keys"), light = setOf("secrets", "keys"), card = BoardCard("Secrets and keys", "A secret is hidden text you can read back. A key scrambles data.")),
                    Beat(8, show = setOf("data"), light = setOf("keys", "data"), pulse = "data" to "keys", card = BoardCard("Keys never leave", "A notes app sends notes in; the scrambling happens inside the vault.")),
                    Beat(10, show = setOf("certs"), light = setOf("certs"), card = BoardCard("Certificates", "The files behind the browser padlock. Some renew themselves.")),
                    Beat(12, show = setOf("tier"), light = setOf("keys", "tier"), card = BoardCard("Hardware-protected keys?", "Premium tier: HSM-backed. A hobby app is fine on Standard.")),
                ),
            ),
            // 13: "Okay. So who's allowed to open the safe?"
            Scene(
                13, "Who may open it",
                DiagramSpec(
                    "Give the app the smallest role it needs on the vault: Secrets User to read, not Administrator.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("user", "Secrets User", 0.3f, 0.78f, w = 0.36f, kind = NodeKind.PILL),
                        DNode("admin", "Administrator", 0.74f, 0.78f, w = 0.36f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "vault", "a role"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("app", "vault")),
                    Beat(14, show = setOf("user", "admin"), light = setOf("user", "app"), pulse = "app" to "vault", card = BoardCard("A role is a named permission", "Only reads secrets? Key Vault Secrets User, not Administrator.")),
                ),
            ),
            // 15: "And if someone deletes a secret by mistake?"
            Scene(
                15, "Deleted by mistake",
                DiagramSpec(
                    "Soft delete keeps a deleted secret recoverable; purge protection stops anyone erasing it for good in that window.",
                    nodes = listOf(
                        DNode("secret", "A secret", 0.2f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("soft", "Soft delete", 0.78f, 0.3f, w = 0.36f, h = 0.2f, tone = 1, sub = "a recycle bin"),
                        DNode("purge", "Purge protection", 0.78f, 0.78f, w = 0.4f, h = 0.2f, sub = "the bin is locked"),
                    ),
                    edges = listOf(
                        DEdge("secret", "soft", "deleted"),
                        DEdge("soft", "purge", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("secret")),
                    Beat(16, show = setOf("soft", "purge"), light = setOf("purge"), pulse = "secret" to "soft", card = BoardCard("Purge protection", "Locks the recycle bin: nobody can delete a secret for good too early.")),
                ),
            ),
            // 17: "Right. Back to the puzzle, then. How does the app open the safe with n..."
            Scene(
                17, "A badge, not a password",
                DiagramSpec(
                    "Azure makes the app a badge, its managed identity; the badge holds a role on the vault, and no password is stored.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "the app's badge"),
                        DNode("vault", "Key Vault", 0.82f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "id", "wears"),
                        DEdge("id", "vault", "Secrets User"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("app", "vault")),
                    Beat(18, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("Managed identity", "A badge Azure makes for your app. No password to store, leak or change.")),
                ),
            ),
            // 20: "And badges come in two kinds. A system-assigned identity belongs to on..."
            Scene(
                20, "Two kinds of badge",
                DiagramSpec(
                    "A system-assigned identity belongs to one app; a user-assigned one is made on its own and shared by many.",
                    nodes = listOf(
                        DNode("sys", "System-assigned", 0.24f, 0.2f, w = 0.42f, kind = NodeKind.PILL),
                        DNode("one", "Recipe app", 0.24f, 0.62f, w = 0.32f, h = 0.18f, tone = 1),
                        DNode("usr", "User-assigned", 0.76f, 0.2f, w = 0.42f, kind = NodeKind.PILL),
                        DNode("a1", "Website", 0.6f, 0.62f, w = 0.22f, tone = 1),
                        DNode("a2", "Mobile API", 0.88f, 0.62f, w = 0.24f, tone = 1),
                        DNode("a3", "Report job", 0.74f, 0.9f, h = 0.14f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("sys", "one"),
                        DEdge("usr", "a1"),
                        DEdge("usr", "a2"),
                        DEdge("usr", "a3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("sys", "one"), light = setOf("sys", "one"), card = BoardCard("System-assigned", "One click. Belongs to one app, like your recipe app, and goes with it.")),
                    Beat(22, show = setOf("usr"), light = setOf("usr"), card = BoardCard("User-assigned", "Made on its own, first. Give it a role once, hand it to many apps.")),
                    Beat(24, show = setOf("a1", "a2", "a3"), light = setOf("usr", "a1", "a2", "a3"), card = BoardCard("A shop's three apps, one badge", "Website, mobile backend and report job all share one user-assigned badge.")),
                ),
            ),
            // 25: "What does the code look like? Is it hard?"
            Scene(
                25, "The same code everywhere",
                DiagramSpec(
                    "DefaultAzureCredential uses your az login on your laptop and the managed identity in Azure.",
                    nodes = listOf(
                        DNode("dev", "Your laptop", 0.18f, 0.18f, w = 0.34f, h = 0.18f, tone = 1, sub = "after az login"),
                        DNode("azure", "In Azure", 0.18f, 0.76f, w = 0.34f, h = 0.18f, tone = 1, sub = "managed identity"),
                        DNode("dac", "DefaultAzure Credential", 0.48f, 0.47f, w = 0.32f, h = 0.22f),
                        DNode("vault", "Key Vault", 0.87f, 0.47f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("dev", "dac"),
                        DEdge("azure", "dac"),
                        DEdge("dac", "vault"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("dac", "vault")),
                    Beat(26, show = setOf("dev", "azure"), light = setOf("dac"), pulse = "dac" to "vault", card = BoardCard("C#", "new SecretClient(vaultUri, new DefaultAzureCredential())", code = true)),
                ),
            ),
            // 27: "And if I don't want to write any code at all?"
            Scene(
                27, "No code at all",
                DiagramSpec(
                    "A Key Vault reference in an app setting is fetched when the app starts, with the app's managed identity, which still needs its role.",
                    nodes = listOf(
                        DNode("setting", "App setting", 0.22f, 0.22f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Key Vault reference"),
                        DNode("app", "Your app", 0.22f, 0.72f, w = 0.34f, h = 0.2f),
                        DNode("id", "Managed identity", 0.6f, 0.72f, w = 0.36f, kind = NodeKind.PILL),
                        DNode("vault", "Key Vault", 0.82f, 0.24f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("setting", "app", "start-up"),
                        DEdge("id", "vault", "Secrets User"),
                        DEdge("vault", "setting", "fetched", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("app")),
                    Beat(28, show = setOf("setting", "vault"), light = setOf("setting"), pulse = "vault" to "setting", card = BoardCard("App setting", "@Microsoft.KeyVault(VaultName=v;SecretName=s)", code = true)),
                    Beat(30, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("It still needs the role", "The reference saves you the code, not the permission.")),
                ),
            ),
            // 31: "Okay. What do people usually get wrong?"
            Scene(
                31, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Key Vault access wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("admin", "Administrator for a reader", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("role", "A badge with no role: 403", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("pin", "A pinned old version", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("gone", "Deleting a shared badge", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("admin", "role", "pin", "gone"), card = BoardCard("Updates never arriving?", "Leave the version off the reference to get the latest.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Key Vault questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("hsm", "Hardware keys", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Premium tier"),
                        DNode("purge", "No delete for good", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "purge protection"),
                        DNode("user", "Shared or made first", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "user-assigned"),
                        DNode("read", "Only reads secrets", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Secrets User"),
                        DNode("none", "No credentials in code", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "managed identity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("hsm", "purge", "user", "read"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("none"), light = setOf("none"), card = BoardCard("No credentials in code?", "Managed identity, with DefaultAzureCredential or a Key Vault reference.")),
                    Beat(38, card = BoardCard("Today", "A vault with roles, a secret, Secrets User; then a reference with a green tick.")),
                ),
            ),
        ),
    )

    private fun appConfig() = PodcastBoard(
        "az-appconfig",
        listOf(
            // 0: "Hi, and welcome back. Today is Azure App Configuration. Here's a puzzl..."
            Scene(
                0, "The puzzle",
                DiagramSpec(
                    "A setting changes in the store, and the running app must notice without a restart.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("store", "App Configuration", 0.76f, 0.42f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("q", "Watch how many keys?", 0.24f, 0.82f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("store", "app", "settings"),
                        DEdge("q", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "store"), pulse = "store" to "app"),
                    Beat(1, show = setOf("q"), light = setOf("q"), card = BoardCard("Check them all?", "That's a lot of checking. The real answer is just one key.")),
                ),
            ),
            // 2: "It would be. The answer is just one, and we'll see why. First, what Ap..."
            Scene(
                2, "A shared notice board",
                DiagramSpec(
                    "App Configuration is one central store of non-secret settings that every app reads.",
                    nodes = listOf(
                        DNode("store", "App Configuration", 0.5f, 0.17f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("kv", "Key = value", 0.5f, 0.47f, w = 0.4f, h = 0.12f, kind = NodeKind.PILL, tone = 1),
                        DNode("web", "Website", 0.17f, 0.8f, w = 0.3f, tone = 1),
                        DNode("mob", "Mobile API", 0.5f, 0.8f, w = 0.3f, tone = 1),
                        DNode("ord", "Orders", 0.83f, 0.8f, w = 0.3f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("kv", "web"),
                        DEdge("kv", "mob"),
                        DEdge("kv", "ord"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("store")),
                    Beat(4, show = setOf("kv"), light = setOf("store", "kv"), card = BoardCard("App Configuration", "A shared notice board for settings. Change once, every app sees it.")),
                    Beat(6, show = setOf("web", "mob", "ord"), light = setOf("web", "mob", "ord"), pulse = "kv" to "mob", card = BoardCard("A small online shop", "Website, mobile backend and order service share one daily order limit.")),
                ),
            ),
            // 7: "Can I keep passwords there too?"
            Scene(
                7, "No secrets on the board",
                DiagramSpec(
                    "Secrets stay in Key Vault; the store only holds a Key Vault reference that points to them.",
                    nodes = listOf(
                        DNode("store", "App Configuration", 0.26f, 0.25f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("ref", "Key Vault reference", 0.26f, 0.75f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pointer"),
                        DNode("vault", "Key Vault", 0.8f, 0.75f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("store", "ref"),
                        DEdge("ref", "vault", "points to", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("store")),
                    Beat(8, show = setOf("ref", "vault"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Secrets go in Key Vault", "Store a Key Vault reference, never the password as a plain value.")),
                ),
            ),
            // 9: "What if my test app and my live app need different values?"
            Scene(
                9, "One key, two labels",
                DiagramSpec(
                    "A label is a variant of one key per environment; each app loads its own label with a label filter.",
                    nodes = listOf(
                        DNode("key", "DailyLimit", 0.5f, 0.13f, w = 0.4f),
                        DNode("dev", "label dev: 50", 0.24f, 0.45f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("prod", "label prod: 15", 0.76f, 0.45f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("devapp", "Test app", 0.24f, 0.72f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("liveapp", "Live app", 0.76f, 0.72f, w = 0.3f, h = 0.14f),
                        DNode("snap", "Snapshot: frozen set", 0.5f, 0.93f, w = 0.56f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("key", "dev"),
                        DEdge("key", "prod"),
                        DEdge("dev", "devapp"),
                        DEdge("prod", "liveapp"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("key")),
                    Beat(10, show = setOf("dev", "prod"), light = setOf("dev", "prod"), card = BoardCard("Label", "Same key, different value per environment: dev 50, prod 15.")),
                    Beat(12, show = setOf("devapp", "liveapp", "snap"), light = setOf("liveapp"), pulse = "prod" to "liveapp", card = BoardCard("Label filter and snapshot", "Each app loads its own label. A snapshot freezes keys for a release.")),
                ),
            ),
            // 17: "Okay. Back to the puzzle. Why only one key?"
            Scene(
                17, "One key on watch",
                DiagramSpec(
                    "The app watches one sentinel key; when it changes, the middleware reloads every other key.",
                    nodes = listOf(
                        DNode("sent", "Sentinel key", 0.26f, 0.18f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "the guard"),
                        DNode("others", "All other keys", 0.26f, 0.62f, w = 0.42f, h = 0.2f, tone = 2),
                        DNode("mw", "Middleware", 0.78f, 0.18f, w = 0.36f, h = 0.18f, sub = "on each request"),
                        DNode("app", "Your app", 0.78f, 0.62f, w = 0.36f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("mw", "sent", "watches"),
                        DEdge("others", "app", "reloaded"),
                        DEdge("mw", "app"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("sent")),
                    Beat(18, show = setOf("others", "app"), light = setOf("sent"), pulse = "others" to "app", card = BoardCard("Sentinel key", "Bump one key and the app reloads all the rest. One request, not hundreds.")),
                    Beat(20, show = setOf("mw"), light = setOf("mw"), pulse = "mw" to "sent", card = BoardCard("No middleware, no refresh", "The check runs on the next request after the refresh interval.")),
                    Beat(22, light = setOf("app"), card = BoardCard("Don't register every key", "Register the sentinel and refresh all. App code reads values as usual.")),
                ),
            ),
            // 23: "Nice. Now, feature flags. What's a flag?"
            Scene(
                23, "Feature flags",
                DiagramSpec(
                    "A feature flag is an on-off switch in the store that turns part of the app on with no new release.",
                    nodes = listOf(
                        DNode("flag", "Feature flag", 0.26f, 0.3f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "DarkMode"),
                        DNode("app", "To-do app", 0.78f, 0.3f, w = 0.34f, h = 0.22f, tone = 1),
                        DNode("off", "Broken? Switch it off", 0.5f, 0.8f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("flag", "app", "on or off"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("flag")),
                    Beat(24, show = setOf("app", "off"), light = setOf("flag", "app"), pulse = "flag" to "app", card = BoardCard("A to-do app's dark mode", "Ship it with the flag off, switch it on when ready, off if it breaks.")),
                ),
            ),
            // 25: "Can a flag be on for some people and off for others?"
            Scene(
                25, "Feature filters",
                DiagramSpec(
                    "A feature filter decides a flag's answer while the app runs: by percentage, time window or targeting.",
                    nodes = listOf(
                        DNode("flag", "Feature flag", 0.5f, 0.14f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("pct", "Percentage", 0.17f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "10% of users"),
                        DNode("time", "Time window", 0.5f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "one weekend"),
                        DNode("tgt", "Targeting", 0.83f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "beta group"),
                        DNode("custom", "or a custom filter", 0.5f, 0.88f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("flag", "pct"),
                        DEdge("flag", "time"),
                        DEdge("flag", "tgt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("flag")),
                    Beat(26, show = setOf("pct", "time", "tgt", "custom"), card = BoardCard("Feature filter", "A rule that decides the flag's answer at runtime.")),
                    Beat(28, light = setOf("pct", "time", "tgt"), pulse = "flag" to "pct", card = BoardCard("A recipe app's new search", "Ten percent first, a weekend banner, or the beta testers first.")),
                ),
            ),
            // 29: "And how does my code check a flag?"
            Scene(
                29, "Code and access",
                DiagramSpec(
                    "Code asks the feature manager about a flag; the app reads the store with its managed identity.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.25f, w = 0.3f, h = 0.2f),
                        DNode("fm", "Feature manager", 0.2f, 0.75f, w = 0.34f, h = 0.2f, tone = 1),
                        DNode("id", "Managed identity", 0.62f, 0.75f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "Data Reader"),
                        DNode("store", "App Configuration", 0.74f, 0.25f, w = 0.4f, h = 0.28f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "fm", "is it on?"),
                        DEdge("id", "store", "reads"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("app", "store")),
                    Beat(30, show = setOf("fm"), light = setOf("fm"), pulse = "app" to "fm", card = BoardCard("C#", "await featureManager.IsEnabledAsync(\"DarkMode\")", code = true)),
                    Beat(32, show = setOf("id"), light = setOf("id"), pulse = "id" to "store", card = BoardCard("No connection string", "Managed identity with App Configuration Data Reader. Free tier for trying.")),
                ),
            ),
            // 33: "Identity again. So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's App Configuration questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("restart", "No restart", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "sentinel key"),
                        DNode("env", "Value per environment", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "label"),
                        DNode("secret", "A secret value", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Key Vault reference"),
                        DNode("share", "A share of users", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "percentage filter"),
                        DNode("more", "Dates or named users", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "time window, targeting"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("restart", "env", "secret"), card = BoardCard("Listen for the phrase", "No restart: sentinel. Per environment: label. Secret: a reference.")),
                    Beat(36, show = setOf("share", "more"), light = setOf("share"), card = BoardCard("A fifth of users?", "A percentage filter. Not a label, and not a sentinel key.")),
                    Beat(38, card = BoardCard("Today", "Free store, two labelled values, a flag, a console app, a sentinel refresh.")),
                ),
            ),
        ),
    )

    private fun blobBasics() = PodcastBoard(
        "az-blob-basics",
        listOf(
            // 0: "Hi, and welcome back. Today is Blob Storage. Here's a puzzle to start...."
            Scene(
                0, "The puzzle",
                DiagramSpec(
                    "Your app asks to read a file that sits in the Archive tier, the cheapest storage there is.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("arch", "Archive", 0.78f, 0.42f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("q", "Read it?", 0.22f, 0.82f, w = 0.32f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("app", "arch", "read"),
                        DEdge("q", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "arch"), pulse = "app" to "arch"),
                    Beat(1, show = setOf("q"), light = setOf("q"), card = BoardCard("Read slowly?", "Most people guess so. The real answer comes with the tiers.")),
                ),
            ),
            // 2: "That's what most people guess. The real answer is a surprise, and we'l..."
            Scene(
                2, "Account, container, blob",
                DiagramSpec(
                    "A storage account holds containers, a container holds blobs, and a blob is a file.",
                    nodes = listOf(
                        DNode("acct", "Storage account", 0.5f, 0.16f, w = 0.46f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "the building"),
                        DNode("c1", "photos", 0.25f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("c2", "backups", 0.75f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("b1", "cat.jpg", 0.25f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("b2", "monday.bak", 0.75f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("acct", "c1"),
                        DEdge("acct", "c2"),
                        DEdge("c1", "b1"),
                        DEdge("c2", "b2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("acct")),
                    Beat(4, light = setOf("acct"), card = BoardCard("Blob Storage", "Files of any size in the cloud. Pay for storing, and for touching.")),
                    Beat(6, show = setOf("c1", "b1"), light = setOf("c1"), pulse = "acct" to "c1", card = BoardCard("Container", "A room in the account: a folder-like group with its own access level.")),
                    Beat(8, show = setOf("c2", "b2"), light = setOf("c1", "c2"), card = BoardCard("A photo-sharing app", "One account, two containers: photos and backups.")),
                ),
            ),
            // 9: "Are all blobs the same kind?"
            Scene(
                9, "Three kinds of blob",
                DiagramSpec(
                    "Block blobs for whole files, append blobs for add-only logs, page blobs for disks.",
                    nodes = listOf(
                        DNode("block", "Block blob", 0.5f, 0.15f, w = 0.6f, h = 0.2f, sub = "the default: files"),
                        DNode("append", "Append blob", 0.5f, 0.48f, w = 0.6f, h = 0.2f, tone = 1, sub = "logs, add to the end"),
                        DNode("page", "Page blob", 0.5f, 0.81f, w = 0.6f, h = 0.2f, tone = 2, sub = "virtual machine disks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("block", "append", "page"), light = setOf("block")),
                    Beat(11, light = setOf("append"), card = BoardCard("A log line every second?", "Append blob. You can add to the end but never edit old lines.")),
                ),
            ),
            // 12: "Append blob. Now, copies. Azure always keeps several copies of your da..."
            Scene(
                12, "How many copies, how far",
                DiagramSpec(
                    "LRS keeps copies in one building, ZRS across zones in a region, GRS in a second region too.",
                    nodes = listOf(
                        DNode("lrs", "LRS", 0.17f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "one building"),
                        DNode("zrs", "ZRS", 0.5f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "across zones"),
                        DNode("grs", "GRS", 0.83f, 0.2f, w = 0.3f, h = 0.2f, sub = "second region"),
                        DNode("ra", "RA-GRS", 0.83f, 0.62f, w = 0.3f, h = 0.2f, sub = "read it any time"),
                        DNode("gz", "GZRS: zones and geo", 0.33f, 0.62f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("lrs", "zrs"),
                        DEdge("zrs", "grs"),
                        DEdge("grs", "ra"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("lrs", "zrs", "grs")),
                    Beat(14, light = setOf("lrs", "zrs"), pulse = "lrs" to "zrs", card = BoardCard("LRS and ZRS", "LRS: three copies, one datacenter. ZRS: spread over separate buildings.")),
                    Beat(16, show = setOf("ra", "gz"), light = setOf("grs", "ra"), pulse = "grs" to "ra", card = BoardCard("GRS and RA-GRS", "GRS: a far-away region, read after failover. RA-GRS: read any time.")),
                    Beat(18, card = BoardCard("Which one?", "Hobby test files: LRS. A shop surviving a building: ZRS. A region: GRS.")),
                ),
            ),
            // 19: "Now, hot and cold. I keep hearing those words."
            Scene(
                19, "Access tiers",
                DiagramSpec(
                    "Each access tier trades the price of keeping a file against the price of reading it.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.26f, 0.2f, w = 0.42f, h = 0.22f, sub = "daily use"),
                        DNode("cool", "Cool", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "30 days minimum"),
                        DNode("cold", "Cold", 0.26f, 0.62f, w = 0.42f, h = 0.22f, tone = 1, sub = "90 days minimum"),
                        DNode("arch", "Archive", 0.76f, 0.62f, w = 0.42f, h = 0.22f, tone = 2, sub = "180 days, offline"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("hot", "cool", "cold"), light = setOf("hot"), card = BoardCard("Access tier", "Pay to keep or pay to read, never cheaply for both.")),
                    Beat(24, show = setOf("arch"), light = setOf("arch"), card = BoardCard("Archive is offline", "Not slow: you cannot read it at all. That's the puzzle's answer.")),
                ),
            ),
            // 25: "Then how do I get it back?"
            Scene(
                25, "Rehydrate first",
                DiagramSpec(
                    "An Archive blob must be rehydrated to Hot or Cool, which can take hours, before anything reads it.",
                    nodes = listOf(
                        DNode("arch", "Archive", 0.2f, 0.35f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("hot", "Hot or Cool", 0.8f, 0.35f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("time", "Up to 15 hours", 0.5f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "standard priority"),
                    ),
                    edges = listOf(
                        DEdge("arch", "hot", "rehydrate"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("arch")),
                    Beat(26, show = setOf("hot", "time"), light = setOf("time"), pulse = "arch" to "hot", card = BoardCard("Rehydrate", "Move it back to Hot or Cool first, like adding water to dried food.")),
                ),
            ),
            // 27: "Fifteen hours. So when should I use which tier?"
            Scene(
                27, "Files moving by age",
                DiagramSpec(
                    "A lifecycle rule moves a nightly report from Hot to Cool to Archive, then deletes it, with nobody involved.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.17f, 0.2f, w = 0.28f, h = 0.18f, sub = "this month"),
                        DNode("cool", "Cool", 0.5f, 0.2f, w = 0.28f, h = 0.18f, tone = 1, sub = "after 30 days"),
                        DNode("arch", "Archive", 0.83f, 0.2f, w = 0.28f, h = 0.18f, tone = 2, sub = "after a year"),
                        DNode("gone", "Deleted", 0.83f, 0.58f, w = 0.28f, kind = NodeKind.PILL, tone = 2),
                        DNode("rule", "Lifecycle rule", 0.3f, 0.75f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "runs once a day"),
                    ),
                    edges = listOf(
                        DEdge("hot", "cool"),
                        DEdge("cool", "arch"),
                        DEdge("arch", "gone", "6 years"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("hot", "cool", "arch"), light = setOf("hot", "cool", "arch"), card = BoardCard("A small study app", "Nightly reports: Hot this month, Cool for a year, then Archive.")),
                    Beat(30, show = setOf("gone", "rule"), light = setOf("rule"), pulse = "arch" to "gone", card = BoardCard("Lifecycle rule", "A policy that moves or deletes blobs by age. Nobody does it by hand.")),
                ),
            ),
            // 31: "Nice. What do people get wrong?"
            Scene(
                31, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Blob Storage wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("read", "Reading Archive directly", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("fee", "Cool, deleted in a week", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("grs", "GRS, not RA-GRS", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("v1", "An old style account", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("read", "fee", "grs", "v1"), light = setOf("v1"), card = BoardCard("Where did the tiers go?", "Tiers need a StorageV2 account. Cool has an early deletion fee.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Blob Storage questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("arch", "Reading Archive", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "rehydrate, hours"),
                        DNode("region", "Lose a region", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "GRS"),
                        DNode("ra", "Read without failover", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "RA-GRS"),
                        DNode("zone", "Cheapest zone-safe", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "ZRS"),
                        DNode("files", "Logs, by age, monthly", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "append, lifecycle, Cool"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("arch", "region", "ra", "zone"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("files"), light = setOf("files"), card = BoardCard("The files themselves", "Add-only logs: append blob. By age: lifecycle rule. Tiers: StorageV2.")),
                    Beat(38, card = BoardCard("Today", "StorageV2 with LRS, a private container, Cool tier, a lifecycle rule.")),
                ),
            ),
        ),
    )

    private fun blobSdk() = PodcastBoard(
        "az-blob-sdk",
        listOf(
            // 0: "Hi, and welcome back. Last time we met Blob Storage, a place in the cl..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A friend needs one private photo for ten minutes, without being handed the key to everything.",
                    nodes = listOf(
                        DNode("friend", "A friend", 0.18f, 0.4f, w = 0.24f, h = 0.26f, kind = NodeKind.USER),
                        DNode("photo", "Private photo", 0.78f, 0.4f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("key", "The storage password?", 0.5f, 0.82f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("friend", "photo", "ten minutes"),
                        DEdge("key", "friend", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("friend", "photo"), pulse = "friend" to "photo"),
                    Beat(1, show = setOf("key"), light = setOf("key"), card = BoardCard("Hand over the password?", "Then they see everything, forever. We need something smaller.")),
                ),
            ),
            // 2: "Exactly. We'll solve it properly in a bit. First, the code itself. Azu..."
            Scene(
                2, "Building, room, box",
                DiagramSpec(
                    "The SDK has three client objects: the account, one container in it, and one blob in that.",
                    nodes = listOf(
                        DNode("svc", "Service client", 0.18f, 0.42f, w = 0.32f, h = 0.26f, tone = 1, sub = "the building"),
                        DNode("cont", "Container client", 0.52f, 0.42f, w = 0.32f, h = 0.26f, sub = "one room"),
                        DNode("blob", "Blob client", 0.85f, 0.42f, w = 0.28f, h = 0.26f, tone = 2, sub = "one box"),
                        DNode("sdk", "SDK: a ready-made library", 0.5f, 0.84f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("svc", "cont"),
                        DEdge("cont", "blob"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("sdk"), light = setOf("sdk")),
                    Beat(4, show = setOf("svc", "cont", "blob"), light = setOf("svc", "cont", "blob"), pulse = "cont" to "blob", card = BoardCard("Three clients", "Account, then container, then blob. Start at the building, walk in.")),
                ),
            ),
            // 5: "And how does the code get through the front door? With a key?"
            Scene(
                5, "Through the front door",
                DiagramSpec(
                    "DefaultAzureCredential uses your az login on a laptop and the managed identity in Azure.",
                    nodes = listOf(
                        DNode("dev", "Your laptop", 0.18f, 0.18f, w = 0.34f, h = 0.18f, tone = 1, sub = "after az login"),
                        DNode("azure", "In Azure", 0.18f, 0.76f, w = 0.34f, h = 0.18f, tone = 1, sub = "managed identity"),
                        DNode("dac", "DefaultAzure Credential", 0.5f, 0.47f, w = 0.32f, h = 0.22f),
                        DNode("store", "Storage", 0.86f, 0.47f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("dev", "dac"),
                        DEdge("azure", "dac"),
                        DEdge("dac", "store", "a role"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("dac", "store")),
                    Beat(6, show = setOf("dev", "azure"), light = setOf("dac"), pulse = "dac" to "store", card = BoardCard("C#", "new BlobServiceClient(uri, new DefaultAzureCredential())", code = true)),
                ),
            ),
            // 8: "It's the theme of the whole security side. Now, information about a fi..."
            Scene(
                8, "Labels on a file",
                DiagramSpec(
                    "Metadata travels with one blob and cannot be searched; index tags can be searched across the account.",
                    nodes = listOf(
                        DNode("blob", "A photo", 0.2f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("props", "System properties", 0.7f, 0.2f, w = 0.46f, h = 0.2f, tone = 2, sub = "type, last changed"),
                        DNode("meta", "Metadata", 0.2f, 0.55f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE, sub = "a luggage tag"),
                        DNode("tags", "Index tags", 0.7f, 0.55f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a catalogue"),
                        DNode("query", "Every file where owner is Usaid", 0.5f, 0.87f, w = 0.7f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("blob", "props"),
                        DEdge("blob", "meta"),
                        DEdge("query", "tags", "search"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("blob", "props", "meta"), light = setOf("props")),
                    Beat(10, light = setOf("meta"), pulse = "blob" to "meta", card = BoardCard("Metadata", "A photo-sharing app stores who uploaded each picture. Up to 8 KB.")),
                    Beat(11, show = setOf("query")),
                    Beat(12, show = setOf("tags"), light = setOf("tags"), pulse = "query" to "tags", card = BoardCard("Index tags", "Searchable across the whole account. Metadata is not.")),
                ),
            ),
            // 14: "Now, the puzzle. The answer is a SAS, a shared access signature. In pl..."
            Scene(
                14, "A link with limits",
                DiagramSpec(
                    "Three kinds of SAS; only the user delegation SAS is signed without the account key.",
                    nodes = listOf(
                        DNode("sas", "SAS link", 0.5f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "a hotel key card"),
                        DNode("ud", "User delegation", 0.18f, 0.52f, w = 0.34f, h = 0.22f, sub = "Entra ID key"),
                        DNode("svc", "Service SAS", 0.52f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("acct", "Account SAS", 0.84f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("pol", "Stored access policy", 0.6f, 0.87f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("sas", "ud"),
                        DEdge("sas", "svc"),
                        DEdge("sas", "acct"),
                        DEdge("pol", "svc", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("sas"), light = setOf("sas"), card = BoardCard("SAS: shared access signature", "A link with limits: one door, only read, stops at checkout time.")),
                    Beat(16, show = setOf("ud", "svc", "acct"), light = setOf("ud")),
                    Beat(20, light = setOf("ud"), card = BoardCard("Prefer user delegation", "Cancel it by revoking the user delegation key. The master key stays put.")),
                    Beat(22, show = setOf("pol"), light = setOf("pol", "svc"), pulse = "pol" to "svc", card = BoardCard("Stored access policy", "Cancels a service SAS without changing the account key.")),
                ),
            ),
            // 23: "So walk me through a real one."
            Scene(
                23, "A thirty-minute link",
                DiagramSpec(
                    "The API checks the user, returns a short read-only SAS link, and the phone downloads straight from storage.",
                    nodes = listOf(
                        DNode("phone", "Phone", 0.16f, 0.42f, w = 0.22f, h = 0.26f, kind = NodeKind.USER),
                        DNode("api", "Your API", 0.52f, 0.16f, w = 0.3f, h = 0.2f, sub = "checks the user"),
                        DNode("store", "Storage", 0.84f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("key", "Account key in the app", 0.4f, 0.86f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "never"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "ask"),
                        DEdge("phone", "store", "download"),
                        DEdge("api", "store", "uploads", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("phone", "api", "store")),
                    Beat(24, light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Say you're building a photo app", "API checks the user, returns a read-only SAS for 30 minutes.")),
                    Beat(26, light = setOf("store"), pulse = "phone" to "store", card = BoardCard("After 30 minutes: 403", "Access denied. The app asks the API for a fresh link.")),
                    Beat(28, show = setOf("key"), light = setOf("key"), pulse = "api" to "store", card = BoardCard("Uploads from a phone", "Through the API, or a short SAS. Never the key in the app.")),
                ),
            ),
            // 29: "Okay. Last piece. Leases."
            Scene(
                29, "One writer at a time",
                DiagramSpec(
                    "A lease locks a blob for writes, so several servers updating one file take turns.",
                    nodes = listOf(
                        DNode("s1", "Server 1", 0.16f, 0.18f, tone = 1),
                        DNode("s2", "Server 2", 0.16f, 0.48f, tone = 1),
                        DNode("s3", "Server 3", 0.16f, 0.78f, tone = 1),
                        DNode("file", "High score file", 0.74f, 0.48f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("lease", "Lease", 0.74f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "15 to 60 seconds"),
                    ),
                    edges = listOf(
                        DEdge("s1", "file", "holds the lease"),
                        DEdge("s2", "file", dashed = true),
                        DEdge("s3", "file", dashed = true),
                        DEdge("lease", "file"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("file")),
                    Beat(30, show = setOf("lease"), light = setOf("lease"), card = BoardCard("Lease", "An engaged sign on the door: one writer at a time.")),
                    Beat(32, show = setOf("s1", "s2", "s3"), light = setOf("s1"), pulse = "s1" to "file", card = BoardCard("Picture a small game", "Three servers, one high score file. With a lease, they take turns.")),
                    Beat(34, light = setOf("lease"), card = BoardCard("The catch", "A lease blocks your own writes too, unless you pass the lease id.")),
                ),
            ),
            // 35: "So what should I remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's blob code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("meta", "Uploader's name", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "metadata"),
                        DNode("tags", "Search by a key", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "index tags"),
                        DNode("sas", "Short private link", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "user delegation SAS"),
                        DNode("lease", "One writer", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a lease"),
                        DNode("none", "No keys in code", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "DefaultAzureCredential"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("meta", "tags", "sas", "lease"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, show = setOf("none"), light = setOf("none"), card = BoardCard("No keys in code?", "DefaultAzureCredential and a managed identity. Never a key in an app.")),
                    Beat(40, card = BoardCard("Today", "Upload a blob, set metadata, make a 10-minute SAS, open it twice.")),
                ),
            ),
        ),
    )

    private fun cosmosModel() = PodcastBoard(
        "az-cosmos-model",
        listOf(
            // 0: "Hi, and welcome back. Today we meet Cosmos DB, Azure's big database. H..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "A live chat app whose data was split up the wrong way: can it be fixed in place?",
                    nodes = listOf(
                        DNode("app", "Chat app", 0.2f, 0.4f, w = 0.3f, h = 0.2f),
                        DNode("db", "Chat messages", 0.78f, 0.4f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("fix", "Wrong split: change it?", 0.5f, 0.82f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "db", "live"),
                        DEdge("fix", "db", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "db"), pulse = "app" to "db"),
                    Beat(1, show = setOf("fix"), light = setOf("fix"), card = BoardCard("Just change a setting?", "No. We find out why when we get to partition keys.")),
                ),
            ),
            // 2: "It means no. We'll see why. First, what is Cosmos DB, in plain words? ..."
            Scene(
                2, "A filing cabinet",
                DiagramSpec(
                    "Account, database, container, item: a cabinet, a drawer, a folder and the cards inside.",
                    nodes = listOf(
                        DNode("acct", "Account", 0.5f, 0.1f, w = 0.44f, kind = NodeKind.CYLINDER, tone = 1, sub = "the cabinet"),
                        DNode("db", "Database", 0.5f, 0.36f, w = 0.44f, tone = 2, sub = "a drawer"),
                        DNode("cont", "Container", 0.5f, 0.62f, w = 0.44f, sub = "a folder"),
                        DNode("item", "Item: a JSON document", 0.5f, 0.88f, w = 0.6f, kind = NodeKind.NOTE, sub = "a contact card"),
                    ),
                    edges = listOf(
                        DEdge("acct", "db"),
                        DEdge("db", "cont"),
                        DEdge("cont", "item"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("JSON document", "A little text record, like a contact card: name, email, phone.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("cont"), pulse = "cont" to "item", card = BoardCard("Cabinet, drawer, folder, card", "Account, database, container, item.")),
                ),
            ),
            // 5: "Is there only one way to talk to it?"
            Scene(
                5, "Pick the API",
                DiagramSpec(
                    "The API is the language your code speaks to Cosmos, chosen once when the account is created.",
                    nodes = listOf(
                        DNode("acct", "Account", 0.5f, 0.12f, w = 0.4f, h = 0.18f, kind = NodeKind.CYLINDER),
                        DNode("nosql", "NoSQL", 0.17f, 0.5f, w = 0.3f, h = 0.22f, sub = "new apps, default"),
                        DNode("mongo", "MongoDB", 0.5f, 0.5f, w = 0.3f, h = 0.22f, tone = 1, sub = "existing Mongo"),
                        DNode("other", "Postgres and others", 0.83f, 0.5f, w = 0.3f, h = 0.22f, tone = 2, sub = "moving apps in"),
                        DNode("fixed", "Fixed at creation", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("acct", "nosql"),
                        DEdge("acct", "mongo"),
                        DEdge("acct", "other"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("acct")),
                    Beat(6, show = setOf("nosql", "mongo", "other"), light = setOf("nosql"), card = BoardCard("API", "The language your code speaks to the database. NoSQL is the default.")),
                    Beat(10, show = setOf("fixed"), light = setOf("mongo", "fixed"), pulse = "acct" to "mongo", card = BoardCard("Moving a MongoDB shop app?", "Minimal code changes: the MongoDB API. A brand-new to-do app: NoSQL.")),
                ),
            ),
            // 12: "There's another one coming. But first, consistency. Cosmos keeps copie..."
            Scene(
                12, "How fresh is a read",
                DiagramSpec(
                    "Five consistency levels, from Strong (freshest, costliest) to Eventual (cheapest, fastest).",
                    nodes = listOf(
                        DNode("towns", "Copies in many towns", 0.5f, 0.09f, w = 0.6f, h = 0.14f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("strong", "Strong", 0.5f, 0.27f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "always the latest; costs most"),
                        DNode("bounded", "Bounded staleness", 0.5f, 0.42f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "behind by a set amount"),
                        DNode("session", "Session", 0.5f, 0.57f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, sub = "your own writes; the default"),
                        DNode("prefix", "Consistent prefix", 0.5f, 0.72f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "never out of order"),
                        DNode("eventual", "Eventual", 0.5f, 0.87f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "catches up; cheapest"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, light = setOf("towns"), card = BoardCard("Consistency level", "The promise about how fresh a read is, across the copies (replicas).")),
                    Beat(14, show = setOf("strong", "bounded", "session"), light = setOf("strong")),
                    Beat(16, show = setOf("prefix", "eventual"), light = setOf("prefix", "eventual")),
                    Beat(18, light = setOf("session"), card = BoardCard("Session is the default", "A notes app: you see your own edit at once, others soon after.")),
                    Beat(19, light = setOf("prefix"), card = BoardCard("Late, never out of order", "A game leaderboard: consistent prefix. Order without Strong's cost.")),
                ),
            ),
            // 21: "What is that, in plain words?"
            Scene(
                21, "Shelves by one label",
                DiagramSpec(
                    "Items with the same partition key value share a logical partition; a query that names it reads one shelf.",
                    nodes = listOf(
                        DNode("q", "Query: Usaid's items", 0.5f, 0.1f, w = 0.6f, h = 0.14f, tone = 1),
                        DNode("p1", "owner: usaid", 0.17f, 0.48f, w = 0.3f, h = 0.28f, sub = "one shelf"),
                        DNode("p2", "owner: sanya", 0.5f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "untouched"),
                        DNode("p3", "more owners", 0.83f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "untouched"),
                        DNode("cap", "At most 20 GB a shelf", 0.5f, 0.86f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("q", "p1", "cheap"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("p1", "p2", "p3", "cap"), light = setOf("p1"), card = BoardCard("Partition key", "The shelf label. Same value, same shelf: a logical partition.")),
                    Beat(24, show = setOf("q"), light = setOf("q", "p1"), pulse = "q" to "p1", card = BoardCard("The puzzle answer", "The partition key is fixed when the container is created.")),
                ),
            ),
            // 25: "So how do I choose well?"
            Scene(
                25, "Choosing a key",
                DiagramSpec(
                    "A good key has many values, spreads writes evenly, and is in the query you run most.",
                    nodes = listOf(
                        DNode("owner", "Owner", 0.17f, 0.22f, w = 0.3f, h = 0.24f, sub = "per-user app"),
                        DNode("date", "Date", 0.5f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "today is hot"),
                        DNode("country", "Country", 0.83f, 0.22f, w = 0.3f, h = 0.24f, tone = 2, sub = "never"),
                        DNode("hot", "Hot partition", 0.83f, 0.6f, w = 0.3f, kind = NodeKind.NOTE),
                        DNode("chat", "Chat: conversation id", 0.33f, 0.84f, w = 0.6f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("country", "hot"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("owner"), light = setOf("owner"), card = BoardCard("Many, even, in the query", "A study app asked per user: the owner id is a great key.")),
                    Beat(28, show = setOf("date", "country", "hot"), light = setOf("hot"), pulse = "country" to "hot", card = BoardCard("Hot partition", "Few values, so one huge shelf takes all the writes.")),
                    Beat(30, show = setOf("chat"), light = setOf("chat"), card = BoardCard("Picture a chat app", "Asked per conversation? Key on conversation id, not message id.")),
                ),
            ),
            // 31: "Now, money. How do I pay for all this?"
            Scene(
                31, "Paying in tokens",
                DiagramSpec(
                    "Every action costs request units: about 1 RU for a 1 KB point read, more for queries and writes.",
                    nodes = listOf(
                        DNode("ru", "Request unit (RU)", 0.5f, 0.14f, w = 0.56f, h = 0.18f, kind = NodeKind.PILL, sub = "an arcade token"),
                        DNode("read", "Read by id", 0.17f, 0.6f, w = 0.3f, h = 0.24f, tone = 1, sub = "about 1 RU"),
                        DNode("query", "Query", 0.5f, 0.6f, w = 0.3f, h = 0.24f, tone = 1, sub = "more"),
                        DNode("write", "Write", 0.83f, 0.6f, w = 0.3f, h = 0.24f, tone = 1, sub = "about 5 a KB"),
                    ),
                    edges = listOf(
                        DEdge("ru", "read"),
                        DEdge("ru", "query"),
                        DEdge("ru", "write"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("ru")),
                    Beat(32, show = setOf("read", "query", "write"), light = setOf("ru", "read"), pulse = "ru" to "read", card = BoardCard("Request units", "Tokens at an arcade. A 1 KB read by id costs about one.")),
                ),
            ),
            // 33: "And how do I buy the tokens?"
            Scene(
                33, "Buying the tokens",
                DiagramSpec(
                    "Provisioned for steady load, autoscale for a ceiling, serverless to pay per use; a 429 means you ran out.",
                    nodes = listOf(
                        DNode("prov", "Provisioned", 0.17f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "steady, busy"),
                        DNode("auto", "Autoscale", 0.5f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "down to 10%"),
                        DNode("serv", "Serverless", 0.83f, 0.22f, w = 0.3f, h = 0.24f, sub = "pay per use"),
                        DNode("app", "Your app", 0.25f, 0.75f, w = 0.3f, h = 0.2f),
                        DNode("err", "429: slow down", 0.72f, 0.75f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "retry with backoff"),
                    ),
                    edges = listOf(
                        DEdge("err", "app", "too many"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("prov", "auto", "serv"), light = setOf("serv"), card = BoardCard("A small hobby app?", "Spiky traffic: serverless. Steady, busy traffic: provisioned.")),
                    Beat(36, show = setOf("app", "err"), light = setOf("err"), pulse = "err" to "app", card = BoardCard("429", "Out of RU. Retry after a short wait, or raise the limit.")),
                ),
            ),
            // 37: "So what should I remember for the exam?"
            Scene(
                37, "For the exam",
                DiagramSpec(
                    "The exam's Cosmos DB model questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("mongo", "Minimal Mongo changes", 0.26f, 0.1f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "MongoDB API"),
                        DNode("session", "Default consistency", 0.76f, 0.1f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Session"),
                        DNode("prefix", "Late, never out of order", 0.26f, 0.34f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "consistent prefix"),
                        DNode("serv", "Unpredictable traffic", 0.76f, 0.34f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "serverless, autoscale"),
                        DNode("err", "A 429", 0.5f, 0.58f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "retry, or add RU"),
                        DNode("key", "Partition key", 0.5f, 0.86f, w = 0.7f, h = 0.2f, sub = "many values, in the query, fixed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("mongo", "session", "prefix", "serv", "err"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, show = setOf("key"), light = setOf("key"), card = BoardCard("The one to remember", "Many values, spread evenly, in your common query. Never changes.")),
                    Beat(42, card = BoardCard("Today", "Free-tier account, container keyed on owner, two items, one query.")),
                ),
            ),
        ),
    )

    private fun cosmosSdk() = PodcastBoard(
        "az-cosmos-sdk",
        listOf(
            // 0: "Hi, and welcome to the very last episode. Today, Cosmos DB in code. He..."
            Scene(
                0, "The question",
                DiagramSpec(
                    "Two people save the same shopping list at once; without help, the second save wipes out the first.",
                    nodes = listOf(
                        DNode("u1", "Person 1", 0.16f, 0.22f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("u2", "Person 2", 0.16f, 0.68f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("list", "Shopping list", 0.74f, 0.42f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("lost", "First change lost?", 0.7f, 0.86f, w = 0.44f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("u1", "list", "save"),
                        DEdge("u2", "list", "save"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("u1", "u2", "list"), pulse = "u1" to "list"),
                    Beat(1, show = setOf("lost"), light = setOf("lost"), pulse = "u2" to "list", card = BoardCard("The second save wins", "The first person's item quietly disappears.")),
                ),
            ),
            // 2: "Without help, yes. We'll fix that soon. First, the code. The dot net S..."
            Scene(
                2, "The client objects",
                DiagramSpec(
                    "The SDK mirrors Cosmos itself: a client for the account, then a database, then a container.",
                    nodes = listOf(
                        DNode("client", "Cosmos client", 0.18f, 0.45f, w = 0.32f, h = 0.26f, tone = 1, sub = "the account"),
                        DNode("db", "Database", 0.52f, 0.45f, w = 0.3f, h = 0.26f, tone = 2),
                        DNode("cont", "Container", 0.85f, 0.45f, w = 0.28f, h = 0.26f),
                    ),
                    edges = listOf(
                        DEdge("client", "db"),
                        DEdge("db", "cont"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, light = setOf("client"), pulse = "db" to "cont", card = BoardCard("C#", "client.GetContainer(\"twotracks\", \"logs\")", code = true)),
                ),
            ),
            // 3: "What's the cheapest thing I can do with it?"
            Scene(
                3, "Point read or query",
                DiagramSpec(
                    "A point read goes straight to one item; a query without the partition key asks every partition.",
                    nodes = listOf(
                        DNode("pr", "Point read", 0.22f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "id + partition key"),
                        DNode("one", "One shelf", 0.22f, 0.62f, w = 0.32f, h = 0.22f, sub = "about 1 RU"),
                        DNode("qx", "Query, no key", 0.72f, 0.18f, w = 0.48f, h = 0.2f, tone = 1, sub = "asks every shelf"),
                        DNode("a", "P1", 0.57f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("b", "P2", 0.73f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("c", "P3", 0.89f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("pr", "one"),
                        DEdge("qx", "a"),
                        DEdge("qx", "b"),
                        DEdge("qx", "c"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("pr", "one"), light = setOf("pr"), pulse = "pr" to "one", card = BoardCard("Point read", "Id plus partition key: walk straight to the spot. About 1 RU.")),
                    Beat(6, show = setOf("qx", "a", "b", "c"), light = setOf("qx"), pulse = "qx" to "b", card = BoardCard("For example, a fitness app", "This week's workouts: query with my partition key, so one shelf.")),
                ),
            ),
            // 7: "Okay. How do I write things?"
            Scene(
                7, "Writing items",
                DiagramSpec(
                    "Create adds, upsert adds or replaces, and patch changes only the fields you name.",
                    nodes = listOf(
                        DNode("create", "Create", 0.17f, 0.22f, w = 0.3f, kind = NodeKind.PILL, tone = 1),
                        DNode("upsert", "Upsert", 0.5f, 0.22f, w = 0.3f, kind = NodeKind.PILL, tone = 1),
                        DNode("patch", "Patch", 0.83f, 0.22f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("item", "An item", 0.5f, 0.7f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("create", "item"),
                        DEdge("upsert", "item"),
                        DEdge("patch", "item", "named fields"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("item")),
                    Beat(8, show = setOf("create", "upsert", "patch"), light = setOf("patch"), pulse = "patch" to "item", card = BoardCard("Patch", "Changes just the named fields. Handy for bumping a counter.")),
                ),
            ),
            // 9: "Now, my shopping list. How do I stop that lost change?"
            Scene(
                9, "A version stamp",
                DiagramSpec(
                    "Each save sends back the ETag it read; if the item has changed since, the save fails with 412.",
                    nodes = listOf(
                        DNode("u1", "Person 1", 0.16f, 0.22f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("u2", "Person 2", 0.16f, 0.68f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("item", "Shopping list", 0.74f, 0.3f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "ETag: edition 7"),
                        DNode("err", "412: read again", 0.74f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("u1", "item", "if-match 7"),
                        DEdge("u2", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("item"), light = setOf("item"), card = BoardCard("ETag", "A version stamp, like a newspaper's edition number. Changes on every write.")),
                    Beat(12, show = setOf("u1", "u2", "err"), light = setOf("err"), pulse = "u1" to "item", card = BoardCard("If-match", "Save only if it is still the edition I read. Otherwise: 412.")),
                    Beat(14, light = setOf("item"), card = BoardCard("Optimistic concurrency", "No lock. Hope nobody clashes, and check the stamp when you save.")),
                ),
            ),
            // 15: "You said stored procedure. What runs inside the database?"
            Scene(
                15, "Code inside the database",
                DiagramSpec(
                    "Four kinds of server-side JavaScript, each working inside one partition key.",
                    nodes = listOf(
                        DNode("sp", "Stored procedure", 0.26f, 0.16f, w = 0.46f, h = 0.22f, sub = "all or nothing"),
                        DNode("pre", "Pre-trigger", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "before a write"),
                        DNode("post", "Post-trigger", 0.26f, 0.5f, w = 0.46f, h = 0.22f, tone = 1, sub = "after a write"),
                        DNode("udf", "User-defined function", 0.76f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "inside a query"),
                        DNode("named", "Triggers must be named", 0.5f, 0.85f, w = 0.66f, kind = NodeKind.NOTE, sub = "in the request"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Say you're building a booking app", "Booking and payment record: a stored procedure saves both, or neither.")),
                    Beat(18, show = setOf("pre", "post", "udf"), light = setOf("pre", "post", "udf")),
                    Beat(20, show = setOf("named"), light = setOf("named"), card = BoardCard("The trigger trap", "Triggers don't fire on their own. Name them in the request.")),
                ),
            ),
            // 21: "Can items delete themselves after a while?"
            Scene(
                21, "A sell-by date",
                DiagramSpec(
                    "A TTL, in seconds, on the container or on each item, makes Cosmos remove items for you.",
                    nodes = listOf(
                        DNode("item", "Login code", 0.2f, 0.4f, w = 0.32f, h = 0.22f),
                        DNode("ttl", "TTL: 300", 0.5f, 0.82f, w = 0.4f, kind = NodeKind.PILL, sub = "time to live, seconds"),
                        DNode("gone", "Removed", 0.8f, 0.4f, w = 0.3f, h = 0.22f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("item", "gone", "after 5 minutes"),
                        DEdge("ttl", "item", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("item")),
                    Beat(22, show = setOf("ttl", "gone"), light = setOf("ttl"), pulse = "item" to "gone", card = BoardCard("TTL: time to live", "A sell-by date. For example, a login code gone after five minutes.")),
                ),
            ),
            // 23: "Nice. Now, the change feed. What is it, in plain words?"
            Scene(
                23, "The change feed",
                DiagramSpec(
                    "The change feed lists inserts and updates in order; a function reacts, and a lease container keeps its place.",
                    nodes = listOf(
                        DNode("logs", "Study logs", 0.17f, 0.2f, w = 0.3f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("feed", "Change feed", 0.5f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL, sub = "inserts, updates"),
                        DNode("func", "Function", 0.83f, 0.2f, w = 0.3f, h = 0.2f, sub = "Cosmos DB trigger"),
                        DNode("nodel", "No deletes", 0.17f, 0.58f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "use a soft delete"),
                        DNode("lease", "Lease container", 0.5f, 0.58f, w = 0.3f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 1, sub = "the bookmark"),
                        DNode("streak", "Streak", 0.83f, 0.58f, w = 0.3f, h = 0.2f, tone = 1),
                        DNode("prefix", "Sharing? A prefix each", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("logs", "feed"),
                        DEdge("feed", "func"),
                        DEdge("func", "streak", "update"),
                        DEdge("func", "lease", "checkpoint"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("logs", "feed", "func"), light = setOf("feed"), pulse = "feed" to "func", card = BoardCard("Change feed", "A running list of inserts and updates, like a document's activity log.")),
                    Beat(26, show = setOf("streak"), light = setOf("streak"), pulse = "func" to "streak", card = BoardCard("Picture a study app", "Each new study log, a function updates the user's streak.")),
                    Beat(28, show = setOf("nodel"), light = setOf("nodel"), card = BoardCard("Deletes are not in the feed", "Mark items deleted instead, a soft delete, often with a TTL.")),
                    Beat(30, show = setOf("lease"), light = setOf("lease"), pulse = "func" to "lease", card = BoardCard("Lease container", "A bookmark of checkpoints, so no change is handled twice.")),
                    Beat(32, show = setOf("prefix"), light = setOf("prefix"), card = BoardCard("Replay history?", "Set start from beginning: every existing item comes through once.")),
                ),
            ),
            // 33: "So what should I remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Cosmos DB code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cheap", "Cheapest read", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "point read"),
                        DNode("race", "Two writers", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "ETag, if-match"),
                        DNode("atomic", "All or nothing", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "stored procedure"),
                        DNode("react", "React to every write", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "change feed, lease"),
                        DNode("traps", "Traps", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "name triggers; no deletes in the feed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("cheap", "race", "atomic", "react"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("traps"), light = setOf("traps"), card = BoardCard("Two traps", "Triggers run only when named. Deletes are not in the change feed.")),
                    Beat(38, card = BoardCard("Today", "CreateItemAsync, ReadItemAsync, compare RU with a query, then ttl 60.")),
                ),
            ),
        ),
    )

    val all: List<PodcastBoard> by lazy { listOf(appService(), slotsAndScaling(), functions(), durable(), containers(), apim(), eventGrid(), eventHubs(), serviceBus(), appInsights(), availability(), redis(), cdn(), identity(), msal(), graph(), keyVault(), appConfig(), blobBasics(), blobSdk(), cosmosModel(), cosmosSdk()) }
}
