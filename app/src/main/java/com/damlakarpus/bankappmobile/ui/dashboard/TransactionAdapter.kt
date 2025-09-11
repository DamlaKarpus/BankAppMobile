package com.damlakarpus.bankappmobile.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.data.model.transaction.Transaction
import com.damlakarpus.bankappmobile.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val currentIban: String // Oturumdaki kullanıcının IBAN’ı
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DiffCallback()) {

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tx: Transaction) {
            val context = binding.root.context
            val isOutgoing = tx.accountIban == currentIban // biz gönderdik mi?

            // Karşı tarafın IBAN’ı
            val counterpartyIban = when {
                tx.type.equals("TRANSFER", true) && isOutgoing -> tx.targetAccountIban
                tx.type.equals("TRANSFER", true) && !isOutgoing -> tx.accountIban
                else -> tx.accountIban ?: tx.targetAccountIban
            }

            // ROL METNİ strings.xml’den alınır
            val roleText = when {
                tx.type.equals("TRANSFER", true) && isOutgoing -> context.getString(R.string.role_receiver)   // "Alıcı"
                tx.type.equals("TRANSFER", true) && !isOutgoing -> context.getString(R.string.role_sender)    // "Gönderen"
                tx.type.equals("DEPOSIT", true) -> context.getString(R.string.role_deposit)                   // "Para Yatırma"
                tx.type.equals("WITHDRAW", true) -> context.getString(R.string.role_withdraw)                 // "Para Çekme"
                else -> context.getString(R.string.role_transaction)                                         // "İşlem"
            }

            // ÜST SATIR: Eğer isim varsa "Alıcı: Bahar Yılmaz", yoksa sadece rol ("Alıcı")
            binding.tvUserName.text = if (!tx.targetUserName.isNullOrBlank()) {
                "$roleText: ${tx.targetUserName}"
            } else {
                roleText
            }

            // ALT SATIR: IBAN
            binding.tvReceiverIban.text = counterpartyIban?.let {
                context.getString(R.string.iban_label, it)
            } ?: context.getString(R.string.iban_missing)

            // TUTAR
            val amountText = tx.amount?.let {
                NumberFormat.getCurrencyInstance(Locale("tr", "TR")).format(it)
            } ?: "-"
            binding.tvAmount.text = amountText

            // TARİH
            val formattedDate = try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("tr"))
                tx.transactionTime?.let { parser.parse(it)?.let(fmt::format) } ?: "-"
            } catch (_: Exception) {
                tx.transactionTime ?: "-"
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
