package com.damlakarpus.bankappmobile.viewmodel

import androidx.lifecycle.LiveData
import com.damlakarpus.bankappmobile.base.BaseViewModel
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.model.login.LoginResponse
import com.damlakarpus.bankappmobile.model.login.LoginRequest
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel() {

    // Login işlemi
    fun loginUser(request: LoginRequest): LiveData<Resource<LoginResponse>> {
        return doRequest {
            repository.login(request)
        }
    }

    // Register işlemi
    fun registerUser(request: RegisterRequest): LiveData<Resource<RegisterResponse>> {
        return doRequest {
            repository.register(request)
        }
    }
}