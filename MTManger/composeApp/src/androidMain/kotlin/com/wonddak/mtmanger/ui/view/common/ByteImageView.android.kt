package com.wonddak.mtmanger.ui.view.common

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
internal actual fun ByteArrayImageView(
    modifier: Modifier,
    imgBytes: ByteArray,
) {
    Image(
        bitmap = BitmapFactory.decodeByteArray(
            imgBytes,
            0,
            imgBytes.size
        ).asImageBitmap(),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center
    )
}