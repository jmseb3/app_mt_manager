package com.wonddak.mtmanger.util

expect object AppUtil {
    fun sendMail()
}

internal const val MAIL_ADDRESS ="jmseb2@gmail.com"
internal const val MAIL_TITLE ="<MT매니저 관련 문의입니다.>"