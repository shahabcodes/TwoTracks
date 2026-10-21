package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseFavoritesTest {
    private fun startingOrder(role: PersonRole) =
        CourseFavorites.sorted(Courses.all, CourseFavorites.starting(role), role).map { it.id }

    @Test
    fun `the developer starts with AI-200, AZ-305 and German A1 on top`() {
        assertEquals(listOf("ai-200", "az-305", "de-a1", "az-204", "ielts"), startingOrder(PersonRole.TECH))
    }

    @Test
    fun `the teacher starts with IELTS, then German A1, on top`() {
        assertEquals(listOf("ielts", "de-a1", "az-204", "ai-200", "az-305"), startingOrder(PersonRole.TEACHER))
    }

    @Test
    fun `every starting star is a real course`() {
        val ids = Courses.all.map { it.id }.toSet()
        PersonRole.entries.forEach { role -> assertTrue("$role", ids.containsAll(CourseFavorites.first(role))) }
    }

    @Test
    fun `with nothing starred the order is the courses' own`() {
        assertEquals(Courses.all.map { it.id }, CourseFavorites.sorted(Courses.all, emptySet(), PersonRole.TEACHER).map { it.id })
    }
}
