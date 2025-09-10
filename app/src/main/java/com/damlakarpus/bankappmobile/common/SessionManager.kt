package com.damlakarpus.bankappmobile.common

/**
 * SessionManager sadece uygulama açıkken RAM’de token ve kullanıcı bilgilerini tutar.
 * App kapandığında bilgiler silinir.
 */
object SessionManager {

    // JWT token
    var token: String? = null

    // Kullanıcı bilgileri
    var userId: Int? = null
    var iban: String? = null
    var balance: Double? = null
    var userName: String? = null

    /**
     * Session bilgilerini kaydet veya güncelle.
     * Null olmayan parametreler güncellenir.
     * userName sadece boş değilse güncellenir.
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
        if (!userName.isNullOrBlank()) {
            this.userName = userName
        }
    }

     //Oturum verilerini sıfırlar.
    fun clearSession() {
        token = null
        userId = null
        iban = null
        balance = null
        userName = null
    }
}
