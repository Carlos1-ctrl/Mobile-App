package com.example.mobile_app.data.repository

import com.example.mobile_app.data.api.RetrofitInstance
import com.example.mobile_app.data.model.User

/**
 * Repositorio que actua como fuente unica de datos para los usuarios.
 * Abstrae la logica de acceso a la API del resto de la app
 */
class UserRepository {

    /**
     * Obtiene una Lista de 10 usuarios desde la Api.
     * @param gender es el Filtro de genero ya sea "male", "female" o null para ambos
     * @return Lista de User
     */
    suspend fun getUsers(gender: String?):List<User>{
        return RetrofitInstance.api.getUsers(10, gender).results
    }
}