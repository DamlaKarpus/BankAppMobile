package com.damlakarpus.bankappmobile.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    @SerializedName("jwt")
    val token: String? = null, // Backend’den dönen JWT
    @SerializedName("userName") // Backend’de User entity’sindeki userName ile eşleşiyor
    val username: String? = null
)
