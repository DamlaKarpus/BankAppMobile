package com.damlakarpus.bankappmobile.common

/**
 * SessionManager sadece uygulama açıkken RAM’de token ve kullanıcı bilgilerini tutar.
 * App kapandığında bilgiler silinir.
 */
object SessionManager {

    // Sadece raw JWT token saklanacak
    var token: String? = null

    // Kullanıcı bilgileri
    var userId: Int? = null
    var iban: String? = null
    var balance: Double? = null
    var userName: String? = null

    /**
     * Session bilgilerini kaydet veya güncelle
     * Parametreler opsiyonel: null olmayanları günceller.
     * Not: userName sadece boşsa güncellenir, aksi halde sabit kalır.
     */
    fun saveSession(
        token: String? = null,
        userId: Int? = null,
        iban: String? = null,
        balance: Double? = null,
        userName: String? = null
    ) {
        token?.let { this.token = it }
        userId?.let { this.userId = it }
        iban?.let { this.iban = it }
        balance?.let { this.balance = it }
        userName?.let { this.userName = it }
    }

    /**
     * Session temizle
     */
    fun clearSession() {
        token = null
        userId = null
        iban = null
        balance = null
        userName = null
    }
}
