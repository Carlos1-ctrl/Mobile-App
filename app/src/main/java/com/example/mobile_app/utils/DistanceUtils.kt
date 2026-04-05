package com.example.mobile_app.utils

import kotlin.math.*

/**
 * calcula la distancia en kilometros entre dos coordenadas geograficas
 * usando la formula de Haversine
 * @param lat1 Latitud del punto 1 (dispositivo)
 * @param lon1 Longitud del punto 1 (dispositivo)
 * @param lat2 Latitud del punto 2 (usuario)
 * @param lon2 Longitud del punto 2 (usuario)
 * @return Distancia en Kilometros entre los dos puntos
 */
fun calculateDistanceKm(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
):Double {
    val R = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 *  atan2(sqrt(a), sqrt(1 -a))
    return  R * c
}