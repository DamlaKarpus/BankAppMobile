package com.damlakarpus.bankappmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.Transaction
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse
import com.damlakarpus.bankappmobile.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val repository = TransactionRepository()

    // Transfer sonucu
    private val _transactionResult = MutableLiveData<TransactionResponse>()
    val transactionResult: LiveData<TransactionResponse> get() = _transactionResult

    // Son 5 işlem
    private val _recentTransactions = MutableLiveData<List<Transaction>>()
    val recentTransactions: LiveData<List<Transaction>> get() = _recentTransactions

    // Tüm işlemler
    private val _allTransactions = MutableLiveData<List<Transaction>>()
    val allTransactions: LiveData<List<Transaction>> get() = _allTransactions

    // Yüklenme durumu
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Hata mesajı
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Para gönderme
    fun transfer(request: TransactionRequest) {
        safeLaunch {
            val response = repository.transfer(request)
            _transactionResult.value = response
        }
    }

    // Son 3 işlemi al
    fun fetchRecentTransactions(accountIban: String?) {
        accountIban ?: return

        safeLaunch {
            val response = repository.getTransactions(accountIban) // TransactionResponse
            val transactions = response.transactions.orEmpty()
            _recentTransactions.value = transactions
                .sortedByDescending { it.transactionTime }
                .take(3)
        }
    }

    // Tüm işlemleri al
    fun fetchAllTransactions(accountIban: String?) {
        val token = SessionManager.token
        if (accountIban.isNullOrEmpty() || token.isNullOrEmpty()) {
            _error.value = "IBAN veya token geçersiz!"
            return
        }

        safeLaunch {
            val response = repository.getTransactions(accountIban) // TransactionResponse
            val transactions = response.transactions.orEmpty()
            _allTransactions.value = transactions.sortedByDescending { it.transactionTime }
        }
    }

    // Ortak güvenli coroutine başlatıcı
    private fun safeLaunch(block: suspend () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                block()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Bilinmeyen bir hata oluştu"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
