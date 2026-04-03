package com.example.mobile_app.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.mobile_app.data.model.User
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun createPdf(context: Context, user: User) {
    try {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { textSize = 14f }

        var y = 30f
        val lineHeight = 24f

        canvas.drawText("Nombre: ${user.name.first} ${user.name.last}", 10f, y, paint); y += lineHeight
        canvas.drawText("Email: ${user.email}", 10f, y, paint); y += lineHeight
        canvas.drawText("Teléfono: ${user.phone}", 10f, y, paint); y += lineHeight
        canvas.drawText("Ciudad: ${user.location.city}", 10f, y, paint); y += lineHeight
        canvas.drawText("País: ${user.location.country}", 10f, y, paint); y += lineHeight
        canvas.drawText("Calle: ${user.location.street.name} ${user.location.street.number}", 10f, y, paint)

        pdf.finishPage(page)

        val fileName = "user_${user.name.first}_${user.name.last}.pdf"
        val outputStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ usa MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
            )
            outputStream = uri?.let { context.contentResolver.openOutputStream(it) }
        } else {
            // Android 9 o menor
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            outputStream = FileOutputStream(file)
        }

        outputStream?.use { pdf.writeTo(it) }
        pdf.close()

        Toast.makeText(context, "PDF guardado en Descargas: $fileName", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        Toast.makeText(context, "Error al guardar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}