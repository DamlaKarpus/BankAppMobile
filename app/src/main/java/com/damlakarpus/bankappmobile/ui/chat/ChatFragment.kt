package com.damlakarpus.bankappmobile.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.chat.ChatMessage
import com.damlakarpus.bankappmobile.databinding.FragmentChatBinding
import java.text.NumberFormat
import java.util.Locale

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView setup
        chatAdapter = ChatAdapter(messages)
        binding.rvChatMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        // ✅ Eğer sohbet boşsa ilk bot mesajını ekle
        if (messages.isEmpty()) {
            addBotMessage(getString(R.string.chat_welcome))
        }

        // Gönder butonu
        binding.btnSend.setOnClickListener {
            val userText = binding.etMessage.text.toString().trim()
            if (userText.isNotEmpty()) {
                addUserMessage(userText)
                binding.etMessage.text.clear()
                handleUserInput(userText)
            }
        }
    }

    private fun addUserMessage(text: String) {
        val message = ChatMessage(text, isUser = true)
        chatAdapter.addMessage(message)
        scrollToBottom()
    }

    private fun addBotMessage(text: String) {
        val message = ChatMessage(text, isUser = false)
        chatAdapter.addMessage(message)
        scrollToBottom()
    }

    private fun addLoadingMessage(): ChatMessage {
        val message = ChatMessage(isLoading = true)
        chatAdapter.addMessage(message)
        scrollToBottom()
        return message
    }

    private fun removeMessage(message: ChatMessage) {
        messages.remove(message)
        chatAdapter.notifyDataSetChanged()
    }

    private fun scrollToBottom() {
        binding.rvChatMessages.post {
            if (messages.isNotEmpty()) {
                binding.rvChatMessages.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun handleUserInput(userText: String) {
        val input = userText.lowercase(Locale("tr", "TR"))

        when {
            listOf("para gönder", "gönder", "transfer", "para gonder", "para yolla")
                .any { input.contains(it) } -> {
                addBotMessage(getString(R.string.chat_transfer))
                showLoadingAndNavigate(R.id.action_chatFragment_to_transactionFragment)
            }

            listOf("işlem", "işlemler", "geçmiş", "hareket", "islem", "islemler")
                .any { input.contains(it) } -> {
                addBotMessage(getString(R.string.chat_history))
                showLoadingAndNavigate(R.id.action_chatFragment_to_allTransactionsFragment)
            }

            listOf("bakiye", "hesap", "param")
                .any { input.contains(it) } -> {
                val balance = SessionManager.balance ?: 0.0
                val formatted = NumberFormat
                    .getCurrencyInstance(Locale("tr", "TR"))
                    .format(balance)
                addBotMessage(getString(R.string.chat_balance, formatted))
            }

            listOf("iban", "hesap no")
                .any { input.contains(it) } -> {
                val iban = SessionManager.iban ?: getString(R.string.chat_no_iban)
                addBotMessage(getString(R.string.chat_iban, iban))
            }

            listOf("kayıt", "üye ol", "hesap aç")
                .any { input.contains(it) } -> {
                addBotMessage(getString(R.string.chat_register))
                showLoadingAndNavigate(R.id.registerFragment)
            }

            listOf("giriş", "login", "giris")
                .any { input.contains(it) } -> {
                addBotMessage(getString(R.string.chat_login))
                showLoadingAndNavigate(R.id.loginFragment)
            }

            else -> {
                addBotMessage(getString(R.string.chat_unknown))
            }
        }
    }


    private fun showLoadingAndNavigate(destinationId: Int) {
        val loadingMsg = addLoadingMessage()
        binding.rvChatMessages.postDelayed({
            removeMessage(loadingMsg)
            findNavController().navigate(destinationId)
        }, 2000) // 2 saniye bekle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
