package com.wonddak.mtmanger.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class DeviceActionHelper {
    actual fun makeCall(phoneNumber: String) {
        UIApplication.sharedApplication.openURL(NSURL(string = "tel:$phoneNumber"))
    }

}