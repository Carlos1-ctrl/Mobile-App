package com.example.mobile_app.data.model

/**
 * Ubicacion Completa del usuario
 */
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val coordinates: Coordinate
)