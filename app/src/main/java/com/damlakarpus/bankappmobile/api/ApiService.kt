package com.damlakarpus.bankappmobile.api

import com.damlakarpus.bankappmobile.data.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.data.model.login.LoginResponse
import com.damlakarpus.bankappmobile.data.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.data.model.login.LoginRequest

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
