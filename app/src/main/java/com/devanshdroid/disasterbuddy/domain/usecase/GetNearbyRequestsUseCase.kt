package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.data.repository.HelpRequestRepository
import javax.inject.Inject

class GetNearbyRequestsUseCase @Inject constructor(
    private val helpRequestRepository: HelpRequestRepository
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double
    ) = helpRequestRepository.getNearbyRequests(latitude, longitude)
}