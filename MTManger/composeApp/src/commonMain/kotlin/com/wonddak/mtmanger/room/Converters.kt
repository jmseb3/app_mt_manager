package com.wonddak.mtmanger.room

import android.graphics.Bitmap
import androidx.room.TypeConverter

class Converters {

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun toByteArray(bitmap : Bitmap) : ByteArray{
        return bitmap.toByteArray()
    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray) : Bitmap{
        return bytes.toBitmap()
    }
}

internal expect fun Bitmap.toByteArray() :ByteArray
internal expect fun ByteArray.toBitmap() :Bitmap