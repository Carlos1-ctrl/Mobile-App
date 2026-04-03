package com.example.mobile_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_app.data.datastore.FavoritesManager
import com.example.mobile_app.data.repository.UserRepository

/**
 * Factory para crear instancias de [UserViewModel] con sus dependencias.
 * Necesario porque el ViewModel tiene parámetros en el constructor.
 */
class UserViewModelFactory(
    private val repo: UserRepository,
    private val favManager: FavoritesManager
) : ViewModelProvider.Factory {

    /**
     * Crea una nueva instancia del ViewModel solicitado.
     * @throws IllegalArgumentException si el ViewModel no es reconocido
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repo, favManager) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}