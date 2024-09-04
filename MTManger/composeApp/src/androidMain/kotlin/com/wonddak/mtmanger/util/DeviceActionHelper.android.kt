package com.wonddak.mtmanger.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import org.koin.java.KoinJavaComponent

actual class DeviceActionHelper {
    private val context: Context = KoinJavaComponent.getKoin().get()
    actual fun makeCall(phoneNumber: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")
            ).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

}