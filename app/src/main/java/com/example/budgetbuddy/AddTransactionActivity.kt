package com.example.budgetbuddy

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.data.model.Transaction
import com.example.budgetbuddy.databinding.ActivityAddTransactionBinding
import com.example.budgetbuddy.utils.DataManager
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private var selectedDate: String = ""
    private var editingTransactionId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)




        setContentView(binding.root)

        // Toggle category visibility based on selected type
        binding.radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioIncome) {
                binding.layoutCategory.visibility = View.GONE
            } else {
                binding.layoutCategory.visibility = View.VISIBLE
            }
        }

        // 🔥 Trigger the visibility manually based on the default selection
        val selectedId = binding.radioGroupType.checkedRadioButtonId
        if (selectedId == R.id.radioIncome) {
            binding.layoutCategory.visibility = View.GONE
        } else {
            binding.layoutCategory.visibility = View.VISIBLE
        }


        // Setup Spinner with categories
        val categories = listOf("Food", "Transport", "Bills", "Entertainment", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Default Date
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectedDate = sdf.format(Date())
        binding.editDate.setText(selectedDate)

        binding.editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val pickedDate = Calendar.getInstance()
                    pickedDate.set(year, month, dayOfMonth)
                    selectedDate = sdf.format(pickedDate.time)
                    binding.editDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Check if launched for editing
        val passedId = intent.getLongExtra("transaction_id", -1)
        if (passedId != -1L) {
            editingTransactionId = passedId
            val existing = DataManager.getTransactions(this).find { it.id == passedId }
            existing?.let {
                binding.editTitle.setText(it.title)
                binding.editAmount.setText(it.amount.absoluteValue.toString()) // show positive value
                selectedDate = it.date
                binding.editDate.setText(selectedDate)
                binding.radioIncome.isChecked = it.amount >= 0
                binding.radioExpense.isChecked = it.amount < 0
                binding.layoutCategory.visibility = if (it.amount < 0) View.VISIBLE else View.GONE
                binding.spinnerCategory.setSelection(getCategoryIndex(it.category))
                Toast.makeText(this, "Editing transaction...", Toast.LENGTH_SHORT).show()
            }
        }


        binding.saveButton.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val rawAmount = binding.editAmount.text.toString().toDoubleOrNull()
            val category = binding.spinnerCategory.selectedItem.toString()
            val isIncome = binding.radioIncome.isChecked

            if (title.isBlank() || rawAmount == null) {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert amount based on type
            val amount = if (isIncome) rawAmount else -rawAmount

            val transactions = DataManager.getTransactions(this).toMutableList()
            // If editing, remove the old one
            editingTransactionId?.let {
                transactions.removeIf { t -> t.id == it }
            }

            val transaction = Transaction(
                id = editingTransactionId ?: System.currentTimeMillis(),
                title = title,
                amount = amount,
                category = category,
                date = selectedDate
            )

            transactions.add(transaction)
            DataManager.saveTransactions(this, transactions)

            Toast.makeText(this, if (editingTransactionId != null) "Transaction updated." else "Transaction saved.", Toast.LENGTH_SHORT).show()
            finish()

            DataManager.saveTransactions(this, transactions)

            Toast.makeText(this, "Transaction saved.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun getCategoryIndex(category: String): Int {
        val categories = listOf("Food", "Transport", "Bills", "Entertainment", "Others")
        return categories.indexOf(category).coerceAtLeast(0)
    }


}