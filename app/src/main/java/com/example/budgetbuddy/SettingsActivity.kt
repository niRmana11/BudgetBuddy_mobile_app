package com.example.budgetbuddy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.databinding.ActivitySettingsBinding
import com.example.budgetbuddy.utils.DataManager
import com.example.budgetbuddy.utils.FileHelper

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedCurrency = DataManager.getCurrency(this)


        binding.editCurrency.setText(savedCurrency)


        binding.saveSettingsButton.setOnClickListener {
            val currency = binding.editCurrency.text.toString()

            if (currency.isBlank()) {
                Toast.makeText(this, "Please enter a valid currency.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save only currency, keep the old budget
            val existingBudget = DataManager.getBudget(this)
            DataManager.saveSettings(this, currency, existingBudget)

            Toast.makeText(this, "Currency saved.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.buttonExportData.setOnClickListener {
            val transactions = DataManager.getTransactions(this)
            val success = FileHelper.exportData(this, transactions)
            if (success) {
                Toast.makeText(this, "Backup saved successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Backup failed.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonImportData.setOnClickListener {
            val restored = FileHelper.importData(this)
            if (restored != null) {
                DataManager.saveTransactions(this, restored)
                Toast.makeText(this, "Data restored successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No backup found or error restoring.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}