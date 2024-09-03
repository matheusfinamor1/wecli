package com.example.wecli.uiLayer.utils.formatter

import com.example.wecli.uiLayer.utils.DateFormats
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern(DateFormats.DATE_FORMAT)
    val date = Instant.ofEpochMilli(this)
        .atZone(ZoneId.of(DateFormats.TIME_ZONE))
        .toLocalDate()
    return date.format(formatter)
}