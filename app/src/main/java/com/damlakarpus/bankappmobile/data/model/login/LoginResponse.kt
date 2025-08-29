package com.damlakarpus.bankappmobile.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    @SerializedName("jwt")
    val token: String? = null // Başarılı login sonrası JWT veya token dönecek
)
