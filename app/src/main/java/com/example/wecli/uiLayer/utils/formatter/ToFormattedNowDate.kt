package com.example.wecli.uiLayer.utils.formatter

import com.example.wecli.uiLayer.utils.DateFormats
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toFormattedNowDate(): String =
    this.format(DateTimeFormatter.ofPattern(DateFormats.DATE_FORMAT))
