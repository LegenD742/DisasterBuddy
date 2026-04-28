package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.data.repository.HelpRequestRepository
import javax.inject.Inject

class AcceptRequestUseCase @Inject constructor(
    private val helpRequestRepository: HelpRequestRepository
) {
    operator fun invoke(requestId: String) =
        helpRequestRepository.acceptRequest(requestId)
}