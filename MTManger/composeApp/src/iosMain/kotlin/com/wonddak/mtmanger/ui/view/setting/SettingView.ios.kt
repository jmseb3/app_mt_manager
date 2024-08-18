package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import platform.Foundation.NSBundle

@Composable
internal actual fun SettingAdFooter(removeAd: Boolean) {
    Text("")
}

actual fun getVersion(): String {
    return NSBundle.mainBundle().infoDictionary!!["CFBundleShortVersionString"].toString()
}
