package com.example.mobile_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mobile_app.viewmodel.LoginViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.example.mobile_app.ui.theme.AccentBlue
import com.example.mobile_app.ui.theme.AccentGlow
import com.example.mobile_app.ui.theme.AccentPurple
import com.example.mobile_app.ui.theme.DarkBackground
import com.example.mobile_app.ui.theme.DarkCard
import com.example.mobile_app.ui.theme.DarkSurface
import com.example.mobile_app.ui.theme.ErrorRed
import com.example.mobile_app.ui.theme.TextPrimary
import com.example.mobile_app.ui.theme.TextSecondary

@Composable
fun LoginScreen(nav: NavController, vm: LoginViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment =  Alignment.CenterHorizontally

        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(AccentPurple, AccentBlue))
                    ),
                contentAlignment = Alignment.Center
            ){Text("DMx", fontSize = 15.sp, fontWeight = FontWeight.Bold, color= Color.White)}

            Spacer(Modifier.height(16.dp))

            Text(
                "Bienvenido",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                "Inicia Sesión para Continuar",
                fontSize = 14.sp,
                color= TextSecondary
            )
            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        value = vm.email,
                        onValueChange = {vm.email = it},
                        label = { Text("Correo electronico") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = AccentGlow)
                        },
                        isError = vm.emailError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor =  AccentPurple,
                            unfocusedBorderColor = TextSecondary,
                            focusedLabelColor = AccentPurple,
                            cursorColor = AccentPurple,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    vm.emailError?.let {
                        Text(it, color= ErrorRed, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.password,
                        onValueChange = {vm.password= it},
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AccentGlow)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = vm.passwordError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor =  AccentPurple,
                            unfocusedBorderColor = TextSecondary,
                            focusedLabelColor = AccentPurple,
                            cursorColor = AccentPurple,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    vm.passwordError?.let {
                        Text(it, color= ErrorRed, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(12.dp))

                    Button(onClick = {
                        vm.validate()
                        if(vm.loginSuccess){
                            vm.resetLogin()
                            nav.navigate("list"){
                                popUpTo("login"){ inclusive = true}
                            }
                        }
                    },
                        modifier= Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                        ) {
                        Text(
                            "Entrar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                            )
                    }
                    }
                }
            }

        }
    }


