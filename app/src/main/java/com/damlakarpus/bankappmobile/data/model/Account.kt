package com.damlakarpus.bankappmobile.data.model

data class Account(
    val id: Int,
    val iban: String,
    val name: String,
    val balance: Double,
    val active: Boolean,
    val userId: Int
)
