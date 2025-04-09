package com.example.budgetbuddy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.databinding.ActivitySettingsBinding
import com.example.budgetbuddy.utils.DataManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedCurrency = DataManager.getCurrency(this)
        val savedBudget = DataManager.getBudget(this)

        binding.editCurrency.setText(savedCurrency)
        binding.editBudget.setText(if (savedBudget > 0) savedBudget.toString() else "")

        binding.saveSettingsButton.setOnClickListener {
            val currency = binding.editCurrency.text.toString()
            val budget = binding.editBudget.text.toString().toFloatOrNull()

            if (currency.isBlank() || budget == null) {
                Toast.makeText(this, "Please enter valid currency and budget.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DataManager.saveSettings(this, currency, budget)
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}