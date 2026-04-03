package com.example.mobile_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun validate(){
        emailError = when{
            email.isBlank() -> "El correo no puede estar vacio"
            !email.contains("@") -> "Correo invalido"
            !email.contains(".") -> "Correo invalido"
            else -> null
        }
        passwordError = when{
            password.isBlank() -> "La contraseña no puede estar vacia"
            password.length< 6 -> "minimo 6 caracteres"
            else -> null
        }

        loginSuccess=emailError == null && passwordError == null
    }
    fun resetLogin() {
        loginSuccess = false
    }
}