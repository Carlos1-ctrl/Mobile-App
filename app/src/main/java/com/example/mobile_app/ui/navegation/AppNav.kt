package com.example.mobile_app.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.ui.screens.UserDetailScreen
import com.example.mobile_app.ui.screens.UserListScreen
import com.example.mobile_app.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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