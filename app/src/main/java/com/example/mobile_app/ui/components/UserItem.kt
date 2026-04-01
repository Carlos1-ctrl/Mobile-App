package com.example.mobile_app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.mobile_app.data.model.User

@Composable
fun UserItem(
    user: User,
    isFav: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onFav: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {

        Row(Modifier.padding(16.dp)) {

            AsyncImage(
                model = user.picture.large,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Column(Modifier.weight(1f)) {
                Text("${user.name.first} ${user.name.last}")
                Text(user.email)
                Text(user.gender)
            }

            Column {
                Button(onClick = onFav) {
                    Text(if (isFav) "★" else "☆")
                }
                Button(onClick = onDelete) {
                    Text("X")
                }
            }
        }
    }
}
