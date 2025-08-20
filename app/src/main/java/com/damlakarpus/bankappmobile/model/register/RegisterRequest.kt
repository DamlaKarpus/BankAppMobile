package com.damlakarpus.bankappmobile.model.register

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("userName")
    val userName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
