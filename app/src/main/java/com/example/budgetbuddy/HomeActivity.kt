package com.example.budgetbuddy

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetbuddy.databinding.ActivityHomeBinding
import com.example.budgetbuddy.data.model.Transaction
import com.example.budgetbuddy.utils.DailyReminderReceiver
import com.example.budgetbuddy.utils.DataManager
import com.example.budgetbuddy.utils.NotificationHelper
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scheduleDailyReminder()

        binding.addTransactionButton.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        binding.viewAnalysisButton.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.viewBudgetSetupButton.setOnClickListener{
            startActivity(Intent(this, BudgetSetupActivity::class.java))
        }

        binding.viewSettingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        transactions = DataManager.getTransactions(this).toMutableList()
        transactionAdapter = TransactionAdapter(transactions)
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = transactionAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

    }




    private fun checkBudgetAndNotify() {
        val budget = DataManager.getBudget(this)
        if (budget <= 0f) return

        val totalSpent = DataManager.getTransactions(this)
            .filter { it.amount < 0 }
            .sumOf { it.amount }

        val spent = -totalSpent
        val percent = (spent / budget * 100).toInt()

        if (percent >= 100) {
            NotificationHelper.showBudgetWarning(this, "You have exceeded your budget!")
        } else if (percent >= 80) {
            NotificationHelper.showBudgetWarning(this, "You're nearing your monthly budget.")
        }
    }


    override fun onResume() {
        super.onResume()
        transactions.clear()
        transactions.addAll(DataManager.getTransactions(this))
        transactionAdapter.notifyDataSetChanged()


        checkBudgetAndNotify()
    }


    private fun scheduleDailyReminder() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(this, DailyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        alarmManager.setRepeating(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            android.app.AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}