package com.shahaabapps.twotracks

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.shahaabapps.core.ui.theme.AppLook

/**
 * The home-screen icon follows the look.
 *
 * Each look has its own launcher alias in the manifest, and exactly one is
 * enabled. Switching is done as the app leaves the screen, not when the look
 * is tapped in Settings: some launchers drop or redraw the icon when the
 * enabled entry changes, and a few close the app with it, which is harmless
 * once the app is already in the background and alarming while it is in use.
 */
object LauncherIcon {
    /** The alias that is the icon for [look], and the one that is not. */
    internal fun aliases(look: AppLook): Pair<String, String> = when (look) {
        AppLook.PARALLEL -> PARALLEL to DEPARTURES
        AppLook.DEPARTURES -> DEPARTURES to PARALLEL
    }

    /**
     * Enables [look]'s icon and disables the other, if that is not already so.
     * The new one is enabled first, so there is never a moment with no icon.
     */
    fun apply(context: Context, look: AppLook) {
        val pm = context.packageManager
        val (on, off) = aliases(look)
        val onName = ComponentName(context.packageName, on)
        val offName = ComponentName(context.packageName, off)
        if (pm.isEnabled(onName, defaultOn = on == PARALLEL) && !pm.isEnabled(offName, defaultOn = off == PARALLEL)) return
        runCatching {
            pm.setComponentEnabledSetting(onName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            pm.setComponentEnabledSetting(offName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        }
    }

    /** Whether a component is enabled, reading "default" as what the manifest says. */
    private fun PackageManager.isEnabled(name: ComponentName, defaultOn: Boolean): Boolean = when (getComponentEnabledSetting(name)) {
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> defaultOn
        else -> false
    }

    internal const val PARALLEL = "com.shahaabapps.twotracks.MainActivity"
    internal const val DEPARTURES = "com.shahaabapps.twotracks.DeparturesLauncher"
}
