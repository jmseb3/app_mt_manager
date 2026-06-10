package com.wonddak.mtmanger.ui.view.setting

import platform.Foundation.NSBundle

actual fun getVersion(): String {
    return NSBundle.mainBundle().infoDictionary!!["CFBundleShortVersionString"].toString()
}
