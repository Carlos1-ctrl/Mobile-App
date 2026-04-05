package com.example.mobile_app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee la instancia del retrofit configurada
 * para consumir la API de randomuser.me.
 * usa lazy initialization para crearse solo cuando se necesita
 */
object RetrofitInstance {
    val api: ApiService by lazy{
    Retrofit.Builder()
        .baseUrl("https://randomuser.me/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
    }

}