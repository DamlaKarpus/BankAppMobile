package com.damlakarpus.bankappmobile.api

import com.damlakarpus.bankappmobile.data.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.data.model.login.LoginResponse
import com.damlakarpus.bankappmobile.data.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.data.model.login.LoginRequest
import com.damlakarpus.bankappmobile.data.model.Account
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Kullanıcı kayıt
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    // Kullanıcı giriş
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // Kullanıcının tüm hesaplarını getir (token ile)
    @GET("accounts/me")
    suspend fun getAccounts(
    ): List<Account>

    // Tek hesap getir (IBAN üzerinden, token ile)
    @GET("accounts/iban/{iban}")
    suspend fun getAccountByIban(
        @Path("iban") iban: String,
        @Header("Authorization") bearerToken: String // "Bearer $token"
    ): Account
}
