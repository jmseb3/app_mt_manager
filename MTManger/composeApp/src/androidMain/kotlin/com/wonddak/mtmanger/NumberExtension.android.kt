package com.wonddak.mtmanger

import java.text.DecimalFormat

actual fun Int.toPriceString(add: String): String {
    val dec = DecimalFormat("#,###")
    return dec.format(this) + add
}
actual fun Long.toPriceString(add: String): String {
    val dec = DecimalFormat("#,###")
    return dec.format(this) + add
}