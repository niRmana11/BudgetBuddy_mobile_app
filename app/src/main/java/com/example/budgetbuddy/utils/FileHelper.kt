package com.example.budgetbuddy.utils

import android.content.Context
import android.widget.Toast
import com.example.budgetbuddy.data.model.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

object FileHelper {
    private const val BACKUP_FILE_NAME = "transactions_backup.json"

    // Export to internal storage
    fun exportData(context: Context, transactions: List<Transaction>): Boolean {
        return try {
            val json = Gson().toJson(transactions)
            val file = File(context.filesDir, BACKUP_FILE_NAME)
            file.writeText(json)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Import from internal storage
    fun importData(context: Context): List<Transaction>? {
        return try {
            val file = File(context.filesDir, BACKUP_FILE_NAME)
            if (!file.exists()) return null

            val json = file.readText()
            val type = object : TypeToken<List<Transaction>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }
}
