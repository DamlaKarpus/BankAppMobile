package com.damlakarpus.bankappmobile.data.model.register

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("userName")
    val userName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("password")
    val password: String? = null
)
