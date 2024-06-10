package com.wonddak.mtmanger.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import org.koin.java.KoinJavaComponent

actual object AppUtil {
    actual fun sendMail() {
        val context: Context = KoinJavaComponent.getKoin().get()
        Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(MAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, MAIL_TITLE)
            flags = FLAG_ACTIVITY_NEW_TASK
        }.let {
            context.startActivity(it)
        }
    }
}