package com.wonddak.mtmanger

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

fun Int.toPriceString(add: String = "원"): String {
    val dec = DecimalFormat("#,###")
    return dec.format(this) + add
}

fun Long.toDateString(): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd")
    return formatter.format(Date(this))
}

fun Long.toDateStringInfos(): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS")
    return formatter.format(Date(this))
}