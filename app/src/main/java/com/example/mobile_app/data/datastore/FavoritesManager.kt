package com.example.mobile_app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.mobile_app.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("favorites")

class FavoritesManager(private val context: Context) {

    private val gson = Gson()
    private val EMAILS_KEY = stringSetPreferencesKey("fav_emails")
    private val USERS_KEY = stringPreferencesKey("fav_users_json")

    /** Flow de emails favoritos */
    val favoritesFlow: Flow<Set<String>> =
        context.dataStore.data.map { it[EMAILS_KEY] ?: emptySet() }

    /** Flow de usuarios favoritos completos */
    val favoriteUsersFlow: Flow<List<User>> =
        context.dataStore.data.map { prefs ->
            val json = prefs[USERS_KEY] ?: return@map emptyList()
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        }

    suspend fun toggleFavorite(user: User) {
        context.dataStore.edit { prefs ->
            // Actualizar emails
            val currentEmails = prefs[EMAILS_KEY] ?: emptySet()
            prefs[EMAILS_KEY] = if (currentEmails.contains(user.email)) {
                currentEmails - user.email
            } else {
                currentEmails + user.email
            }

            val json = prefs[USERS_KEY] ?: "[]"
            val type = object : TypeToken<MutableList<User>>() {}.type
            val currentUsers: MutableList<User> = gson.fromJson(json, type) ?: mutableListOf()

            if (currentUsers.any { it.email == user.email }) {
                currentUsers.removeAll { it.email == user.email }
            } else {
                currentUsers.add(user)
            }

            prefs[USERS_KEY] = gson.toJson(currentUsers)
        }
    }
}