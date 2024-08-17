package com.wonddak.mtmanger.ui.view.home.main

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

actual val useShare: Boolean
    get() = false

actual fun shareImage(bitmap: ImageBitmap?) {
    bitmap?.let { share(bitmap) }
}

suspend fun Bitmap.getUri(context: Context): Uri = withContext(Dispatchers.IO) {
    val sharedImagesDir = File(context.filesDir, "share_images")

    if (!sharedImagesDir.exists()) {
        sharedImagesDir.mkdirs()
    }

    val tempFileToShare: File = sharedImagesDir.resolve("share_picture.png")
    val outputStream = FileOutputStream(tempFileToShare)
    compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()

    FileProvider.getUriForFile(
        context,
        "com.wonddak.mtmanger.fileprovider",
        tempFileToShare
    )
}

fun share(imageBitmap: ImageBitmap) {
    CoroutineScope(Dispatchers.Main).launch {
        val context = AppContext.get()
        val intentShareFile = Intent(Intent.ACTION_SEND)
        val mimeType = "image/*"
        val mimeTypeArray = arrayOf(mimeType)
        intentShareFile.setType(mimeType)
        val uri = imageBitmap.asAndroidBitmap().getUri(context)
        intentShareFile.clipData = ClipData(
            "A label describing your file to the user",
            mimeTypeArray,
            ClipData.Item(uri)
        )
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intentShareFile)
    }
}

object AppContext {
    private var value: WeakReference<Context?>? = null
    fun set(context: Context) {
        value = WeakReference(context)
    }

    internal fun get(): Context {
        return value?.get() ?: throw RuntimeException("Context Error")
    }
}