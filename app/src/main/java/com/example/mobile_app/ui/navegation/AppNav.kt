package com.example.mobile_app.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.ui.screens.UserDetailScreen
import com.example.mobile_app.ui.screens.UserListScreen
import com.example.mobile_app.viewmodel.LoginViewModel
import com.example.mobile_app.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_app.ui.screens.LoginScreen

@Composable
fun AppNav(viewModel: UserViewModel){
    val nav= rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController = nav, startDestination = "login"){
    composable("login") {
        LoginScreen(nav, loginViewModel)
    }
    composable("list"){
        UserListScreen(nav,viewModel)
    }
        composable("detail/{email}"){backStack ->
            val email = backStack.arguments?.getString("email")
            val user = viewModel.users.find { it.email == email }
                ?: viewModel.favoriteUsers.find { it.email == email }

            user?.let {
                UserDetailScreen(it, nav)
            }
        }
    }
}
