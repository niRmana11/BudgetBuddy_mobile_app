package com.example.budgetbuddy

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy.data.model.Transaction
import com.example.budgetbuddy.databinding.ActivityAddTransactionBinding
import com.example.budgetbuddy.utils.DataManager
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.saveButton.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val amount = binding.editAmount.text.toString().toDoubleOrNull()
            val category = binding.editCategory.text.toString()

            if (title.isBlank() || amount == null || category.isBlank()) {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transactions = DataManager.getTransactions(this).toMutableList()
            val transaction = Transaction(
                id = System.currentTimeMillis(),
                title = title,
                amount = amount,
                category = category,
                date = selectedDate
            )
            transactions.add(transaction)
            DataManager.saveTransactions(this, transactions)

            Toast.makeText(this, "Transaction saved.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}