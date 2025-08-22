package com.damlakarpus.bankappmobile.api

import com.damlakarpus.bankappmobile.common.Constants
import com.damlakarpus.bankappmobile.common.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // AuthInterceptor direkt burada ekleniyor
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder()

                // Token varsa header ekle
                SessionManager.token?.let { token ->
                    builder.addHeader("Authorization", "Bearer $token")
                }

                val requestWithHeaders = builder.build()
                chain.proceed(requestWithHeaders)
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)  // Constants üzerinden alıyoruz
            .client(client)               // Token ekleyen client
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
