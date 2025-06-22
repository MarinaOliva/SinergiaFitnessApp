package com.example.clubdeportivosinergiafitness.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import android.view.View
import java.io.File
import java.io.FileOutputStream

object PdfUtils {

    fun guardarVistaComoPDF(context: Context, view: View, nombreArchivo: String): File {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "$nombreArchivo.pdf")
        val outputStream = FileOutputStream(file)

        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
        bitmap.recycle()

        return file
    }

    fun abrirPDF(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se encontró una app para abrir PDF", Toast.LENGTH_SHORT).show()
        }
    }
}
