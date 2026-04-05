package com.example.mobile_app.data.api
import com.example.mobile_app.data.model.Response.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz Retrofit que define los endpoints de la API de randomuser.me
 */
interface ApiService {

    /**
     *Obtiene una lista de usuarios aleatorios
     * @param result Numero de usuarios a obtener (default 10)
     * @param gender filtro de genero ya sea "male" o "female" o null para ambos
     * @return [RandomUserResponse] con la lista de usuarios
     */
    @GET("/api")
    suspend fun getUsers(
        @Query("results") result: Int=10,
        @Query("gender")gender: String? = null
    ): RandomUserResponse
}