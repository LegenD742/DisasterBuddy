package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.data.repository.HelpRequestRepository
import javax.inject.Inject

class CreateHelpRequestUseCase @Inject constructor(
    private val helpRequestRepository: HelpRequestRepository
) {
    operator fun invoke(
        description: String,
        latitude: Double,
        longitude: Double
    ) = helpRequestRepository.createHelpRequest(description, latitude, longitude)
}