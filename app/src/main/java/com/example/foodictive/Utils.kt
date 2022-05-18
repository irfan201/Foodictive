package com.example.foodictive

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun rotateBitmap(bitmap: Bitmap, backCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (backCamera){
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }else{
        matrix.postRotate(-90f)
        matrix.postScale(-1f,1f,bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}

fun createFile(application: Application): File {
    val media = application.externalMediaDirs.firstOrNull()?.let {
        File(it,"MyCamera").apply { mkdirs() }
    }

    val outputDirectory = if (
        media != null && media.exists()
    )media else application.filesDir

    return File(outputDirectory,"$timeStamp.jpg")
}

private const val FILE_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(FILE_FORMAT, Locale.US).format(System.currentTimeMillis())
