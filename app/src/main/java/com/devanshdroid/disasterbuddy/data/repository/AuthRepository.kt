package com.devanshdroid.disasterbuddy.data.repository


import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.data.model.AuthResponse
import com.devanshdroid.disasterbuddy.data.model.LoginRequest
import com.devanshdroid.disasterbuddy.data.model.RegisterRequest
import com.devanshdroid.disasterbuddy.data.remote.api.AuthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val preferenceManager: PreferenceManager
) {

    fun register(request: RegisterRequest): Flow<NetworkResult<AuthResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = authApiService.register(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                preferenceManager.jwtToken = body.token
                preferenceManager.userRole = body.user.role
                preferenceManager.userId = body.user._id
                preferenceManager.userName = body.user.name
                emit(NetworkResult.Success(body))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Registration failed",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun login(request: LoginRequest): Flow<NetworkResult<AuthResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = authApiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                preferenceManager.jwtToken = body.token
                preferenceManager.userRole = body.user.role
                preferenceManager.userId = body.user._id
                preferenceManager.userName = body.user.name
                emit(NetworkResult.Success(body))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Login failed",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun logout() {
        preferenceManager.clearAll()
    }

    fun isLoggedIn() = preferenceManager.isLoggedIn()
    fun getUserRole() = preferenceManager.userRole
    fun getUserName() = preferenceManager.userName
}