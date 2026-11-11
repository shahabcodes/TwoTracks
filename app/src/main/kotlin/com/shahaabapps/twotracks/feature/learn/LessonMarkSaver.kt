package com.shahaabapps.twotracks.feature.learn

import android.util.Log
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.ink.LessonInkRepository
import com.shahaabapps.core.model.LessonInk
import com.shahaabapps.core.model.LessonMark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Writes a lesson's marks, a moment after the last one is drawn.
 *
 * It has a scope of its own rather than the lesson's, and that is the whole
 * reason it exists. Marking up a paragraph is a dozen strokes in a few
 * seconds, so the write is held back until the pen pauses — and the most
 * likely moment for the pen to pause for good is the moment somebody leaves
 * the lesson, which is exactly when a view model's scope is cancelled. A
 * delayed write owned by the screen being marked up is a write that is
 * thrown away precisely when it matters.
 *
 * Writes for one lesson are serialised, so the finished state is the last
 * one drawn rather than whichever upload happened to land last.
 */
@Singleton
class LessonMarkSaver @Inject constructor(
    private val repository: LessonInkRepository,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** The record each lesson's marks are kept at, once it has one. */
    private val records = mutableMapOf<String, LessonInk>()
    private val pending = mutableMapOf<String, Job>()
    private val lock = Mutex()

    /** Remembers the record a lesson already had, so a save updates it. */
    fun known(record: LessonInk) {
        if (record.topicId.isNotBlank()) records[record.topicId] = record
    }

    /**
     * Writes [marks] after a pause, replacing any write still waiting.
     * [now] true writes at once, for the pen being put away or the lesson
     * closing, where there is nothing left to wait for.
     */
    fun save(coupleId: String, lesson: LessonInk, marks: List<LessonMark>, now: Boolean = false) {
        pending.remove(lesson.topicId)?.cancel()
        pending[lesson.topicId] = scope.launch {
            if (!now) delay(SETTLE_MS)
            lock.withLock { write(coupleId, lesson, marks) }
        }
    }

    private suspend fun write(coupleId: String, lesson: LessonInk, marks: List<LessonMark>) {
        val known = records[lesson.topicId] ?: lesson
        if (marks.isEmpty()) {
            // Every mark rubbed out takes the record with it, rather than
            // leaving a row pointing at an empty file for every lesson that
            // was ever marked and then cleaned.
            if (known.id.isNotBlank()) {
                when (val result = repository.clear(coupleId, known)) {
                    is AppResult.Success -> records.remove(lesson.topicId)
                    is AppResult.Failure -> Log.w(TAG, "clearing ${lesson.topicId} failed", result.error)
                }
            }
            return
        }
        val record = known.copy(
            courseId = lesson.courseId, moduleId = lesson.moduleId, topicId = lesson.topicId,
            owner = lesson.owner, updatedAtIso = Instant.now().toString(),
        )
        when (val result = repository.save(coupleId, record, marks)) {
            is AppResult.Success -> records[lesson.topicId] = result.data
            is AppResult.Failure -> Log.w(TAG, "saving the marks on ${lesson.topicId} failed", result.error)
        }
    }

    private companion object {
        /** Long enough to cover a run of strokes, short enough to be invisible. */
        const val SETTLE_MS = 1_200L
        const val TAG = "LessonMarks"
    }
}
