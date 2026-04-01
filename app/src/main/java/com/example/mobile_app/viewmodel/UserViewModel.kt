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
): ViewModel() {
    var users = mutableStateListOf<User>()
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var genderFilter by mutableStateOf<String?>(null)
    var favorites by mutableStateOf(setOf<String>())

    init{
        loadUsers()
        viewModelScope.launch{
            favManager.favoritesFlow.collect {
                favorites = it
            }
        }
    }
    fun loadUsers(reset: Boolean = false){
        viewModelScope.launch{
            try{
                loading=true
                val newUser = repo.getUsers(genderFilter)
                if (reset) users.clear()
                users.addAll(newUser)
            }
            catch (e:Exception){
                error = "Error al cargar usuarios"
            } finally {
                loading = false
            }
        }
    }
    fun toggleFavorite(user: User){
        viewModelScope.launch{
            favManager.toggleFavorite(user.email)
        }
    }
    fun deleteUser(user : User){
        users.remove(user)
    }
    fun filter(gender: String?){
        genderFilter = gender
        loadUsers(true)
    }
}