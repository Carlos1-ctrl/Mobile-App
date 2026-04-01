package com.example.mobile_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobile_app.data.datastore.FavoritesManager
import com.example.mobile_app.data.repository.UserRepository
import com.example.mobile_app.ui.navegation.AppNav
import com.example.mobile_app.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = UserRepository()
        val fav = FavoritesManager(applicationContext)
        val vm = UserViewModel(repo, fav)

        setContent {
            AppNav(vm)
        }
    }
}