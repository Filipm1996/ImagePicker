package com.example.imagepicker

import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.IOException
import java.net.URL

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}

fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap, contentResolver : ContentResolver): Boolean {
    val imageCollection = sdk29AndUp {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bmp.width)
        put(MediaStore.Images.Media.HEIGHT, bmp.height)
    }
    return try {
        contentResolver.insert(imageCollection, contentValues)?.also { uri ->
            contentResolver.openOutputStream(uri).use { outputStream ->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
        } ?: throw IOException("Couldn't create MediaStore entry")
        true
    } catch(e: IOException) {
        e.printStackTrace()
        false
    }
}

fun getBitmapFromUrl(urlString: String) : Bitmap?{
    try {
        val url = URL(urlString)
        return BitmapFactory.decodeStream(url.openStream())
    } catch (e: IOException) {
        println(e)
    }
    return null
}

