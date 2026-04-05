package com.example.mobile_app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.data.datastore.FavoritesManager
import com.example.mobile_app.data.model.User
import com.example.mobile_app.data.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel principal que maneja el estado de la lista de usuarios.
 * Sobrevive cambios de configuración y gestiona la comunicación
 * entre el repositorio, el DataStore y la UI.
 *
 * @param repo Repositorio que obtiene usuarios desde la API
 * @param favManager Manejador de favoritos persistentes con DataStore
 */
class UserViewModel(
    private val repo: UserRepository,
    private val favManager: FavoritesManager
) : ViewModel() {

    /** Lista de usuarios actualmente visibles según el filtro activo */
    var users = mutableStateListOf<User>()

    /** Indica si hay una carga en progreso */
    var loading by mutableStateOf(false)

    /** Mensaje de error para mostrar en la UI, null si no hay error */
    var error by mutableStateOf<String?>(null)

    /** Filtro de género activo: "male", "female" o null para todos */
    var genderFilter by mutableStateOf<String?>(null)

    /** Emails de usuarios marcados como favoritos */
    var favorites by mutableStateOf(setOf<String>())

    /**
     * Map de todos los usuarios vistos, usando email como llave.
     * Evita duplicados y permite acceder a favoritos sin importar el filtro activo.
     */
    private val seenUsersMap = mutableMapOf<String, User>()

    /**
     * Lista de usuarios favoritos calculada desde el map completo.
     * Funciona independientemente del filtro de género activo.
     */
    val favoriteUsers: List<User>
        get() = seenUsersMap.values.filter { favorites.contains(it.email) }

    init {
        loadUsers()

        // Colectar emails favoritos del DataStore
        viewModelScope.launch {
            favManager.favoritesFlow.collect {
                favorites = it
            }
        }

        // Cargar usuarios favoritos completos del DataStore al iniciar
        // para que estén disponibles aunque no aparezcan en la lista actual
        viewModelScope.launch {
            favManager.favoriteUsersFlow.collect { savedFavUsers ->
                savedFavUsers.forEach { user ->
                    seenUsersMap[user.email] = user
                }
            }
        }
    }

    /**
     * Carga usuarios desde la API aplicando el filtro de género activo.
     * Acumula los usuarios en [seenUsersMap] para tener acceso a todos los vistos.
     *
     * @param reset Si es true, limpia la lista antes de agregar nuevos usuarios
     */
    fun loadUsers(reset: Boolean = false) {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                val newUsers = repo.getUsers(genderFilter)

                if (reset) users.clear()
                users.addAll(newUsers)

                // Acumular en el map para acceso global sin importar el filtro
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

    /**
     * Alterna el estado de favorito de un usuario.
     * También lo registra en el map para que esté disponible en favoritos.
     *
     * @param user Usuario al que se le toggleará el favorito
     */
    fun toggleFavorite(user: User) {
        seenUsersMap[user.email] = user
        viewModelScope.launch {
            favManager.toggleFavorite(user)
        }
    }

    /**
     * Elimina un usuario de la lista visible y del map de usuarios vistos.
     *
     * @param user Usuario a eliminar
     */
    fun deleteUser(user: User) {
        users.remove(user)
        seenUsersMap.remove(user.email)
    }

    /**
     * Aplica un filtro de género y recarga la lista desde cero.
     *
     * @param gender "male", "female" o null para mostrar todos
     */
    fun filter(gender: String?) {
        genderFilter = gender
        loadUsers(true)
    }
}