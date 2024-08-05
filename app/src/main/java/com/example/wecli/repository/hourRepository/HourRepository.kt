package com.example.wecli.repository.hourRepository

import kotlinx.coroutines.flow.Flow

interface HourRepository {
    fun getCurrentHour(): Flow<String>
}