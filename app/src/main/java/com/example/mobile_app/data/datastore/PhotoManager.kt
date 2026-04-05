package com.example.mobile_app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/** * Extencion de Context para acceder al DataStorage de favoritos*/
val Context.photoDataStore by preferencesDataStore("user_photos")

/**
 * Maneja la persistencia de fotos asociadas a usuarios.
 * Guarda la URI de la foto usando el email del usuario como llave,
 * de modo que cada usuario tiene su propia foto que persiste.
 * @param context el contexto de la aplicacion
 */
class PhotoManager(private val context: Context){

    /**
     * Guarda la URI de una foto asociada a un usuario.
     * @param email es el Email del usuario
     * @param uri URI de la foto como string, vacio para eliminar
     */
    suspend fun savePhoto(email: String, uri: String){
        val key= stringPreferencesKey(email)
        context.photoDataStore.edit { prefs ->
            prefs[key]= uri
        }
        }

    /**
     *Obtieene la URI de la foto de un usuario como Flow.
     * @param email es el Email del usuario
     * @return Flow con la Uri como string o null si no tiene foto
     */
    fun getPhoto(email: String): Flow<String?>{
        val key = stringPreferencesKey(email)
        return context.photoDataStore.data.map { prefs ->
            prefs[key]
        }
    }
}