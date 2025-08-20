package com.damlakarpus.bankappmobile.model.login

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null // Başarılı login sonrası JWT veya token dönecek
)
