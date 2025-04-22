package com.example.budgetbuddy

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
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

        val backBtn = findViewById<ImageButton>(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }


        // Group only expenses
        val expenseTransactions = transactions.filter { it.amount < 0 }

        // Group by category and sum expenses
        val categoryMap = expenseTransactions.groupBy { it.category }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

        // Format summary lines
        val summaryList = categoryMap.entries.map { (cat, sum) ->
            "$cat - LKR %.2f".format(-sum)
        }

        // Show in ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, summaryList)
        binding.analysisListView.adapter = adapter

        if (summaryList.isEmpty()) {
            summaryList.plus("No data to analyze.")
        }

        val totalExpense = expenseTransactions.sumOf { it.amount }
        binding.totalTextView.text = "Total: LKR %.2f".format(-totalExpense)

    }
}
