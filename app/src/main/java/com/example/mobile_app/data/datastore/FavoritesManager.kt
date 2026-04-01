package com.example.mobile_app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
val Context.dataStore by preferencesDataStore("favorites")

class FavoritesManager(private val context: Context) {

    private val KEY = stringSetPreferencesKey("fav_users")

    val favoritesFlow: Flow<Set<String>> =
        context.dataStore.data.map { it[KEY] ?: emptySet<String>() }

    suspend fun toggleFavorite(email: String){
        context.dataStore.edit {
            val current = it[KEY] ?: emptySet<String>()
            it[KEY] = if (current.contains(email)) {
                current - email
            } else {
                current + email
            }
        }

    }
}