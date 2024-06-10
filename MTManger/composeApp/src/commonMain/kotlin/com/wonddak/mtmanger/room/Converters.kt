package com.wonddak.mtmanger.room

import androidx.room.TypeConverter
import org.jetbrains.skia.Bitmap

class Converters {

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun toByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

internal expect fun Bitmap.toByteArray() :ByteArray
internal expect fun ByteArray.toBitmap() :Bitmap