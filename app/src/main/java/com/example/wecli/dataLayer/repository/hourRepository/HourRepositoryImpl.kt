package com.example.wecli.dataLayer.repository.hourRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HourRepositoryImpl : HourRepository {
    override fun getCurrentHour(): Flow<String> = flow {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        emit(now.format(formatter))
    }
}