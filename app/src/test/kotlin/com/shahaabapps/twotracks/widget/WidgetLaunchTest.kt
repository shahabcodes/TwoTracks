package com.shahaabapps.twotracks.widget

import com.shahaabapps.core.content.Courses
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * The gate between a widget's intent and the navigation graph. Navigating to a
 * route the graph does not know throws, and this runs on launch, so anything
 * that is not one of the three shapes the widgets build is dropped rather than
 * passed on.
 */
class WidgetLaunchTest {

    private val launch = WidgetLaunch()

    @Test
    fun `the routes the widgets actually build are accepted`() {
        for (route in listOf(
            WidgetRoutes.topic("az-204", "compute-app-service"),
            WidgetRoutes.deck("german-a1"),
            WidgetRoutes.dailyReview("az-204"),
        )) {
            launch.consume()
            launch.offer(route)
            assertEquals(route, launch.route.value)
        }
    }

    @Test
    fun `the daily review route names the module the quiz screen expects`() {
        assertEquals("learn/az-204/quiz/${Courses.DAILY_MODULE_ID}", WidgetRoutes.dailyReview("az-204"))
    }

    @Test
    fun `anything else is dropped rather than navigated to`() {
        for (route in listOf(
            null, "", "today", "learn", "learn/az-204",
            "learn/az-204/topic", "learn/az-204/topic/a/b", "learn//deck",
            "settings/secret", "../../etc",
        )) {
            launch.consume()
            launch.offer(route)
            assertNull("should have dropped $route", launch.route.value)
        }
    }

    @Test
    fun `a consumed route is not navigated to twice`() {
        launch.offer(WidgetRoutes.deck("german-a1"))
        launch.consume()
        assertNull(launch.route.value)
    }
}
