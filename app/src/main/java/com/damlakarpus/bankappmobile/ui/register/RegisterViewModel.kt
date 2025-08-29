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

class RegisterViewModel : BaseViewModel() {

    private val api: ApiService = RetrofitClient.apiService

    fun registerUser(request: RegisterRequest): LiveData<Resource<RegisterResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                val response = api.register(request) // Retrofit suspend, direkt RegisterResponse
                val message = response.message.orEmpty()

                // Kayıt başarılıysa Success emit et
                if (response.success == true || message.contains("başarılı", ignoreCase = true)) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(message.ifEmpty { "Kayıt başarısız" }))
                }

            } catch (e: HttpException) {
                // 400 veya 500 durumunda bile response body varsa parse et
                val errorBody = e.response()?.errorBody()?.string().orEmpty()
                val errorResponse = try {
                    com.google.gson.Gson().fromJson(errorBody, RegisterResponse::class.java)
                } catch (_: Exception) {
                    null
                }

                if (errorResponse?.success == true) {
                    emit(Resource.Success(errorResponse))
                } else {
                    val msg = errorResponse?.message
                        ?: errorBody.ifEmpty { "HTTP ${e.code()}: ${e.message()}" }
                    emit(Resource.Error(msg))
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Bilinmeyen hata oluştu"))
            }
        }
    }
}
