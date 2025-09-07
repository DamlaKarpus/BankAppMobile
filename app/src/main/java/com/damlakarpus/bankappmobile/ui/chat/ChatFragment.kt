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
            addBotMessage("Merhaba 👋 Size nasıl yardımcı olabilirim?")
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
            listOf("para gönder", "gönder", "transfer", "para gonder", "para yolla").any { input.contains(it) } -> {
                addBotMessage("Tabii 💸 sizi para transferi ekranına yönlendiriyorum...")
                showLoadingAndNavigate(R.id.action_chatFragment_to_transactionFragment)
            }
            listOf("işlem", "işlemler", "geçmiş", "hareket", "islem", "islemler").any { input.contains(it) } -> {
                addBotMessage("Hemen 📑 son işlemlerinizi açıyorum...")
                showLoadingAndNavigate(R.id.action_chatFragment_to_allTransactionsFragment)
            }
            listOf("bakiye", "hesap", "param").any { input.contains(it) } -> {
                val balance = SessionManager.balance ?: 0.0
                addBotMessage("Şu anki bakiyeniz: $balance ₺")
            }
            listOf("iban", "hesap no").any { input.contains(it) } -> {
                val iban = SessionManager.iban ?: "Kayıtlı IBAN bulunamadı."
                addBotMessage("IBAN bilginiz: $iban\n👉 Üzerine uzun basarak kopyalayabilirsiniz.")
            }
            listOf("kayıt", "üye ol", "hesap aç").any { input.contains(it) } -> {
                addBotMessage("Sizi kayıt ekranına yönlendiriyorum 📝")
                showLoadingAndNavigate(R.id.registerFragment)
            }
            listOf("giriş", "login", "giris").any { input.contains(it) } -> {
                addBotMessage("Sizi giriş ekranına yönlendiriyorum 🔑")
                showLoadingAndNavigate(R.id.loginFragment)
            }
            else -> {
                addBotMessage("Bunu tam anlayamadım 🤔\n👉 'para gönder', 'işlemler', 'bakiye', 'iban', 'kayıt ol' veya 'giriş' diyebilirsiniz.")
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