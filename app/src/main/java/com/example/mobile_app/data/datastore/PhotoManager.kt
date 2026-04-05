package com.example.mobile_app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.photoDataStore by preferencesDataStore("user_photos")

class PhotoManager(private val context: Context){
    suspend fun savePhoto(email: String, uri: String){
        val key= stringPreferencesKey(email)
        context.photoDataStore.edit { prefs ->
            prefs[key]= uri
        }

        }
    fun getPhoto(email: String): Flow<String?>{
        val key = stringPreferencesKey(email)
        return context.photoDataStore.data.map { prefs ->
            prefs[key]
        }
    }
}