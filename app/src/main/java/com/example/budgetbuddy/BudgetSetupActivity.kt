package com.example.budgetbuddy

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.databinding.ActivityBudgetSetupBinding
import com.example.budgetbuddy.utils.DataManager
import com.example.budgetbuddy.utils.NotificationHelper

class BudgetSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentBudget = DataManager.getBudget(this)
        if (currentBudget > 0) {
            binding.editBudget.setText(currentBudget.toString())
        }

        binding.saveBudgetButton.setOnClickListener {
            val budget = binding.editBudget.text.toString().toFloatOrNull()
            if (budget == null || budget <= 0) {
                Toast.makeText(this, "Enter a valid budget amount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            DataManager.saveSettings(this, DataManager.getCurrency(this), budget)
            Toast.makeText(this, "Budget saved.", Toast.LENGTH_SHORT).show()
            showBudgetProgress()
        }

        showBudgetProgress()
    }

    private fun showBudgetProgress() {
        val budget = DataManager.getBudget(this)
        val transactions = DataManager.getTransactions(this)
        val totalExpense = transactions.filter { it.amount < 0 }.sumOf { it.amount }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val spent = -totalExpense
        binding.expenseSummary.text = "You have spent: LKR %.2f".format(spent)

        if (budget > 0) {
            val percentage = (spent / budget * 100).toInt()
            binding.budgetProgressBar.progress = percentage.coerceAtMost(100)

            when {
                percentage >= 100 -> {
                    binding.budgetWarning.text = "You have exceeded your budget!"
                    NotificationHelper.showBudgetWarning(this, "You have exceeded your budget limit.")
                }
                percentage >= 80 -> {
                    binding.budgetWarning.text = "You are close to exceeding your budget."
                    NotificationHelper.showBudgetWarning(this, "You're nearing your monthly budget limit.")
                }
                else -> {
                    binding.budgetWarning.text = ""
                }
            }
        }

    }
}
