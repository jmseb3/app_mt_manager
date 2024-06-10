package com.wonddak.mtmanger

actual fun Int.toPriceString(add: String): String {
    return "$this $add"
}