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
    val id: Long? = null,  // id bazen olmayabilir diye nullable yaptım

    @SerializedName("accountIban")
    val accountIban: String? = null,

    @SerializedName("targetAccountIban")
    val targetAccountIban: String? = null,   // Hedef hesap IBAN

    @SerializedName("amount")
    val amount: Double? = null,               // BigDecimal yerine Double

    @SerializedName("type")
    val type: String? = null,                 // İşlem türü: DEPOSIT, WITHDRAW, TRANSFER

    @SerializedName("transactionTime")
    val transactionTime: String? = null,      // LocalDateTime yerine String (JSON timestamp)

    @SerializedName("targetUserName")
    val targetUserName: String? = null
)
