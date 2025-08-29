package com.damlakarpus.bankappmobile.data.repository

import com.damlakarpus.bankappmobile.api.RetrofitClient
import com.damlakarpus.bankappmobile.data.model.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AccountRepository {

    // Token ile kullanıcı hesaplarını çek
    suspend fun getAccounts(token: String): List<Account> = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.apiService.getAccounts()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    // IBAN ile hesap çek
    suspend fun getAccountByIban(iban: String, token: String): Account? = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.apiService.getAccountByIban(iban, "Bearer $token")
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }
    }
}
