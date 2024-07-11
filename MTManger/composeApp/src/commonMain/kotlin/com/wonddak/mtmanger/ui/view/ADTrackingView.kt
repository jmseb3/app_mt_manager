package com.wonddak.mtmanger.ui.view

import androidx.compose.runtime.Composable

@Composable
expect fun ADTrackingView(
    finish: () -> Unit,
)

expect val useADT :Boolean