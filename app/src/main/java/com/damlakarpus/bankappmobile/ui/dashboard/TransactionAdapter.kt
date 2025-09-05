package com.damlakarpus.bankappmobile.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damlakarpus.bankappmobile.data.model.transaction.Transaction
import com.damlakarpus.bankappmobile.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DiffCallback()) {

    class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            // IBAN gösterimi
            binding.tvReceiverIban.text = if (!transaction.targetAccountIban.isNullOrEmpty()) {
                "Alıcı IBAN: ${transaction.targetAccountIban}"
            } else {
                "Gönderen IBAN: ${transaction.accountIban}"
            }

            // Tutar
            binding.tvAmount.text = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
                .format(transaction.amount)

            // Tarih formatlama (backend string → okunabilir format)
            val formattedDate = try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("tr"))
                val date = parser.parse(transaction.transactionTime)
                if (date != null) formatter.format(date) else transaction.transactionTime
            } catch (e: Exception) {
                transaction.transactionTime // hata olursa direkt geleni bas
            }

            binding.tvTimestamp.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }
}
