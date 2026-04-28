package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.data.repository.AlertRepository
import javax.inject.Inject

class GetAlertsUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    operator fun invoke() = alertRepository.getAlerts()
}