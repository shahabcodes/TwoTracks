package com.shahaabapps.twotracks.ui.guide

/**
 * What the tour says, in order.
 *
 * It explains the app in the order someone would actually use it: the studying
 * first, then the day it belongs to, then the two longer views that say
 * whether any of it is working. Each step rings a real control and the app
 * navigates to the screen that control is on, so nothing here describes a
 * screen the reader is not looking at.
 *
 * Only the tab bar and the top of Learn are ringed, because those are on
 * screen whatever has been scrolled. A step pointing at something further down
 * a list would ring empty space as often as not.
 */
fun guideSteps(learnRoute: String, todayRoute: String, levelsRoute: String, usRoute: String): List<GuideStep> = listOf(
    GuideStep(
        route = learnRoute,
        anchor = GuideAnchors.TAB_LEARN,
        title = "Learn is where you study",
        body = "Both courses live here, and the app opens on it. Everything else in Two Tracks measures what happens on this tab.",
    ),
    GuideStep(
        route = learnRoute,
        anchor = GuideAnchors.LEARN_TODAY_CARD,
        title = "A day you can finish",
        body = "One topic to read, the words that are due, and the questions that have come back round. Three things, and the day is done — it is a loop you can close, not a list that only grows.",
    ),
    GuideStep(
        route = learnRoute,
        anchor = GuideAnchors.LEARN_COURSES,
        title = "Read, then prove it",
        body = "A topic asks you to guess before you read, checks you as you go, and asks you to say it back in your own words. Pass the module test and the course moves on.",
    ),
    GuideStep(
        route = todayRoute,
        anchor = GuideAnchors.TAB_TODAY,
        title = "Today is the plan for today",
        body = "Your slots, your streak, and whether your partner has studied yet. Your minutes count themselves as you read, so there is nothing to fill in here.",
    ),
    GuideStep(
        route = levelsRoute,
        anchor = GuideAnchors.TAB_LEVELS,
        title = "Levels are the road to the move",
        body = "Each level is a short checklist with a date: the courses to pass and the paperwork to get done, like the anabin degree check. Tick them off as they happen.",
    ),
    GuideStep(
        route = usRoute,
        anchor = GuideAnchors.TAB_US,
        title = "US is the two of you",
        body = "The shared run counts only days you both studied. Settings lives at the bottom of this tab: the voice each language is read in, when reminders arrive, and the day your study starts counting.",
    ),
)
