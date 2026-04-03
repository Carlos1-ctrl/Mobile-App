package com.example.mobile_app.ui.screens


import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_app.ui.components.ErrorBanner
import com.example.mobile_app.ui.components.UserItem
import com.example.mobile_app.ui.theme.*
import com.example.mobile_app.viewmodel.UserViewModel

@Composable
fun UserListScreen(nav: NavController, vm: UserViewModel) {
    var showFavorites by remember { mutableStateOf(false) }

    val favoriteUsers = vm.favoriteUsers
    val displayUsers = if(showFavorites) favoriteUsers else vm.users

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(AccentPurple, AccentBlue)))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =  Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if(showFavorites) "Favoritos" else "Usuarios",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color= Color.White
                )
                IconButton(onClick = {showFavorites = !showFavorites}){
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favoritos",
                        tint= if(showFavorites) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
        vm.error?.let{ErrorBanner(it)}
        if (!showFavorites){
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterButton("Hombres", Modifier.weight(1f)) { vm.filter("male") }
                FilterButton("Mujeres", Modifier.weight(1f)) { vm.filter("female") }
                FilterButton("Reset", Modifier.weight(1f)) { vm.filter(null) }
            }
        }
        else{
            Spacer(Modifier.height(8.dp))
        }
        if(showFavorites && favoriteUsers.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier= Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No tienes favoritos aun",
                        color= TextSecondary,
                        fontSize = 16.sp)
                    Text(
                        "Toca la estrella en un usuario para agregarlo",
                        color = TextSecondary.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }
            }
        }
        else if(vm.loading && vm.users.isEmpty()){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentPurple)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                items(displayUsers) { user ->
                    UserItem(
                        user = user,
                        isFav = vm.favorites.contains(user.email),
                        onClick = { nav.navigate("detail/${user.email}") },
                        onDelete = { vm.deleteUser(user) },
                        onFav = { vm.toggleFavorite(user) }
                    )
                    if (!showFavorites && user == vm.users.last()) vm.loadUsers()
                }
            }
        }

        }
    }

@Composable
fun FilterButton(text: String, modifier:Modifier=Modifier, onClick :() -> Unit){
Button(
    onClick = onClick,
    modifier= modifier.height(38.dp),
    shape = RoundedCornerShape(10.dp),
    colors = ButtonDefaults.buttonColors(containerColor = DarkCard),
    contentPadding = PaddingValues(horizontal = 8.dp)
){
    Text(text, color = AccentGlow, fontSize = 13.sp, fontWeight = FontWeight.Medium)
}
}

