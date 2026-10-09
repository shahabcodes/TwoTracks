package com.shahaabapps.twotracks.ui.guide

/**
 * What the tour says, in order.
 *
 * It explains the app in the order someone would actually use it: the
 * studying first, then the day it belongs to, then the two longer views that
 * say whether any of it is working, and finally every row of Settings. Each
 * step names the screen it belongs to and the app navigates there as the tour
 * moves, so nothing here describes a screen the reader is not looking at.
 *
 * Settings is walked row by row because it is where everything is decided and
 * none of it is guessable: a start date that changes what counts as missed, a
 * voice picker whose labels you set yourself, a reminder that can take over
 * the screen. It scrolls as one piece, so each row can be brought into view
 * and ringed.
 *
 * On the other screens only what is on display is ringed. Those are lazy
 * lists: what is below the fold has not been composed, so there is nothing to
 * measure and nothing to scroll to. Those steps say their piece and ring the
 * tab, which is honest about where the reader has been taken.
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
        "Learn is where you study",
        "Both courses live here, and the app opens on it. Everything else in Two Tracks measures what happens on this tab.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_TODAY_CARD,
        "A day you can finish",
        "One topic to read, the words that are due, and the questions that have come back round. Three things, and the day is done — a loop you can close, not a list that only grows. Tap any line to go straight to it.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_COURSES,
        "Inside a course",
        "Open a course and you get its modules, each a handful of topics and a test. Topics you have read show how well you know them rather than a tick, and anything slipping is offered back at the top under \"Worth going back to\".",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_COURSES,
        "Inside a topic",
        "A topic guesses before it teaches: a question you are not expected to know, then the reading, then checks that ask how sure you are before showing the answer. It finishes by asking you to say it back in your own words and marks what you covered.",
    ),
    GuideStep(
        learnRoute, GuideAnchors.LEARN_TODAY_CARD,
        "Words and daily review",
        "German words become flashcards once you have read their topic, and come back on a schedule that stretches as they stick. Questions you got wrong come back the same way, in Daily review.",
    ),

    // ---- the day, the ladder, the two of you ----
    GuideStep(
        todayRoute, GuideAnchors.TAB_TODAY,
        "Today is the plan for today",
        "Your study slots, your streak, and whether your partner has studied yet. Minutes count themselves as you read, sit a test or clear the deck, so there is nothing to fill in here — Done just marks a slot dealt with.",
    ),
    GuideStep(
        levelsRoute, GuideAnchors.TAB_LEVELS,
        "Levels are the road to the move",
        "Five levels, each a short checklist with a date: the courses to pass and the paperwork to get done. Tick off a task like the anabin degree check as it happens; a level completes when its required items are done.",
    ),
    GuideStep(
        levelsRoute, GuideAnchors.TAB_LEVELS,
        "And the rest of the guide",
        "Below the levels: countries and what each needs, official test results against your targets, the document checklist per person, and the budget. All seeded from the guide and all editable.",
    ),
    GuideStep(
        usRoute, GuideAnchors.TAB_US,
        "US is the two of you",
        "The shared run counts only days you both studied — nobody can keep it going alone. Under it, four weeks of dots showing studied, missed and rest days, and the weekly counts like job applications and teaching days.",
    ),

    // ---- settings, row by row ----
    GuideStep(
        settingsRoute, GuideAnchors.SET_NAME,
        "Settings: your name",
        "At the bottom of US. This is the name your partner sees on the shared screens, and the one the app greets you by.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TRACK,
        "Your track",
        "Which of you this is. The track decides which courses and which roadmap you get: the developer goes to AZ-204 and German, the teacher to IELTS and German.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_PASSWORD,
        "Email and password",
        "Change your password here, or have a reset link sent if you cannot remember the old one. Your email is the account; it is not shown to anyone else.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TIMEZONE,
        "Timezone",
        "Everything dated uses this: when a day rolls over, when reminders fire, and whose calendar the shared streak is counted on. Worth setting if the two of you are in different places.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_INVITE,
        "The invite code",
        "Six letters. Your partner chooses Join on their own phone and enters this, and you are both in the same space — same streak, same levels, their progress on your screens.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TEMPLATE,
        "Your weekly template",
        "The hours you actually have, by weekday: work blocks that are unavailable, study blocks the planner fills, and class blocks tied to a goal. Everything the app says about pace is measured against this.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_START_DATE,
        "Your start date",
        "The day your study begins counting. Nothing before it is ever held against you, which matters if the space was set up weeks before you actually started. Leave it unset and the plan's own first day is used.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_FULL_SCREEN,
        "Reminders that take over the screen",
        "On, a reminder stands in front of whatever you are doing and offers to start the session. Off, it waits as a banner. Android asks for this one itself, so this row opens its settings page.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_BIOMETRICS,
        "Locking the app",
        "Asks for your fingerprint, face or PIN when the app opens and after it has been in the background a while. It needs a screen lock set up on the phone first.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_COMPANION,
        "How it looks",
        "Light, dark or whatever the phone is doing, and four accent colours. Below that, who keeps you company on the sign-in screen — they watch what you type and cover their eyes for your password.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_VOICES,
        "The voice each language is read in",
        "One voice for German, one for English. Tap one to hear it and keep it. Android will not tell an app whether a voice is a man or a woman, so Describe lets you say once what you heard, and the list says so from then on.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SEED,
        "Guide content",
        "Puts the goals, roadmap, countries, tests, documents and budget back to what the guide says, and moves every date to start from today. Your logged hours and your own entries are kept.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_TOUR,
        "This tour",
        "It runs by itself whenever somebody signs in, and it is here whenever you want it again. Skip it any time — nothing is waiting on it.",
    ),
    GuideStep(
        settingsRoute, GuideAnchors.SET_SIGN_OUT,
        "That is everything",
        "Sign out hands the phone to the other one of you, and they get this tour when they sign in. Go and read something.",
    ),
)
