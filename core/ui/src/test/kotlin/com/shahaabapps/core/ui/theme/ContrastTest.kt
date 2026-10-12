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
 *
 * The palette is Parallel's: ink on a white page, with colour kept for the
 * two people's tracks. There is one palette now, in light and dark, so every
 * pair below is measured once per mode.
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

    private val modes = listOf("light" to parallelColors(dark = false), "dark" to parallelColors(dark = true))

    /** The colours the app writes text in besides ink, by name. */
    private fun inks(c: CupertinoColors) = listOf(
        "accentText" to c.accentText,
        "tech" to c.tech,
        "teacher" to c.teacher,
        "success" to c.success,
        "warn" to c.warn,
        "destructive" to c.destructive,
    )

    // ---------------------------------------------------------------- the pairs

    @Test
    fun `text on a card and on the page is readable`() {
        modes.forEach { (m, c) ->
            assertReadable("$m: label on card", c.ink, c.card)
            assertReadable("$m: label on page", c.ink, c.groupedBackground)
            assertReadable("$m: secondary label on card", c.secondaryLabel, c.card)
            assertReadable("$m: secondary label on page", c.secondaryLabel, c.groupedBackground)
        }
    }

    @Test
    fun `text on the translucent fill is readable`() {
        // fill is the idle-pill tint: segmented tracks, idle chips and the
        // option rows of a warm-up all sit on it.
        modes.forEach { (m, c) ->
            val onCard = fillOver(c.fill, c.card)
            val onPage = fillOver(c.fill, c.groupedBackground)
            assertReadable("$m: label on fill over card", c.ink, onCard)
            assertReadable("$m: label on fill over page", c.ink, onPage)
            assertReadable("$m: secondary label on fill over card", c.secondaryLabel, onCard)
            assertReadable("$m: secondary label on fill over page", c.secondaryLabel, onPage)
        }
    }

    @Test
    fun `every coloured ink reads on the card, the page and the fill`() {
        // Links and text actions are accentText; a person's name, times and
        // bars are their track colour; results and warnings are the status
        // colours. All of them are written straight onto these surfaces.
        modes.forEach { (m, c) ->
            inks(c).forEach { (name, ink) ->
                assertReadable("$m: $name on card", ink, c.card)
                assertReadable("$m: $name on page", ink, c.groupedBackground)
                assertReadable("$m: $name on fill over card", ink, fillOver(c.fill, c.card))
                assertReadable("$m: $name on fill over page", ink, fillOver(c.fill, c.groupedBackground))
            }
        }
    }

    @Test
    fun `a status chip can read its own label`() {
        // StatusChip writes its colour on a 6% tint of the same colour. The
        // teacher's orange is the tightest pair, and at 8% it fell under the
        // floor, which is why the chip uses 6%.
        modes.forEach { (m, c) ->
            inks(c).forEach { (name, ink) ->
                assertReadable("$m: $name chip over card", ink, over(ink, 0.06f, c.card))
                assertReadable("$m: $name chip over page", ink, over(ink, 0.06f, c.groupedBackground))
            }
        }
    }

    @Test
    fun `a day on the calendar keeps its date readable`() {
        // The calendar tints a day with the accent when it was studied and
        // with warn when it was missed, and writes the date on top in the
        // ordinary ink.
        modes.forEach { (m, c) ->
            assertReadable("$m: date on a missed day", c.ink, over(c.warn, 0.28f, c.card))
            assertReadable("$m: date on a studied day", c.ink, over(c.accent, 0.28f, c.card))
            assertReadable("$m: date on the developer's day", c.ink, over(c.tech, 0.28f, c.card))
            assertReadable("$m: date on the teacher's day", c.ink, over(c.teacher, 0.28f, c.card))
        }
    }

    @Test
    fun `a status tint keeps its text readable`() {
        // The check, warm-up and faded blocks tint a row with a status colour
        // at low alpha and keep the ordinary ink on top of it.
        modes.forEach { (m, c) ->
            listOf("success" to c.success, "warn" to c.warn, "destructive" to c.destructive).forEach { (n, s) ->
                listOf(0.15f, 0.18f).forEach { a ->
                    assertReadable("$m: label on $n at $a over card", c.ink, over(s, a, c.card))
                }
            }
        }
    }

    @Test
    fun `an accent or track tint keeps its text readable`() {
        // 0.08 is the check and faded blocks, which put both the ink and the
        // secondary label on the tint. 0.12 is the recap block and a diagram's
        // second tone, which hold prose. 0.18 is a marked row in a warm-up.
        modes.forEach { (m, c) ->
            listOf("accent" to c.accent, "tech" to c.tech, "teacher" to c.teacher).forEach { (n, tint) ->
                val light = over(tint, 0.08f, c.card)
                assertReadable("$m: label on $n at 0.08", c.ink, light)
                assertReadable("$m: secondary on $n at 0.08", c.secondaryLabel, light)
                assertReadable("$m: label on $n at 0.12", c.ink, over(tint, 0.12f, c.card))
                assertReadable("$m: label on $n at 0.18", c.ink, over(tint, 0.18f, c.card))
            }
        }
    }

    @Test
    fun `the link colour is readable wherever it is written`() {
        // "Quick check" and "In your own words" labels, an idle chip, a
        // callout and a block's times are all the link colour as ink, so they
        // use accentText, and every surface they land on is here: the tints
        // are 0.08 for a check or faded block, 0.10 for a day's block row,
        // 0.12 for a callout, 0.14 for a status chip.
        modes.forEach { (m, c) ->
            val surfaces = buildList {
                add("card" to c.card)
                add("page" to c.groupedBackground)
                listOf(0.08f, 0.10f, 0.12f, 0.14f).forEach { a ->
                    add("accent at $a over card" to over(c.accent, a, c.card))
                    add("accent at $a over page" to over(c.accent, a, c.groupedBackground))
                    add("its own tint at $a over card" to over(c.accentText, a, c.card))
                    add("its own tint at $a over page" to over(c.accentText, a, c.groupedBackground))
                }
            }
            surfaces.forEach { (name, bg) -> assertReadable("$m: accentText on $name", c.accentText, bg) }
        }
    }

    @Test
    fun `anything filled solid carries its own ink`() {
        // A primary button, a chosen chip, a ladder node and a numbered step
        // are all a colour filled in with something written on it. In light
        // the fill is ink and the writing white; in dark the fill is the pale
        // ink and the writing near-black. Buttons, chips and single bold
        // figures, so 3:1 is the bar. Prose on a solid fill is not allowed.
        modes.forEach { (m, c) ->
            listOf(
                "accent" to c.accent, "tech" to c.tech, "teacher" to c.teacher,
                "success" to c.success, "warn" to c.warn, "destructive" to c.destructive,
            ).forEach { (name, bg) ->
                assertReadable("$m: onSolid on $name", c.onSolid, bg, LARGE)
            }
        }
    }

    @Test
    fun `an outline or rule is visible`() {
        // A chosen option and the card that is the thing to do next are
        // outlined in ink, and every section opens with an ink rule. Those are
        // boundaries rather than text, so 3:1 is the bar. Hairlines between
        // rows are decoration and are not held to it.
        modes.forEach { (m, c) ->
            assertReadable("$m: accent outline on card", c.accent, c.card, LARGE)
            assertReadable("$m: ink rule on page", c.ink, c.groupedBackground, LARGE)
            assertReadable("$m: tech outline on card", c.tech, c.card, LARGE)
            assertReadable("$m: teacher outline on card", c.teacher, c.card, LARGE)
        }
    }

    @Test
    fun `a heavy fill is not a background anyone can read on`() {
        // The shipped bug, kept as a test so the reasoning is not lost: fill is
        // a translucent tint, and forcing its alpha up to make a slab leaves
        // the secondary label far below the floor. If this ever starts passing,
        // the palette changed and the warm-up block could use it again.
        modes.forEach { (m, c) ->
            val slab = over(c.fill, 0.6f, c.card)
            val r = ratio(c.secondaryLabel, slab)
            assertTrue("$m: fill at 0.6 now gives %.2f:1 for the secondary label".format(r), r < BODY)
        }
    }
}
