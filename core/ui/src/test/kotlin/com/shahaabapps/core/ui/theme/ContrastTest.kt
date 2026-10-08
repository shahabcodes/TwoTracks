package com.shahaabapps.core.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Whether the palette's own combinations can actually be read.
 *
 * This exists because of a shipped mistake rather than a theory. The warm-up
 * block used `fill` at 0.6 alpha as a background, which turns a 12% pill tint
 * into a solid mid-grey, and then put the secondary label colour on it: 2.7:1
 * in light, 2.9:1 in dark, where body text wants 4.5:1. It was unreadable on
 * a real phone and nothing here could tell.
 *
 * The maths is WCAG 2.1: relative luminance, then (lighter + 0.05) /
 * (darker + 0.05). The thresholds are its AA ones — 4.5:1 for body text,
 * 3:1 for large or bold text and for the boundary of a control.
 */
class ContrastTest {

    // ---------------------------------------------------------------- the maths

    private fun channel(c: Float): Double {
        val v = c.toDouble()
        return if (v <= 0.03928) v / 12.92 else Math.pow((v + 0.055) / 1.055, 2.4)
    }

    private fun luminance(c: Color): Double =
        0.2126 * channel(c.red) + 0.7152 * channel(c.green) + 0.0722 * channel(c.blue)

    private fun ratio(fg: Color, bg: Color): Double {
        val a = luminance(fg)
        val b = luminance(bg)
        return (maxOf(a, b) + 0.05) / (minOf(a, b) + 0.05)
    }

    /** [fg] at [alpha] composited over the opaque [bg], as the screen does it. */
    private fun over(fg: Color, alpha: Float, bg: Color): Color = Color(
        red = fg.red * alpha + bg.red * (1 - alpha),
        green = fg.green * alpha + bg.green * (1 - alpha),
        blue = fg.blue * alpha + bg.blue * (1 - alpha),
    )

    /** The translucent fill resolved onto whatever surface it is drawn on. */
    private fun fillOver(fill: Color, bg: Color): Color = over(fill, fill.alpha, bg)

    private fun assertReadable(name: String, fg: Color, bg: Color, min: Double = BODY) {
        val r = ratio(fg, bg)
        assertTrue("$name is %.2f:1, wanted %.1f:1".format(r, min), r >= min)
    }

    private companion object {
        /** WCAG AA for body text. */
        const val BODY = 4.5
        /** WCAG AA for large or bold text, and for the edge of a control. */
        const val LARGE = 3.0
    }

    private data class Mode(
        val name: String,
        val page: Color,
        val card: Color,
        val label: Color,
        val secondary: Color,
        val fill: Color,
        val separator: Color,
        val success: Color,
        val warn: Color,
        val destructive: Color,
        val accent: (AccentPalette) -> Color,
    )

    private val modes = listOf(
        Mode(
            "light", Palette.GroupedBackgroundLight, Palette.SurfaceLight,
            Palette.LabelLight, Palette.SecondaryLabelLight, Palette.FillLight,
            Palette.SeparatorLight, Palette.SuccessLight, Palette.WarnLight,
            Palette.DestructiveLight, { it.accentLight },
        ),
        Mode(
            "dark", Palette.GroupedBackgroundDark, Palette.SurfaceDark,
            Palette.LabelDark, Palette.SecondaryLabelDark, Palette.FillDark,
            Palette.SeparatorDark, Palette.SuccessDark, Palette.WarnDark,
            Palette.DestructiveDark, { it.accentDark },
        ),
    )

    // ---------------------------------------------------------------- the pairs

    @Test
    fun `text on a card and on the page is readable`() {
        modes.forEach { m ->
            assertReadable("${m.name}: label on card", m.label, m.card)
            assertReadable("${m.name}: label on page", m.label, m.page)
            assertReadable("${m.name}: secondary label on card", m.secondary, m.card)
            assertReadable("${m.name}: secondary label on page", m.secondary, m.page)
        }
    }

    @Test
    fun `text on the translucent fill is readable`() {
        // fill is the idle-pill tint: text fields, segmented tracks and the
        // option rows of a warm-up all sit on it.
        modes.forEach { m ->
            val onCard = fillOver(m.fill, m.card)
            val onPage = fillOver(m.fill, m.page)
            assertReadable("${m.name}: label on fill over card", m.label, onCard)
            assertReadable("${m.name}: label on fill over page", m.label, onPage)
            assertReadable("${m.name}: secondary label on fill over card", m.secondary, onCard)
        }
    }

    @Test
    fun `the status colours read as text on a card`() {
        modes.forEach { m ->
            // These appear as text on both surfaces, so both have to hold.
            assertReadable("${m.name}: success on card", m.success, m.card)
            assertReadable("${m.name}: warn on card", m.warn, m.card)
            assertReadable("${m.name}: destructive on card", m.destructive, m.card)
            assertReadable("${m.name}: success on page", m.success, m.page)
            assertReadable("${m.name}: warn on page", m.warn, m.page)
            assertReadable("${m.name}: destructive on page", m.destructive, m.page)
        }
    }

    @Test
    fun `a status tint keeps its text readable`() {
        // The check, warm-up and faded blocks tint a row with a status colour
        // at low alpha and keep the ordinary ink on top of it.
        modes.forEach { m ->
            listOf("success" to m.success, "warn" to m.warn, "destructive" to m.destructive).forEach { (n, c) ->
                listOf(0.15f, 0.18f).forEach { a ->
                    assertReadable("${m.name}: label on $n at $a over card", m.label, over(c, a, m.card))
                }
            }
        }
    }

    @Test
    fun `every accent tint keeps its text readable`() {
        // Any of the accents can be the current theme, so a block tinted with
        // the accent has to work with all of them.
        modes.forEach { m ->
            AccentPalette.entries.forEach { p ->
                val accent = m.accent(p)
                // 0.08 is the check and faded blocks, which put both the ink
                // and the secondary label on the tint. 0.18 is a marked row in
                // a warm-up, where only the full ink is used.
                val light = over(accent, 0.08f, m.card)
                assertReadable("${m.name}/${p.name}: label on accent at 0.08", m.label, light)
                assertReadable("${m.name}/${p.name}: secondary on accent at 0.08", m.secondary, light)
                assertReadable("${m.name}/${p.name}: label on accent at 0.18", m.label, over(accent, 0.18f, m.card))
            }
        }
    }

    @Test
    fun `an accent outline on a card is visible`() {
        // The chosen option in a warm-up is outlined in the accent, which is a
        // control boundary rather than text, so 3:1 is the bar.
        modes.forEach { m ->
            AccentPalette.entries.forEach { p ->
                assertReadable("${m.name}/${p.name}: accent outline on card", m.accent(p), m.card, LARGE)
            }
        }
    }

    @Test
    fun `a heavy fill is not a background anyone can read on`() {
        // The shipped bug, kept as a test so the reasoning is not lost: fill is
        // a translucent tint, and forcing its alpha up to make a slab leaves
        // the secondary label far below the floor. If this ever starts passing,
        // the palette changed and the warm-up block could use it again.
        modes.forEach { m ->
            val slab = over(m.fill, 0.6f, m.card)
            val r = ratio(m.secondary, slab)
            assertTrue(
                "${m.name}: fill at 0.6 now gives %.2f:1 for the secondary label".format(r),
                r < BODY,
            )
        }
    }
}
