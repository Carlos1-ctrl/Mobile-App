package com.example.mobile_app.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.navigation.NavController
import com.example.mobile_app.ui.components.UserItem
import com.example.mobile_app.viewmodel.UserViewModel

@Composable
fun UserListScreen(nav: NavController, vm: UserViewModel){
    Column{
        Row{
            Button(onClick = { vm.filter("male")} ){ Text("Hombres") }
            Button(onClick = {vm.filter("female")}) {Text("Mujeres") }
            Button(onClick = { vm.filter(null) }) { Text("Reset") }
        }
        LazyColumn {
            items(vm.users) { user ->

                UserItem(
                    user,
                    vm.favorites.contains(user.email),
                    onClick = { nav.navigate("detail/${user.email}") },
                    onDelete = { vm.deleteUser(user) },
                    onFav = { vm.toggleFavorite(user) }
                )

                if (user == vm.users.last()) vm.loadUsers()
            }
        }
    }
}