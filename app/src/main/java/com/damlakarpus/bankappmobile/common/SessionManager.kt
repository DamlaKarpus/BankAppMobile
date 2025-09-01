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
     * Login sonrası session bilgilerini kaydet
     * @param token: Raw JWT string
     * @param userId: Kullanıcı ID
     * @param iban: Kullanıcının IBAN numarası
     * @param balance: Hesap bakiyesi
     * @param userName: kullanıcı adı
     */
    fun saveSession(token: String, userId: Int, iban: String, balance: Double, userName:String) {
        this.token = token
        this.userId = userId
        this.iban = iban
        this.balance = balance
        this.userName = userName
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
