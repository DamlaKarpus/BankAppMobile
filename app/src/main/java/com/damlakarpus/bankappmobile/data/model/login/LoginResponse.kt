package com.damlakarpus.bankappmobile.data.model.login

data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val token: String? = null // Başarılı login sonrası JWT veya token dönecek
)
