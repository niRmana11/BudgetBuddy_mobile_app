package com.example.budgetbuddy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbuddy.databinding.ActivityTransactionAdapterBinding
import com.example.budgetbuddy.data.model.Transaction

class TransactionAdapter(private val transactions: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ActivityTransactionAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ActivityTransactionAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.binding.textTitle.text = transaction.title
        holder.binding.textAmount.text = "LKR %.2f".format(transaction.amount)
        holder.binding.textCategory.text = transaction.category
        holder.binding.textDate.text = transaction.date
    }

    override fun getItemCount(): Int = transactions.size
}
