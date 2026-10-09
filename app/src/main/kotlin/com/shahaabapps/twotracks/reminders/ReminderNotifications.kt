package com.shahaabapps.twotracks.reminders

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shahaabapps.twotracks.R

object ReminderNotifications {
    const val CHANNEL_ID = "study_reminders"
    private const val NOTIFICATION_ID = 1001

    fun ensureChannel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_reminders),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply { description = "Fires at the start of each planned study slot" }
        manager.createNotificationChannel(channel)
    }

    fun canPost(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    /**
     * Whether a reminder is allowed to take over the screen.
     *
     * Declaring the permission is enough up to Android 13. From 14 the system
     * grants it on install only to apps whose whole purpose is calls or
     * alarms, and a study app is not one of those, so it has to be asked for
     * and can be taken away again at any time.
     */
    fun canTakeOverScreen(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return true
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return runCatching { manager.canUseFullScreenIntent() }.getOrDefault(false)
    }

    /** The system page where the person grants or revokes that. */
    fun fullScreenSettingsIntent(context: Context): Intent? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return null
        return Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
            .setData(android.net.Uri.fromParts("package", context.packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * Posts a reminder.
     *
     * The same screen is attached twice: as the full-screen intent, which the
     * system opens by itself where that has been allowed, and as the tap
     * target, which works everywhere. Where the permission has not been given
     * the system quietly ignores the first and the notification behaves as a
     * heads-up one, so a reminder is never lost to a missing permission — it
     * just arrives more politely.
     */
    fun show(context: Context, title: String, text: String, route: String? = null) {
        if (!canPost(context)) return
        val screen = Intent(context, StudyReminderActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(StudyReminderActivity.EXTRA_TITLE, title)
            putExtra(StudyReminderActivity.EXTRA_TEXT, text)
            route?.let { putExtra(StudyReminderActivity.EXTRA_ROUTE, it) }
        }
        val open = PendingIntent.getActivity(
            context, 0, screen,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(open)
            .setFullScreenIntent(open, true)
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification) }
    }
}
