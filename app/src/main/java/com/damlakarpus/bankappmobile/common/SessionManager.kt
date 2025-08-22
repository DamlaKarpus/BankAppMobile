package com.damlakarpus.bankappmobile.common

object SessionManager {
    var token: String? = null
}

//Bu dosya sadece token’ı RAM’de tutacak.
//App kapanınca token otomatik silinir.