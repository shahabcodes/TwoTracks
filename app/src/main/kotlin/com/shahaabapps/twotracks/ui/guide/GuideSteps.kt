package com.shahaabapps.twotracks.ui.guide

/**
 * What the tour says, in order.
 *
 * One idea a step, in as few words as carry it. A tour is read standing up
 * with a thumb on Next, and a paragraph on a dimmed screen is skipped rather
 * than read — which costs more than saying less would have.
 *
 * It explains the app in the order someone would use it: the day first,
 * then the studying, then the longer views, then every row of Settings. Each step names the screen it belongs to and the app navigates
 * there as the tour moves, so nothing describes a screen the reader is not
 * looking at.
 *
 * Settings is walked row by row because it is where everything is decided and
 * none of it is guessable. Every one of its rows can be brought into view and
 * ringed: the list is told which item each row lives in, scrolls there, and
 * then closes the rest of the gap once the row has said where it is. The other
 * screens ring the tab instead, which is a choice about what is worth pointing
 * at rather than a limit.
 */
fun guideSteps(
    learnRoute: String,
    todayRoute: String,
    levelsRoute: String,
    usRoute: String,
    settingsRoute: String,
): List<GuideStep> = listOf(
    // ---- the day ----
    GuideStep(
        todayRoute, GuideAnchors.TAB_TODAY,
        "Start here",
        "Today opens the app: your next session first, then both your days and the run you share.",
    ),

    // ---- the studying ----
    GuideStep(
        learnRoute, GuideAnchors.TAB_LEARN,
        "Learn",
        "Both courses live here.",
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

    // ---- the two of you, and the ladder ----
    GuideStep(
        usRoute, GuideAnchors.TAB_US,
        "Together",
        "The two of you side by side, and everything you are planning as a pair.",
    ),
    GuideStep(
        usRoute, GuideAnchors.US_LEVELS,
        "Levels",
        "Five checklists with dates each: courses to pass, paperwork to do.",
    ),
    GuideStep(
        levelsRoute, GuideAnchors.LEVELS_LADDER,
        "Tick things off as they happen",
        "Countries, tests, documents and budget sit on Together, under the move. All from the guide, all editable.",
    ),

    // ---- settings, row by row ----
    GuideStep(
        settingsRoute, GuideAnchors.SET_NAME,
        "Settings is behind the gear on Today",
        "Edit holds your name, time zone and password. Your name is what your partner sees.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_INVITE,
        "Invite code",
        // Read by whoever created the space and by whoever joined it, so it
        // cannot say "your partner enters this": for one of the two that
        // already happened, and the other way round it is simply wrong.
        "The six letters that put the two of you in one space. Tap to copy.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TEMPLATE,
        "Weekly hours",
        "The hours you actually have. Everything about pace is measured against it.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_START_DATE,
        "Study started",
        "Nothing before this counts as a day you missed.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_FULL_SCREEN,
        "Full-screen reminders",
        "On, a reminder takes over the screen. Off, it waits as a banner.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_APPEARANCE,
        "How it looks",
        "Parallel or the Departures board, and for Parallel light, dark or whichever the phone is using. The icon follows the look.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_BIOMETRICS,
        "Lock the app",
        "Fingerprint, face or PIN when it opens.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_VOICES,
        "Voices",
        "One per language. Tap to hear it. Describe tells the list what you heard.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_WIDGETS,
        "Home screen widgets",
        "Four of them. This is where you say what each one shows, whose, and which course.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TOUR,
        "This tour",
        "It runs on every sign-in, and it is here whenever you want it again.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SEED,
        "Guide content",
        "Puts the plan back to the guide and restarts its dates. Your hours are kept.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SIGN_OUT,
        "That's everything",
        "Sign out and the other one of you gets this tour. Go and read something.",
    ),
)
