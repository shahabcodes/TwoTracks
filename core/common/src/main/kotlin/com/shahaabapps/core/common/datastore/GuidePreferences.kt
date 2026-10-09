package com.shahaabapps.core.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.guideDataStore: DataStore<Preferences> by preferencesDataStore(name = "guide_prefs")

/**
 * Whether the tour is owed to whoever has just signed in.
 *
 * Set when a sign-in succeeds and cleared when the tour ends, so the tour
 * runs once per sign-in rather than once ever or once per launch. Reopening
 * the app does not sign anyone in, so it does not bring the tour back; signing
 * out and in again does, which is the point — the second person to use the
 * space has never seen it.
 *
 * It is on the device rather than the couple's data on purpose. It describes
 * this phone's screens, two people share one space, and a tour owed to one of
 * them should not be marked as seen because the other watched it.
 */
@Singleton
class GuidePreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val pending: Flow<Boolean> = context.guideDataStore.data.map { it[KEY_PENDING] ?: false }

    suspend fun setPending(value: Boolean) {
        context.guideDataStore.edit { prefs -> prefs[KEY_PENDING] = value }
    }

    private companion object {
        val KEY_PENDING = booleanPreferencesKey("tour_pending")
    }
}
