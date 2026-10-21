package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import com.shahaabapps.core.content.Course
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The courses starred on this phone, which Learn lists first. Starring is
 * each reader's own, so it stays on the device rather than in the shared
 * space. Until someone changes it, the courses being studied now are
 * starred: AI-200, AZ-305 and German A1.
 */
@Singleton
class CourseFavorites @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("learn", Context.MODE_PRIVATE)

    private val _ids = MutableStateFlow(prefs.getStringSet(KEY, null)?.toSet() ?: DEFAULT)
    val ids: StateFlow<Set<String>> = _ids.asStateFlow()

    fun toggle(courseId: String) {
        val next = if (courseId in _ids.value) _ids.value - courseId else _ids.value + courseId
        prefs.edit().putStringSet(KEY, next).apply()
        _ids.value = next
    }

    companion object {
        private const val KEY = "favorites"
        val DEFAULT = setOf("ai-200", "az-305", "de-a1")

        /** The order starred courses take: the current exams first, then any other starred one. */
        private val FIRST = listOf("ai-200", "az-305", "de-a1")

        /** Starred courses first (AI-200, AZ-305, German A1, then any other), then the rest in the courses' own order. */
        fun sorted(courses: List<Course>, favorites: Set<String>): List<Course> {
            val starred = courses.filter { it.id in favorites }.sortedBy { c -> FIRST.indexOf(c.id).let { if (it < 0) FIRST.size else it } }
            return starred + courses.filterNot { it.id in favorites }
        }
    }
}
