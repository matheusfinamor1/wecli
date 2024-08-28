package com.example.wecli.uiLayer.utils.formatter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val origFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val newFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun format(dtTxt: String): String? {
        val dataObj = LocalDateTime.parse(dtTxt, origFormatter)
        return dataObj.format(newFormatter)
    }
}