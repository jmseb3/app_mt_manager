package com.wonddak.mtmanger.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual class PhotoPickerLauncher actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberPhotoPickerLauncher(onResult: (ByteArray) -> Unit): PhotoPickerLauncher {
    val photoPickerLauncher: PhotoPickerLauncher

    val context = LocalContext.current
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                CoroutineScope(Dispatchers.IO).launch {
                    context.contentResolver.openInputStream(uri)?.use { fis ->
                        val byteBuffer = ByteArrayOutputStream()
                        val bufferSize = 1024
                        val buffer = ByteArray(bufferSize)
                        var len = 0
                        while (fis.read(buffer).also { len = it } != -1) {
                            byteBuffer.write(buffer, 0, len)
                        }
                        val originalImage = byteBuffer.toByteArray()
                        val bitmapImage =
                            BitmapFactory.decodeByteArray(originalImage, 0, originalImage.size)
                        val mutableBitmapImage =
                            Bitmap.createScaledBitmap(bitmapImage, bitmapImage.width, bitmapImage.height, false)
                        val outputStream = ByteArrayOutputStream()
                        mutableBitmapImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
                        if (mutableBitmapImage != bitmapImage) {
                            mutableBitmapImage.recycle();
                        } // else they are the same, just recycle once

                        bitmapImage.recycle()
                        val bytes = outputStream.toByteArray()
                        withContext(Dispatchers.Main) {
                            onResult(bytes)
                        }
                    }
                }

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    photoPickerLauncher = remember {
        PhotoPickerLauncher(
            onLaunch = {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }
    return  photoPickerLauncher
}