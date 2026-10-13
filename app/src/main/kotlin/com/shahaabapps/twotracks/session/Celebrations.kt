package com.shahaabapps.twotracks.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/** What just happened, which decides the overlay's icon, label and motion. */
enum class CelebrationKind { SAVED, REMOVED, PASSED, LOGGED }

data class Celebration(val kind: CelebrationKind, val label: String, val id: Long = System.nanoTime())

/**
 * One-shot events for the full-screen celebration overlay. Any write path
 * calls [saved], [removed], [passed] or [logged] after its write succeeds; the
 * overlay hosted in AppActivity plays the matching animation.
 */
@Singleton
class Celebrations @Inject constructor() {
    private val _events = MutableSharedFlow<Celebration>(extraBufferCapacity = 8)
    val events: SharedFlow<Celebration> = _events

    fun saved(label: String = "Saved") = play(CelebrationKind.SAVED, label)
    fun removed(label: String = "Removed") = play(CelebrationKind.REMOVED, label)
    fun passed(label: String = "Passed") = play(CelebrationKind.PASSED, label)
    fun logged(label: String = "Logged") = play(CelebrationKind.LOGGED, label)

    private fun play(kind: CelebrationKind, label: String) {
        _events.tryEmit(Celebration(kind, label))
    }
}
