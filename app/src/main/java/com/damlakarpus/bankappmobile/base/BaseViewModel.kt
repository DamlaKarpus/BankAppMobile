package com.damlakarpus.bankappmobile.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.damlakarpus.bankappmobile.R
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
            // Hata mesajını strings.xml’den alıyoruz
            val defaultMessage = "unknown_error" // key
            emit(Resource.Error(e.message ?: defaultMessage))
        }
    }
}
