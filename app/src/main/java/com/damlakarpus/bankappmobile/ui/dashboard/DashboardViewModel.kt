package com.damlakarpus.bankappmobile.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.damlakarpus.bankappmobile.data.model.Account
import com.damlakarpus.bankappmobile.data.repository.AccountRepository
import com.damlakarpus.bankappmobile.common.SessionManager
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: AccountRepository = AccountRepository()
) : ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Token ile hesapları yükler.
     * Eğer token yoksa hata verir.
     * Alınan hesapların ilkini SessionManager’a kaydeder.
     */
    fun loadAccounts() {
        val token = SessionManager.token
        if (token.isNullOrBlank()) {
            _error.value = "Token bulunamadı, lütfen giriş yapın."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val accountList = repository.getAccounts("Bearer $token")

                if (accountList.isNullOrEmpty()) {
                    _error.value = "Hesap bulunamadı."
                    _accounts.value = emptyList()
                } else {
                    _accounts.value = accountList

                    // İlk hesabı SessionManager’a kaydet
                    accountList.firstOrNull()?.let { firstAccount ->
                        SessionManager.userId = firstAccount.userId
                        SessionManager.iban = firstAccount.iban
                        SessionManager.balance = firstAccount.balance
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Hesaplar yüklenemedi: ${e.localizedMessage ?: "Bilinmeyen hata"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
