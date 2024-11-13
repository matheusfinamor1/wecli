package com.matheusfinamor.wecli.dataLayer.repository.hourRepository

import com.matheusfinamor.wecli.uiLayer.utils.Formats.HOUR_FORMAT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HourRepositoryImpl : HourRepository {
    override fun getCurrentHour(): Flow<String> = flow {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(HOUR_FORMAT)
        emit(now.format(formatter))
    }
}