package com.shahaabapps.twotracks.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shahaabapps.twotracks.widget.WidgetStore
import com.shahaabapps.twotracks.widget.nextRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {
    @Inject lateinit var scheduler: ReminderScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty().ifBlank { "Two Tracks" }
        val text = intent.getStringExtra(EXTRA_TEXT).orEmpty()
        val pending = goAsync()
        scope.launch {
            // Where "Start studying" should land. The widgets' snapshot already
            // works out the next outstanding thing and is readable without a
            // signed-in session, which is exactly what a receiver has.
            val route = runCatching { WidgetStore.read(context).nextRoute() }.getOrNull()
            ReminderNotifications.show(context, title, text, route)
            scheduler.refresh()
            pending.finish()
        }
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_TEXT = "text"
    }
}

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var scheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        scheduler.refresh()
    }
}
