package com.example.wecli.uiLayer.utils.formatter

import com.example.wecli.uiLayer.utils.DateFormats
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val origFormatter = DateTimeFormatter.ofPattern(DateFormats.FULL_DATE_TIME)
    private val newFormatterDataAndHour = DateTimeFormatter.ofPattern(DateFormats.NEW_FORMAT_FULL_DATE_TIME)
    private val newFormatterData = DateTimeFormatter.ofPattern(DateFormats.DATE_FORMAT)
    private val newFormatterHour = DateTimeFormatter.ofPattern(DateFormats.HOUR_FORMAT)

    fun String.formatDataAndHour(): String? {
        return try {
            val dataObj = LocalDateTime.parse(this, origFormatter)
            dataObj.format(newFormatterDataAndHour)
        } catch (e: Exception) {
            null
        }
    }
    fun String.formatData(): String? {
        return try {
            val dataObj = LocalDateTime.parse(this, origFormatter)
            dataObj.format(newFormatterData)
        } catch (e: Exception) {
            null
        }
    }
    fun String.formatHour(): String? {
        return try {
            val dataObj = LocalDateTime.parse(this, origFormatter)
            dataObj.format(newFormatterHour)
        } catch (e: Exception) {
            null
        }
    }
}