package com.wonddak.mtmanger.util

import androidx.compose.runtime.Composable

expect class PhotoPickerLauncher(
    onLaunch: () -> Unit,
) {
    fun launch()
}

@Composable
expect fun rememberPhotoPickerLauncher(
    onResult: (ByteArray) -> Unit,
): PhotoPickerLauncher