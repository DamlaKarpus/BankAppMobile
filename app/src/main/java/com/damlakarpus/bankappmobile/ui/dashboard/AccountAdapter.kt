package com.damlakarpus.bankappmobile.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damlakarpus.bankappmobile.data.model.Account
import com.damlakarpus.bankappmobile.databinding.ItemAccountBinding
import java.text.NumberFormat
import java.util.*

class AccountAdapter : ListAdapter<Account, AccountAdapter.AccountViewHolder>(DiffCallback()) {

    class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            // Hesap ismini string resource üzerinden göster
            binding.tvAccountName.text = binding.root.context.getString(
                com.damlakarpus.bankappmobile.R.string.account_name,
                account.id
            )

            binding.tvIban.text = account.iban

            // Balance’ı para birimi formatıyla göster
            val formattedBalance = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
                .format(account.balance)
            binding.tvBalance.text = formattedBalance
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Account, newItem: Account) = oldItem == newItem
    }
}
