package com.example.mobile_app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.data.datastore.FavoritesManager
import com.example.mobile_app.data.model.User
import com.example.mobile_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: UserRepository,
    private val favManager: FavoritesManager
) : ViewModel() {

    var users = mutableStateListOf<User>()
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var genderFilter by mutableStateOf<String?>(null)
    var favorites by mutableStateOf(setOf<String>())

    private val seenUsersMap = mutableMapOf<String, User>()

    val favoriteUsers: List<User>
        get() = seenUsersMap.values.filter { favorites.contains(it.email) }

    init {
        loadUsers()

        viewModelScope.launch {
            favManager.favoritesFlow.collect {
                favorites = it
            }
        }

        // Cargar usuarios favoritos guardados en DataStore al iniciar
        viewModelScope.launch {
            favManager.favoriteUsersFlow.collect { savedFavUsers ->
                savedFavUsers.forEach { user ->
                    seenUsersMap[user.email] = user
                }
            }
        }
    }

    fun loadUsers(reset: Boolean = false) {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                val newUsers = repo.getUsers(genderFilter)

                if (reset) users.clear()
                users.addAll(newUsers)

                newUsers.forEach { user ->
                    seenUsersMap[user.email] = user
                }

            } catch (e: Exception) {
                error = "Error al cargar usuarios. Verifica tu conexión."
            } finally {
                loading = false
            }
        }
    }

    fun toggleFavorite(user: User) {
        seenUsersMap[user.email] = user
        viewModelScope.launch {
            favManager.toggleFavorite(user)
        }
    }

    fun deleteUser(user: User) {
        users.remove(user)
        seenUsersMap.remove(user.email)
    }

    fun filter(gender: String?) {
        genderFilter = gender
        loadUsers(true)
    }
}