package com.example.foodictive

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Patterns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun rotateBitmap(bitmap: Bitmap, backCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (backCamera){
        matrix.postRotate(360f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }else{
        matrix.postRotate(-360f)
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

fun uriToFile(image: Uri, context: Context): File{
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(image) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len:Int
    while (inputStream.read(buf).also { len = it  } > 0) outputStream.write(buf,0,len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun String.emailValid(): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}

fun createCustomTempFile(context: Context):File{
    val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp,"jpg",storage)
}


private const val FILE_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(FILE_FORMAT, Locale.US).format(System.currentTimeMillis())
