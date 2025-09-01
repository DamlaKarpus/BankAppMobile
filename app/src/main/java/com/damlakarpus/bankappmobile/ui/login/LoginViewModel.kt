package com.damlakarpus.bankappmobile.ui.login

import androidx.lifecycle.LiveData
import com.damlakarpus.bankappmobile.api.ApiService
import com.damlakarpus.bankappmobile.api.RetrofitClient
import com.damlakarpus.bankappmobile.base.BaseViewModel
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.login.LoginRequest
import com.damlakarpus.bankappmobile.data.model.login.LoginResponse
import com.damlakarpus.bankappmobile.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel() {

    private val api: ApiService = RetrofitClient.apiService

    fun loginUser(request: LoginRequest): LiveData<Resource<LoginResponse>> {
        return doRequest {
            val response = api.login(request)

            // Başarılı login sonrası token ve username'i SessionManager'a kaydet
            response.token?.let { token ->
                SessionManager.token = token
            }
            response.username?.let { username ->
                SessionManager.userName = username
            }

            response
        }
    }
}
