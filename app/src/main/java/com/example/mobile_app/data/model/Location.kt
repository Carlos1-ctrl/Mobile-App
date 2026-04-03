package com.example.mobile_app.data.model

data class Location(
    val street: Street,
    val city: String,
    val country: String,
    val coordinates: Coordinate
)