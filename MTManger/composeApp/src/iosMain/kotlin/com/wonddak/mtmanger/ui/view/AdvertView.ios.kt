package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AdvertView(modifier: Modifier) {
    UIKitViewController(
        factory = { AdMobHelper.getUiViewController() },
        modifier = modifier.fillMaxWidth(),
    )
}


object AdMobHelper {
    private lateinit var uiViewController: UIViewController
    fun init(createUIViewController: () -> UIViewController) {
        println("init AdMobHelper!!!!")
        uiViewController = createUIViewController()
    }

    fun getUiViewController(): UIViewController = uiViewController
}