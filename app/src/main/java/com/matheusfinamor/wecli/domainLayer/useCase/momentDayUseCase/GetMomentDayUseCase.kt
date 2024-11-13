package com.matheusfinamor.wecli.domainLayer.useCase.momentDayUseCase

import com.matheusfinamor.wecli.dataLayer.repository.hourRepository.HourRepository
import com.matheusfinamor.wecli.uiLayer.utils.Formats.DAY_AFTERNOON
import com.matheusfinamor.wecli.uiLayer.utils.Formats.DAY_MORNING
import com.matheusfinamor.wecli.uiLayer.utils.Formats.DAY_NIGHT
import com.matheusfinamor.wecli.uiLayer.utils.Formats.HOUR_FORMAT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GetMomentDayUseCase(
    private val hourRepository: HourRepository
) {
    operator fun invoke(): Flow<String> = flow {
        hourRepository.getCurrentHour().collect { currentHour ->
            val formatter = DateTimeFormatter.ofPattern(HOUR_FORMAT)
            val hour = LocalTime.parse(currentHour, formatter)

            when {
                hour.isAfter(LocalTime.of(6, 0)) && hour.isBefore(
                    LocalTime.of(
                        12,
                        0
                    )
                ) -> emit(DAY_MORNING)

                hour.isAfter(LocalTime.of(12, 0)) && hour.isBefore(
                    LocalTime.of(
                        18,
                        0
                    )
                ) -> emit(DAY_AFTERNOON)

                else -> emit(DAY_NIGHT)
            }
        }
    }
}