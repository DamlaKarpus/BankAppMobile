package com.damlakarpus.bankappmobile.data.model

data class Account(
    val id: Int? = null,
    val iban: String? = null,
    val name: String? = null,
    val balance: Double? = null,
    val active: Boolean? = null,
    val userId: Int? = null,
    val userName: String? = null
)
