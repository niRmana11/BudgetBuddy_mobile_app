package com.example.budgetbuddy.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.budgetbuddy.data.model.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataManager {
    private const val PREFS_NAME = "finance_prefs"
    private const val KEY_TRANSACTIONS = "transactions"
    private const val KEY_CURRENCY = "currency"
    private const val KEY_BUDGET = "monthly_budget"

    fun saveTransactions(context: Context, transactions: List<Transaction>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(transactions)
        editor.putString(KEY_TRANSACTIONS, json)
        editor.apply()
    }

    fun getTransactions(context: Context): List<Transaction> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TRANSACTIONS, null)
        val type = object : TypeToken<List<Transaction>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveSettings(context: Context, currency: String, budget: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CURRENCY, currency).putFloat(KEY_BUDGET, budget).apply()
    }

    fun getCurrency(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_CURRENCY, "LKR") ?: "LKR"

    fun getBudget(context: Context): Float =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getFloat(KEY_BUDGET, 0f)
}