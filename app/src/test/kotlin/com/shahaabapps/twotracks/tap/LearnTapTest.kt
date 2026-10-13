package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.GermanA1
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.CourseContent
import com.shahaabapps.twotracks.feature.learn.DeckContent
import com.shahaabapps.twotracks.feature.learn.LearnContent
import com.shahaabapps.twotracks.feature.learn.QuizContent
import com.shahaabapps.twotracks.feature.learn.TopicContent
import com.shahaabapps.twotracks.preview.LearnSamples
import com.shahaabapps.twotracks.preview.Samples
import org.junit.Test

/** Learn, a course, a topic, a test and the word deck: what each press reaches. */
class LearnTapTest(look: AppLook) : TapTest(look) {
    private val de = GermanA1.course

    @Test
    fun `learn opens a course, the daily review and the deck`() {
        val state = LearnSamples.learn()
        show { LearnContent(state, onOpenCourse = record1("course"), onOpenTopic = record2("topic"), onOpenDeck = record1("deck"), onOpenQuiz = record2("quiz")) }
        tap("German A1")
        expect("course(${de.id})")
        tap("Daily review", substring = true)
        expect("quiz(${state.dailyDue.maxByOrNull { it.value }!!.key}, ${Courses.DAILY_MODULE_ID})")
        tap("Words")
        expect("deck(${state.decks.keys.first()})")
    }

    @Test
    fun `a course opens its next topic, its module test and goes back`() {
        val here = de.modules[1]
        show { CourseContent(de.id, LearnSamples.learn(), Samples.today, onBack = record("back"), onOpenTopic = record2("topic"), onOpenQuiz = record2("quiz")) }
        tap("Words · family, numbers and countries")
        expect("topic(${de.id}, ${here.topics.first().id})")
        tap("Module 2 test")
        expect("quiz(${de.id}, ${here.id})")
    }

    @Test
    fun `a topic goes back and can be marked learned`() {
        show {
            TopicContent(
                LearnSamples.topic(), speaker = null, voiceMissing = false, recognizer = null, onBack = record("back"), onOpenTopic = record2("topic"), onOpenQuiz = record2("quiz"),
                onInstallVoice = record("voice"), onChecked = { _, _, _, _ -> calls += "checked" }, onConfidence = { _, _ -> calls += "confidence" },
                onMarkLearned = record("learned"), onUnlearn = record("unlearn"), onMessageShown = {},
            )
        }
        tapDescribed("Back to the course")
        expect("back")
        tap("Mark as learned")
        expect("learned")
    }

    @Test
    fun `a test takes an answer, checks it and can be left`() {
        val state = LearnSamples.quiz()
        val q = state.asked[state.index]
        show {
            QuizContent(state, speaker = null, onBack = record("back"), onOpenTopic = record2("topic"), onToggle = record1("toggle"), onCheck = record("check"), onNext = record("next"), onRetake = record("retake"))
        }
        tap(q.options[2])
        expect("toggle(2)")
        tapDescribed("Leave the test")
        expect("back")
    }

    @Test
    fun `the results go again and finish`() {
        show {
            QuizContent(LearnSamples.results(), speaker = null, onBack = record("back"), onOpenTopic = record2("topic"), onToggle = record1("toggle"), onCheck = record("check"), onNext = record("next"), onRetake = record("retake"))
        }
        tap(label("Take it again", "Board again"))
        expect("retake")
        tap("Done")
        expect("back")
    }

    @Test
    fun `the deck flips, answers both ways and closes`() {
        show {
            DeckContent(
                LearnSamples.deck(), speaker = null, onBack = record("back"), onFlip = record("flip"), onAnswer = record1("answer"),
                onPractiseAnyway = record("practise"), onAgain = record("again"), onInstallVoice = record("voice"),
            )
        }
        tap(label("Say it, then tap to check", "Say it out loud, then flip to check."))
        expect("flip")
        tap("Again")
        tap("Got it")
        expect("answer(false)", "answer(true)")
        tapDescribed("Close the deck")
        expect("back")
    }
}
