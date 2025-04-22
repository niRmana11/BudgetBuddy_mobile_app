package com.example.budgetbuddy.utils

import android.content.Context
import com.example.budgetbuddy.data.model.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataManager {
    private const val PREFS_NAME = "BudgetBuddyPrefs"
    private const val KEY_TRANSACTIONS = "transactions"
    private const val KEY_CURRENCY = "currency"
    private const val KEY_BUDGET = "monthlyBudget"

    // Save transaction list as JSON
    fun saveTransactions(context: Context, transactions: List<Transaction>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(transactions)
        prefs.edit().putString(KEY_TRANSACTIONS, json).apply()
    }

    // Retrieve transactions
    fun getTransactions(context: Context): List<Transaction> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TRANSACTIONS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Transaction>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Save currency and budget together
    fun saveSettings(context: Context, currency: String, budget: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_CURRENCY, currency)
            .putFloat(KEY_BUDGET, budget)
            .apply()
    }

    fun getCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, "LKR") ?: "LKR"
    }

    fun getBudget(context: Context): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_BUDGET, 0f)
    }
}
