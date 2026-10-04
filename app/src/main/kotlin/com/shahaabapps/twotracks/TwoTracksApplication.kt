package com.shahaabapps.twotracks

import android.app.Application
import com.shahaabapps.twotracks.reminders.ReminderNotifications
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TwoTracksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ReminderNotifications.ensureChannel(this)
    }
}
