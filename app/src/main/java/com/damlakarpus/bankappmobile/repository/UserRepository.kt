package com.damlakarpus


import com.damlakarpus.bankappmobile.api.ApiService
import com.damlakarpus.bankappmobile.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.model.login.LoginRequest
import com.damlakarpus.bankappmobile.model.login.LoginResponse

class UserRepository(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }

    suspend fun register(user: RegisterRequest): RegisterResponse {
        return apiService.register(user)
    }
}
