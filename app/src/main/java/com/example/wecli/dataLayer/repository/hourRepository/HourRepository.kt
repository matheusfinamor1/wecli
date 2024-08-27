package com.example.wecli.dataLayer.repository.hourRepository

import kotlinx.coroutines.flow.Flow

interface HourRepository {
    fun getCurrentHour(): Flow<String>
}