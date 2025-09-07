package com.damlakarpus.bankappmobile.data.model.chat

data class ChatMessage(
    val message: String? = null,
    val isUser: Boolean = false,
    val isLoading: Boolean = false
)

