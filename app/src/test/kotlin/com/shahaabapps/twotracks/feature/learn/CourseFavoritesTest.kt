package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.Courses
import org.junit.Assert.assertEquals
import org.junit.Test

class CourseFavoritesTest {
    @Test
    fun `starred courses come first, then the rest, each in course order`() {
        val ids = CourseFavorites.sorted(Courses.all, CourseFavorites.DEFAULT).map { it.id }
        assertEquals(listOf("ai-200", "az-305", "de-a1", "az-204", "ielts"), ids)
    }

    @Test
    fun `with nothing starred the order is the courses' own`() {
        assertEquals(Courses.all.map { it.id }, CourseFavorites.sorted(Courses.all, emptySet()).map { it.id })
    }
}
