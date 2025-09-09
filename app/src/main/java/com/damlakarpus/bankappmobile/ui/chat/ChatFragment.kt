package com.damlakarpus.bankappmobile.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
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

        // Eğer sohbet boşsa ilk bot mesajını ekle
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

    private fun containsKeyword(input: String, keywordArrayId: Int): Boolean {
        val keywords = resources.getStringArray(keywordArrayId)
        return keywords.any { input.contains(it) }
    }

    private fun handleUserInput(userText: String) {
        val input = userText.lowercase(Locale("tr", "TR"))

        when {
            containsKeyword(input, R.array.keywords_transfer) -> {
                addBotMessage(getString(R.string.chat_transfer))
                showLoadingAndNavigate(R.id.action_chatFragment_to_transactionFragment)
            }
            containsKeyword(input, R.array.keywords_history) -> {
                addBotMessage(getString(R.string.chat_history))
                showLoadingAndNavigate(R.id.action_chatFragment_to_allTransactionsFragment)
            }
            containsKeyword(input, R.array.keywords_balance) -> {
                val balance = SessionManager.balance ?: 0.0
                val formatted = NumberFormat
                    .getCurrencyInstance(Locale("tr", "TR"))
                    .format(balance)
                addBotMessage(getString(R.string.chat_balance, formatted))
            }
            containsKeyword(input, R.array.keywords_iban) -> {
                val iban = SessionManager.iban ?: getString(R.string.chat_no_iban)
                addBotMessage(getString(R.string.chat_iban, iban))
            }
            containsKeyword(input, R.array.keywords_register) -> {
                // ✅ Oturumu temizle ve back stack'ten ChatFragment'i kaldır
                SessionManager.clearSession()
                addBotMessage(getString(R.string.chat_register))

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.chatFragment, true) // ChatFragment dahil önceki stack temizlenir
                    .build()

                showLoadingAndNavigate(R.id.registerFragment, navOptions)
            }
            containsKeyword(input, R.array.keywords_login) -> {
                // ✅ Oturumu temizle ve back stack'ten ChatFragment'i kaldır
                SessionManager.clearSession()
                addBotMessage(getString(R.string.chat_login))

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.chatFragment, true)
                    .build()

                showLoadingAndNavigate(R.id.loginFragment, navOptions)
            }
            else -> {
                addBotMessage(getString(R.string.chat_unknown))
            }
        }
    }

    private fun showLoadingAndNavigate(destinationId: Int, navOptions: NavOptions? = null) {
        val loadingMsg = addLoadingMessage()
        binding.rvChatMessages.postDelayed({
            removeMessage(loadingMsg)
            if (navOptions != null) {
                findNavController().navigate(destinationId, null, navOptions)
            } else {
                findNavController().navigate(destinationId)
            }
        }, 2000) // 2 saniye bekle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
