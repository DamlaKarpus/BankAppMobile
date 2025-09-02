package com.damlakarpus.bankappmobile.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TransactionRequest(
    @SerializedName("accountIban")
    val accountIban: String,         // İşlemi yapan hesap IBAN (sender)

    @SerializedName("targetAccountIban")
    val targetAccountIban: String? = null,   // Hedef hesap IBAN (receiver)

    @SerializedName("amount")
    val amount: BigDecimal            // Gönderilecek tutar
)
