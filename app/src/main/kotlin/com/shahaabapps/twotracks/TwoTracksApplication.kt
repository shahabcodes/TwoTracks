package com.shahaabapps.twotracks

import android.app.Application
import com.shahaabapps.twotracks.reminders.ReminderNotifications
import com.shahaabapps.twotracks.widget.WidgetRefresher
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TwoTracksApplication : Application() {

    /**
     * Started here rather than from a screen: the home-screen widgets should
     * keep up with the couple's data whenever the app process is alive, not
     * only while the Learn tab happens to be open.
     */
    @Inject lateinit var widgetRefresher: WidgetRefresher

    override fun onCreate() {
        super.onCreate()
        ReminderNotifications.ensureChannel(this)
        widgetRefresher.start()
    }
}
