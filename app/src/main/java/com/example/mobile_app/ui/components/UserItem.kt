package com.example.mobile_app.ui.components

import android.graphics.drawable.Icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile_app.data.model.User
import com.example.mobile_app.ui.theme.*
@Composable
fun UserItem(
    user: User,
    isFav: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onFav: () -> Unit
){
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog){

        AlertDialog(
            onDismissRequest = {showDeleteDialog= false },
            containerColor = DarkCard,
            title = {
                Text(
                    text = "Eliminar usuario",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text= {
                Text(
                    "Seguro que quieres eliminar a ${user.name.first} ${user.name.last}",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog= false
                    onDelete()
                },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                    ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick={showDeleteDialog= false}
                ) {
                    Text("Cancelar", color=TextSecondary)
                }
            }

        )
    }

    val starScale by animateFloatAsState(
        targetValue = if (isFav) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "starScale"
    )
    val starColor by animateColorAsState(
        targetValue = if (isFav) Color(0xFFFBBF24) else TextSecondary,
        label = "starColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable{ onClick()},
        shape =  RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.picture.large,
                contentDescription = null,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
            )
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("${user.name.first} ${user.name.last}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color= TextPrimary
                    )
                Text("${user.email}",
                    fontSize = 12.sp,
                    color= TextSecondary)
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier= Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if(user.gender == "male") AccentBlue.copy(alpha = 0.2f)
                            else AccentPurple.copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ){
                    Text(
                        if (user.gender == "male") "Hombre" else "Mujer",
                        fontSize =  11.sp,
                        color= if(user.gender == "male") AccentBlue else AccentPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick= onFav) {
                    Icon(
                        imageVector= if(isFav) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorito",
                        tint = starColor,
                        modifier = Modifier.scale(starScale)
                    )
                }

                IconButton(onClick= {showDeleteDialog= true}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = ErrorRed
                    )
                }
            }
        }
    }

}