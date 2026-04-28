package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.data.model.LoginRequest
import com.devanshdroid.disasterbuddy.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) {
    operator fun invoke(
        email: String,
        password: String
    ) = authRepository.login(
        LoginRequest(
            email = email,
            password = password,
            fcm_token = preferenceManager.fcmToken ?: ""
        )
    )
}