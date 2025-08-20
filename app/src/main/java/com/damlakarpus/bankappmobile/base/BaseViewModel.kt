package com.damlakarpus.bankappmobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

// Resource sınıfı: Loading, Success, Error durumlarını yönetmek için
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

// BaseViewModel: Tüm ViewModel'lerde ortak kullanılacak method
open class BaseViewModel : ViewModel() {

    // Retrofit isteklerini LiveData ile dinlemek için yardımcı fonksiyon
    fun <T> doRequest(request: suspend () -> T) = liveData(Dispatchers.IO) {
        emit(Resource.Loading()) // Başlangıçta loading
        try {
            val result = request()
            emit(Resource.Success(result)) // Başarılı sonuç
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Bilinmeyen Hata")) // Hata durumunda
        }
    }
}
