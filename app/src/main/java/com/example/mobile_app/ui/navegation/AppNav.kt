package com.example.mobile_app.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.ui.screens.UserDetailScreen
import com.example.mobile_app.ui.screens.UserListScreen
import com.example.mobile_app.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Configuracion de la navegacion de la app
 * Define las rutas posibles y los composables asociados.
 * La app tiene 2 destinos: la lista de usarios y el detalle de usuario
 * @param viewModel ViewModel compartido en pantallas
 */
@Composable
fun AppNav(viewModel: UserViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "list") {
        composable("list") {
            UserListScreen(nav, viewModel)
        }
        composable("detail/{email}") { backStack ->
            val email = backStack.arguments?.getString("email")
            val user = viewModel.users.find { it.email == email }
                ?: viewModel.favoriteUsers.find { it.email == email }
            user?.let { UserDetailScreen(it, nav) }
        }
    }
}