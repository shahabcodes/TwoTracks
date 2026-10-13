package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.Az204
import com.shahaabapps.core.content.GermanA1
import com.shahaabapps.core.content.Ielts
import org.junit.Assert.assertEquals
import org.junit.Test

/** The words the Departures board prints on Learn and a course. */
class BoardLearnWordsTest {
    @Test
    fun `each course has its own line code and exam`() {
        assertEquals("AZ", lineCode(Az204.course))
        assertEquals("DE", lineCode(GermanA1.course))
        assertEquals("IE", lineCode(Ielts.course))
        assertEquals("AZ-204", boardExam(Az204.course))
        assertEquals("Goethe A1", boardExam(GermanA1.course))
        assertEquals("IELTS", boardExam(Ielts.course))
    }

    @Test
    fun `the header counts the services and the ones on your line`() {
        assertEquals("3 SERVICES · 2 ON YOUR LINE", servicesLine(3, 2))
        assertEquals("1 SERVICE · 1 ON YOUR LINE", servicesLine(1, 1))
        assertEquals("2 SERVICES · NONE ON YOUR LINE", servicesLine(2, 0))
    }

    @Test
    fun `a service says its modules, or whose line it is`() {
        val course = GermanA1.course
        val n = course.modules.size
        val tail = if (course.mockExam != null) ", then a mock exam" else ""
        assertEquals("$n modules$tail", serviceLine(course, partnerOnly = null))
        assertEquals("Sanya's line · $n modules", serviceLine(course, partnerOnly = "Sanya"))
    }
}
