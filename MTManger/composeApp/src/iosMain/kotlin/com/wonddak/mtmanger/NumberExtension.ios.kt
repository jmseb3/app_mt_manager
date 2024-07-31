package com.wonddak.mtmanger

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

private val numberFormatter = NSNumberFormatter()

actual fun Int.toPriceString(add: String): String {
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle
    return "${numberFormatter.stringFromNumber(NSNumber(int = this))} $add"
}

actual fun Long.toPriceString(add: String): String {
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle
    return "${numberFormatter.stringFromNumber(NSNumber(long = this))} $add"
}