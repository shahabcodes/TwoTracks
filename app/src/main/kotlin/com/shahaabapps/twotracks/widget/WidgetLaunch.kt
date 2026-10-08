package com.shahaabapps.twotracks.widget

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Where a widget tap wants to land.
 *
 * The activity cannot navigate when the tap arrives: the session may still be
 * loading, or the app may be locked, and the graph does not exist until a
 * session is ready. So the route waits here and the graph picks it up when it
 * is in a position to honour it.
 */
@Singleton
class WidgetLaunch @Inject constructor() {

    private val _route = MutableStateFlow<String?>(null)
    val route: StateFlow<String?> = _route.asStateFlow()

    /**
     * Offers a route from a widget's intent. Only the three shapes the widgets
     * build are accepted: navigating to a route the graph does not know throws,
     * and an intent extra is not worth a crash on launch.
     */
    fun offer(route: String?) {
        if (route != null && SHAPES.any { it.matches(route) }) _route.value = route
    }

    /** Called once the route has been navigated to, so a rotation does not repeat it. */
    fun consume() {
        _route.value = null
    }

    private companion object {
        val SHAPES = listOf(
            Regex("^learn/[^/]+/topic/[^/]+$"),
            Regex("^learn/[^/]+/quiz/[^/]+$"),
            Regex("^learn/[^/]+/deck$"),
        )
    }
}
