package com.example.mobile_app.data.model.Response

import com.example.mobile_app.data.model.User

/**
 * Modelo que representa la respuesta de la API de randomuser.me.
 * Contiene la lista de usuarios obtenidos de la peticion.
 * @param results es la Lista de User retornada por la API
 */
data class RandomUserResponse (
    val results: List<User>
)