package com.example.wecli.useCase

import com.example.wecli.repository.hourRepository.HourRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GetMomentDayUseCase(
    private val hourRepository: HourRepository
) {
    operator fun invoke(): Flow<String> = flow {
        hourRepository.getCurrentHour().collect { currentHour ->
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val hour = LocalTime.parse(currentHour, formatter)

            when {
                hour.isAfter(LocalTime.of(6, 0)) && hour.isBefore(
                    LocalTime.of(
                        12,
                        0
                    )
                ) -> emit("Morning")

                hour.isAfter(LocalTime.of(12, 0)) && hour.isBefore(
                    LocalTime.of(
                        18,
                        0
                    )
                ) -> emit("Afternoon")

                else -> emit("Night")
            }
        }
    }
}