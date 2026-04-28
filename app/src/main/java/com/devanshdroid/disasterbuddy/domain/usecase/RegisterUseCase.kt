package com.devanshdroid.disasterbuddy.domain.usecase


import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.data.model.RegisterRequest
import com.devanshdroid.disasterbuddy.data.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) {
    operator fun invoke(
        name: String,
        email: String,
        password: String,
        role: String
    ) = authRepository.register(
        RegisterRequest(
            name = name,
            email = email,
            password = password,
            role = role,
            fcm_token = preferenceManager.fcmToken ?: ""
        )
    )
}