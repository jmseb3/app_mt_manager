package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

@Composable
internal actual fun ByteArrayImageView(
    modifier: Modifier,
    imgBytes: ByteArray,
) {
    Image(
        bitmap = Image.makeFromEncoded(imgBytes).toComposeImageBitmap(),
        contentDescription = "",
        modifier = modifier,
        alignment = Alignment.Center
    )
}