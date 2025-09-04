package com.damlakarpus.bankappmobile.repository

import com.damlakarpus.bankappmobile.api.RetrofitClient
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.Transaction
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse

class TransactionRepository {

    private val api = RetrofitClient.apiService

    // Para transferi
    suspend fun transfer(request: TransactionRequest): TransactionResponse {
        return api.transfer(request)
    }

    // İşlem geçmişi
    suspend fun getTransactions(accountIban: String): TransactionResponse {
        val request = mapOf("accountIban" to accountIban)
        return api.getTransactionHistory(request)
    }


}
