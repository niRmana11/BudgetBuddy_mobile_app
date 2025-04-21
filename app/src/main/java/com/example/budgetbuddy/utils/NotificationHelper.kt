package com.example.budgetbuddy.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.budgetbuddy.R

object NotificationHelper {
    private const val CHANNEL_ID = "budget_channel"
    private const val CHANNEL_NAME = "Budget Alerts"

    fun showBudgetWarning(context: Context, message: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 🔕 Step 1: Create notification channel without sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null, null) // 🔕 Disable sound completely
                enableVibration(true) // optional
            }
            manager.createNotificationChannel(channel)
        }

        // 🔔 Step 2: Build and show the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Budget Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }

}
