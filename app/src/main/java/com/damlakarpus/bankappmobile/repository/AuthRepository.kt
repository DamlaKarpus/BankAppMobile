// AuthRepository.kt
package com.damlakarpus.bankappmobile.repository

import com.damlakarpus.bankappmobile.api.ApiService
import com.damlakarpus.bankappmobile.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.model.login.LoginResponse
import com.damlakarpus.bankappmobile.model.login.LoginRequest
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val api: ApiService) {

    // Login fonksiyonu - direkt API'yi çağırıyor
    suspend fun login(request: LoginRequest): LoginResponse {
        return withContext(Dispatchers.IO) {
            api.login(request)
        }
    }

    // Register fonksiyonu - direkt API'yi çağırıyor
    suspend fun register(request: RegisterRequest): RegisterResponse {
        return withContext(Dispatchers.IO) {
            api.register(request)
        }
    }
}
