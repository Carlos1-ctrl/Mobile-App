package com.example.mobile_app.data.api
import com.example.mobile_app.data.model.Response.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/api")
    suspend fun getUsers(
        @Query("results") result: Int=10,
        @Query("gender")gender: String? = null
    ): RandomUserResponse
}