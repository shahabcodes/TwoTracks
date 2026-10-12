package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.seed.SeedSchedule
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.changelog.ChangelogStore
import com.shahaabapps.twotracks.changelog.Release
import com.shahaabapps.twotracks.feature.settings.SettingsView
import com.shahaabapps.twotracks.feature.settings.voicesText
import com.shahaabapps.twotracks.feature.settings.weeklyHoursText

/** The prototype's Settings, for Usaid. */
internal object SettingsSamples {
    val blocks = SeedSchedule.availabilityFor(PersonRole.TECH).blocks

    fun settings() = SettingsView(
        name = "Usaid", isTech = true, profileLine = "The developer · Asia/Qatar", inviteCode = "7KQ2MX",
        partnerName = "Sanya", partnerIsTech = false,
        weeklyHours = weeklyHoursText(blocks.filter { it.kind == com.shahaabapps.core.model.BlockKind.STUDY }.sumOf { it.endMinutes - it.startMinutes }),
        startDate = "6 Oct 2026", fullScreen = true, theme = ThemeMode.SYSTEM, biometricAvailable = true, biometricEnabled = true,
        voices = voicesText(), widgets = "2 on your home screen", version = "0.77.0", tour = true,
    )

    fun releases(): List<Release> = ChangelogStore.parse(
        """
        ## [0.76.0] - 2026-10-12
        ### Changed
        - Together, your partner, countries, tests, documents and budget are the prototype's screens.

        ## [0.75.0] - 2026-10-12
        ### Changed
        - Levels, a level, a goal, the goal form and the fit check are the prototype's screens.

        ## [0.74.0] - 2026-10-12
        ### Changed
        - Learn, a course, a topic, a test and the word deck are the prototype's screens.

        ## [0.73.0] - 2026-10-12
        ### Changed
        - Today, your week and both weeks are the prototype's screens, and the app opens on Today.
        """.trimIndent(),
    )
}
