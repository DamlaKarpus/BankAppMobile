package com.damlakarpus.bankappmobile.viewmodel

import androidx.lifecycle.LiveData
import com.damlakarpus.bankappmobile.base.BaseViewModel
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.model.register.RegisterResponse
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.api.ApiService
import com.damlakarpus.bankappmobile.api.RetrofitClient

class RegisterViewModel : BaseViewModel() {

    private val api: ApiService = RetrofitClient.apiService

    fun registerUser(request: RegisterRequest): LiveData<Resource<RegisterResponse>> {
        return doRequest {
            api.register(request)
        }
    }
}

