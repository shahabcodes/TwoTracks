package com.shahaabapps.twotracks.ui.guide

/**
 * What the tour says, in order.
 *
 * One idea a step, in as few words as carry it. A tour is read standing up
 * with a thumb on Next, and a paragraph on a dimmed screen is skipped rather
 * than read — which costs more than saying less would have.
 *
 * It explains the app in the order someone would use it: the studying first,
 * then the day it belongs to, then the longer views, then every row of
 * Settings. Each step names the screen it belongs to and the app navigates
 * there as the tour moves, so nothing describes a screen the reader is not
 * looking at.
 *
 * Settings is walked row by row because it is where everything is decided and
 * none of it is guessable. It scrolls as one piece, so each row can be brought
 * into view and ringed. The other screens are lazy lists, where anything below
 * the fold has not been composed and cannot be measured, so those steps ring
 * the tab instead.
 */
fun guideSteps(
    learnRoute: String,
    todayRoute: String,
    levelsRoute: String,
    usRoute: String,
    settingsRoute: String,
): List<GuideStep> = listOf(
    // ---- the studying ----
    GuideStep(
        learnRoute, GuideAnchors.TAB_LEARN,
        "Start here",
        "Both courses live on Learn, and the app opens on it.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_TODAY_CARD,
        "Today's three things",
        "A topic, the words that are due, the questions that came back. Tap any line to start it.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_COURSES,
        "Inside a course",
        "Modules of topics, each with a test. Topics show how well you know them, not just a tick.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_COURSES,
        "Inside a topic",
        "Guess, read, then checks that ask how sure you are. It ends by asking you to say it back.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_TODAY_CARD,
        "Words come back",
        "German words become cards once you read their topic. Wrong answers return in Daily review.",
    ),

    // ---- the day, the ladder, the two of you ----
    GuideStep(
        todayRoute, GuideAnchors.TAB_TODAY,
        "Today",
        "Your slots and your streak. Minutes count themselves as you read — nothing to fill in.",
    ),
    GuideStep(
        levelsRoute, GuideAnchors.TAB_LEVELS,
        "Levels",
        "Five checklists with dates: courses to pass, paperwork to do. Tick things off as they happen.",
    ),
    GuideStep(
        levelsRoute, GuideAnchors.TAB_LEVELS,
        "Below the levels",
        "Countries, test scores, documents and budget. All from the guide, all editable.",
    ),
    GuideStep(
        usRoute, GuideAnchors.TAB_US,
        "US",
        "The shared run counts only days you both studied. Below it, four weeks and your weekly counts.",
    ),

    // ---- settings, row by row ----
    GuideStep(
        settingsRoute, GuideAnchors.SET_NAME,
        "Settings is at the bottom of US",
        "Your name is what your partner sees.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TRACK,
        "Your track",
        "Decides your courses and your roadmap.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_PASSWORD,
        "Password",
        "Change it here, or have a reset link sent.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TIMEZONE,
        "Timezone",
        "Sets when your day rolls over and when reminders fire.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_INVITE,
        "Invite code",
        // Read by whoever created the space and by whoever joined it, so it
        // cannot say "your partner enters this": for one of the two that
        // already happened, and the other way round it is simply wrong.
        "The six letters that put the two of you in one space.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TEMPLATE,
        "Weekly template",
        "The hours you actually have. Everything about pace is measured against it.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_START_DATE,
        "Start date",
        "Nothing before this counts as a day you missed.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_FULL_SCREEN,
        "Full-screen reminders",
        "On, a reminder takes over the screen. Off, it waits as a banner.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_BIOMETRICS,
        "Lock the app",
        "Fingerprint, face or PIN when it opens.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_COMPANION,
        "How it looks",
        "Light or dark, four accents, and who keeps you company when you sign in.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_VOICES,
        "Voices",
        "One per language. Tap to hear it. Describe tells the list what you heard.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SEED,
        "Guide content",
        "Puts the plan back to the guide and restarts its dates. Your hours are kept.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TOUR,
        "This tour",
        "It runs on every sign-in, and it is here whenever you want it again.",
    ),
    // Widgets live on the home screen, so there is nothing in the app to
    // ring. A step with no anchor shows its words on their own.
    GuideStep(
        settingsRoute, null,
        "Home screen widgets",
        "Four of them. Hold one on the home screen to pick what it shows, whose, and which course.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SIGN_OUT,
        "That's everything",
        "Sign out and the other one of you gets this tour. Go and read something.",
    ),
)
