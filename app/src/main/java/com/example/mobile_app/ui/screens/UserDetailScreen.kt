package com.example.mobile_app.ui.screens


import android.content.Intent
import android.net.Uri
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.navigation.NavController
import com.example.mobile_app.data.model.User
import java.io.File
import java.io.FileOutputStream

@Composable
fun UserDetailScreen(user: User, nav: NavController) {

    val context = LocalContext.current

    Column(Modifier.padding(16.dp)) {

        AsyncImage(model = user.picture.large, contentDescription = null)

        Text("${user.name.first} ${user.name.last}")
        Text(user.email)
        Text(user.phone)

        Button(onClick = {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,
                "${user.name.first} ${user.phone} ${user.email}")
            intent.setPackage("com.whatsapp")
            context.startActivity(intent)
        }) {
            Text("WhatsApp")
        }

        Button(onClick = {
            val lat = user.location.coordinates.latitude
            val lng = user.location.coordinates.longitude
            val uri = Uri.parse("geo:$lat,$lng")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }) {
            Text("Maps")
        }

        Button(onClick = {
            createPdf(context, user)
        }) {
            Text("PDF")
        }

        Button(onClick = { nav.popBackStack() }) {
            Text("Volver")
        }
    }
}

fun createPdf(context: Context, user: User) {
    val pdf = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdf.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()

    canvas.drawText(user.name.first, 10f, 25f, paint)

    pdf.finishPage(page)

    val file = File(context.filesDir, "user.pdf")
    pdf.writeTo(FileOutputStream(file))
    pdf.close()
}