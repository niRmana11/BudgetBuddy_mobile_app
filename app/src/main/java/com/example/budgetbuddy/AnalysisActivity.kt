package com.example.budgetbuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.databinding.ActivityAnalysisBinding
import com.example.budgetbuddy.utils.DataManager

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactions = DataManager.getTransactions(this)
        val categoryMap = transactions.groupBy { it.category }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

        val summary = categoryMap.entries.joinToString("\n") { (cat, sum) -> "$cat: LKR %.2f".format(sum) }
        binding.analysisTextView.text = if (summary.isNotEmpty()) summary else "No data to analyze."
    }
}