package com.shahaabapps.twotracks

import com.shahaabapps.core.ui.theme.AppLook
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** The launcher icon follows the look, and the manifest has an alias for each. */
class LauncherIconTest {
    @Test
    fun `each look enables its own icon and disables the other`() {
        assertEquals(LauncherIcon.PARALLEL to LauncherIcon.DEPARTURES, LauncherIcon.aliases(AppLook.PARALLEL))
        assertEquals(LauncherIcon.DEPARTURES to LauncherIcon.PARALLEL, LauncherIcon.aliases(AppLook.DEPARTURES))
    }

    @Test
    fun `the manifest declares both aliases, Parallel on by default under the icon's old name`() {
        val manifest = File("src/main/AndroidManifest.xml").readText()
        fun alias(name: String) = Regex("""<activity-alias\s+android:name="\.$name"(.*?)</activity-alias>""", RegexOption.DOT_MATCHES_ALL).find(manifest)?.groupValues?.get(1)
        val parallel = alias(LauncherIcon.PARALLEL.substringAfterLast('.'))
        val departures = alias(LauncherIcon.DEPARTURES.substringAfterLast('.'))
        requireNotNull(parallel) { "no alias for ${LauncherIcon.PARALLEL}" }
        requireNotNull(departures) { "no alias for ${LauncherIcon.DEPARTURES}" }
        assertTrue(parallel.contains("android:enabled=\"true\"") && parallel.contains("category.LAUNCHER"))
        assertTrue(departures.contains("android:enabled=\"false\"") && departures.contains("category.LAUNCHER"))
        assertTrue(departures.contains("@mipmap/ic_launcher_departures"))
    }
}
