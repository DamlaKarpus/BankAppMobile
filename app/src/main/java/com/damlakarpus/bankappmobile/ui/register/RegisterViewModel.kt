package com.damlakarpus.bankappmobile.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.damlakarpus.bankappmobile.api.ApiService
import com.damlakarpus.bankappmobile.api.RetrofitClient
import com.damlakarpus.bankappmobile.base.BaseViewModel
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.data.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.data.model.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import com.google.gson.Gson

class RegisterViewModel : BaseViewModel() {

    private val api: ApiService = RetrofitClient.apiService

    fun registerUser(request: RegisterRequest): LiveData<Resource<RegisterResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                val response = api.register(request) // Retrofit suspend -> RegisterResponse
                val message = response.message.orEmpty()

                if (response.success == true) {
                    emit(Resource.Success(response))
                } else {
                    // Boş mesaj varsa generic hata döndür
                    emit(Resource.Error(message.ifEmpty { "register_failed" }))
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string().orEmpty()
                val errorResponse = try {
                    Gson().fromJson(errorBody, RegisterResponse::class.java)
                } catch (_: Exception) {
                    null
                }

                if (errorResponse?.success == true) {
                    emit(Resource.Success(errorResponse))
                } else {
                    val msg = errorResponse?.message
                        ?: errorBody.ifEmpty { "http_${e.code()}" }
                    emit(Resource.Error(msg))
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "unknown_error"))
            }
        }
    }
}
