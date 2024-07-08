package com.wonddak.mtmanger

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

expect fun Int.toPriceString(add: String = "원"): String
expect fun Long.toPriceString(add: String = "원"): String

@OptIn(FormatStringsInDatetimeFormats::class)
fun Long?.toDateString(): String {
    this ?: return ""
    val date =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.of("Asia/Seoul")).date
    val formatPattern = "yyyy.MM.dd"
    val dateTimeFormat = LocalDate.Format {
        byUnicodePattern(formatPattern)
    }
    return date.format(dateTimeFormat)
}