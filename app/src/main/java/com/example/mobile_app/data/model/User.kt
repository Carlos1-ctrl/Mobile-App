package com.example.mobile_app.data.model

/**
 * Modelo principal que representa a un usuario de la API
 */
data class User(
    val gender: String,
    val email: String,
    val phone: String,
    val name: Name,
    val picture: Picture,
    val location: Location,
    val login: Login
)