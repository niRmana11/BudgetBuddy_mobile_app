package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetbuddy.databinding.ActivityHomeBinding
import com.example.budgetbuddy.data.model.Transaction
import com.example.budgetbuddy.utils.DataManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addTransactionButton.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        binding.viewAnalysisButton.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.viewSettingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        transactions = DataManager.getTransactions(this).toMutableList()
        transactionAdapter = TransactionAdapter(transactions)
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = transactionAdapter
    }

    override fun onResume() {
        super.onResume()
        transactions.clear()
        transactions.addAll(DataManager.getTransactions(this))
        transactionAdapter.notifyDataSetChanged()
    }
}