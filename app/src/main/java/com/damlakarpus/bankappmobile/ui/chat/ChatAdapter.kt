package com.damlakarpus.bankappmobile.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.data.model.chat.ChatMessage
import com.damlakarpus.bankappmobile.databinding.ItemChatMessageBinding

class ChatAdapter(private val messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Mesaj tipleri
    private val TYPE_BOT = 0
    private val TYPE_USER = 1
    private val TYPE_LOADING = 2

    // Kullanıcı ve bot mesajı için ViewHolder
    inner class ChatMessageViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            if (chatMessage.isUser) {
                // Kullanıcı mesajı
                binding.tvUserMessage.text = chatMessage.message
                binding.tvUserMessage.visibility = View.VISIBLE
                binding.tvBotMessage.visibility = View.GONE
            } else {
                // Bot mesajı
                binding.tvBotMessage.text = chatMessage.message
                binding.tvBotMessage.visibility = View.VISIBLE
                binding.tvUserMessage.visibility = View.GONE
            }
        }
    }

    // Loading için ViewHolder
    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val lottie: LottieAnimationView = view.findViewById(R.id.lottieLoading)
        fun bind() {
            lottie.playAnimation()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            messages[position].isLoading -> TYPE_LOADING
            messages[position].isUser -> TYPE_USER
            else -> TYPE_BOT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message, parent, false)
                val binding = ItemChatMessageBinding.bind(view)
                ChatMessageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatMessageViewHolder -> holder.bind(messages[position])
            is LoadingViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun removeMessage(message: ChatMessage) {
        val index = messages.indexOf(message)
        if (index != -1) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
