package com.wonddak.mtmanger.room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

internal actual fun Bitmap.toByteArray() :ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}
internal actual fun ByteArray.toBitmap() :Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}