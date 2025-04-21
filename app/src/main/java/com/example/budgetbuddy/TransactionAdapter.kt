package com.example.budgetbuddy

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbuddy.databinding.ActivityTransactionAdapterBinding
import com.example.budgetbuddy.data.model.Transaction
import com.example.budgetbuddy.utils.DataManager

class TransactionAdapter(
    private val transactions: MutableList<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ActivityTransactionAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ActivityTransactionAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.itemView.context
        val currency = DataManager.getCurrency(context) // 🔥 get currency set in settings

        with(holder.binding) {
            textTitle.text = transaction.title
            textAmount.text = "$currency %.2f".format(transaction.amount)
            textCategory.text = transaction.category
            textDate.text = transaction.date

            // ✏️ Edit
            buttonEdit.setOnClickListener {
                val intent = Intent(context, AddTransactionActivity::class.java)
                intent.putExtra("transaction_id", transaction.id)
                context.startActivity(intent)
            }

            // ❌ Delete
            buttonDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes") { _, _ ->
                        transactions.removeAt(position)
                        DataManager.saveTransactions(context, transactions)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Transaction deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }


    override fun getItemCount(): Int = transactions.size
}

