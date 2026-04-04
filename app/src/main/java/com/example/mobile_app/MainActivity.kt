package com.example.mobile_app


import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_app.data.datastore.FavoritesManager
import com.example.mobile_app.data.repository.UserRepository
import com.example.mobile_app.ui.navegation.AppNav
import com.example.mobile_app.ui.theme.MobileAppTheme
import com.example.mobile_app.viewmodel.UserViewModel
import com.example.mobile_app.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    private val  permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS
            )
        )

        val repo = UserRepository()
        val fav = FavoritesManager(applicationContext)
        val vm = ViewModelProvider(
            this,
            UserViewModelFactory(repo, fav)
        )[UserViewModel::class.java]

        setContent {
            MobileAppTheme {
                AppNav(vm)
            }
        }
    }
}