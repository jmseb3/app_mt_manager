package com.wonddak.mtmanger

import java.text.DecimalFormat

fun Int.toPriceString(add:String ="원"): String {
    val dec = DecimalFormat("#,###")
    return dec.format(this) + add
}