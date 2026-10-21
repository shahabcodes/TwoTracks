package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.model.PersonRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The courses starred on this phone, which Learn lists first. Starring is
 * each reader's own, so it stays on the device rather than in the shared
 * space. Until someone changes it, each person starts with the courses
 * they study now: the developer AI-200, AZ-305 and German A1; the teacher
 * IELTS, then French, then German A1.
 */
@Singleton
class CourseFavorites @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("learn", Context.MODE_PRIVATE)

    /** The stars chosen on this phone, or null while they are still the starting ones. */
    private val _chosen = MutableStateFlow(prefs.getStringSet(KEY, null)?.toSet())
    val chosen: StateFlow<Set<String>?> = _chosen.asStateFlow()

    fun toggle(courseId: String, role: PersonRole) {
        val now = _chosen.value ?: starting(role)
        val next = if (courseId in now) now - courseId else now + courseId
        prefs.edit().putStringSet(KEY, next).apply()
        _chosen.value = next
    }

    companion object {
        private const val KEY = "favorites"

        /**
         * The order each person's starred courses take, which is also what
         * they start with. French has no course in Learn yet; once it has
         * one, its id goes between IELTS and German A1.
         */
        fun first(role: PersonRole): List<String> = when (role) {
            PersonRole.TECH -> listOf("ai-200", "az-305", "de-a1")
            PersonRole.TEACHER -> listOf("ielts", "de-a1")
        }

        fun starting(role: PersonRole): Set<String> = first(role).toSet()

        /** Starred courses first (in the person's own order, then any other), then the rest in the courses' own order. */
        fun sorted(courses: List<Course>, favorites: Set<String>, role: PersonRole): List<Course> {
            val order = first(role)
            val starred = courses.filter { it.id in favorites }.sortedBy { c -> order.indexOf(c.id).let { if (it < 0) order.size else it } }
            return starred + courses.filterNot { it.id in favorites }
        }
    }
}
