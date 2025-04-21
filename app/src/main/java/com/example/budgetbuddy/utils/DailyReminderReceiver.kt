package com.example.budgetbuddy.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DailyReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.showBudgetWarning(context, "Don’t forget to record your expenses today!")
    }
}
