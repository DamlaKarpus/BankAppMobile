package com.damlakarpus.bankappmobile.repository

import com.damlakarpus.bankappmobile.api.RetrofitClient
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse

class TransactionRepository {

    private val api = RetrofitClient.apiService


    // Para transferi
    suspend fun transfer(request: TransactionRequest): TransactionResponse {
        return api.transfer(request)
    }
}
