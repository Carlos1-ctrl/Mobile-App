package com.example.mobile_app.data.model

data class User(
    val gender: String,
    val email: String,
    val phone: String,
    val name: Name,
    val picture: Picture,
    val location: Location
)