package com.example.wecli.uiLayer.utils.formatter

import com.example.wecli.uiLayer.utils.Formats
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern(Formats.DATE_FORMAT)
    val date = Instant.ofEpochMilli(this)
        .atZone(ZoneId.of(Formats.TIME_ZONE))
        .toLocalDate()
    return date.format(formatter)
}