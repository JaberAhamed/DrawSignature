package com.jaber.drawingapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Bitmap.saveToCacheStorage(context: Context, imageName: String): String? {
    val myPath = File(
        Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName)
    var fos: FileOutputStream? = null
    Log.d("Tag405", "screen sort created 1")

    try {
        Log.d("Tag405", "screen sort created 2323")
        fos = FileOutputStream(myPath)
        this.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: java.lang.Exception) {
        Log.d("Tag405", "screen sort created fINALYY ${e.toString()}")
        e.printStackTrace()
    } finally {
        Log.d("Tag405", "screen sort created fINALYY")
        try {
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return myPath.getAbsolutePath()
}

