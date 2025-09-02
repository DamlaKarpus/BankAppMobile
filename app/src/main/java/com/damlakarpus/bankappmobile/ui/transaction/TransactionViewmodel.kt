package com.damlakarpus.bankappmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse
import com.damlakarpus.bankappmobile.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val repository = TransactionRepository()

    private val _transactionResult = MutableLiveData<TransactionResponse>()
    val transactionResult: LiveData<TransactionResponse> get() = _transactionResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun transfer(request: TransactionRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.transfer(request)
                _transactionResult.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
