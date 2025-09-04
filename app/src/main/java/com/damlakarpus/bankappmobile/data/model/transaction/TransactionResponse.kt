package com.damlakarpus.bankappmobile.data.model.transaction

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

    @SerializedName("transactions")
    val transactions: List<Transaction>? = null,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("transaction")
    val transaction: Transaction? = null  // İşlem detayları
)

data class Transaction(
    @SerializedName("id")
    val id: Long,

    @SerializedName("accountIban")
    val accountIban: String,

    @SerializedName("targetAccountIban")
    val targetAccountIban: String?,   // Hedef hesap IBAN

    @SerializedName("amount")
    val amount: Double,               // BigDecimal yerine Double

    @SerializedName("type")
    val type: String,                 // İşlem türü: DEPOSIT, WITHDRAW, TRANSFER

    @SerializedName("transactionTime")
    val transactionTime: String       // LocalDateTime yerine String (JSON timestamp)
)
