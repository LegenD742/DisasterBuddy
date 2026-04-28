package com.devanshdroid.disasterbuddy.data.repository

import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.data.model.GeneralResponse
import com.devanshdroid.disasterbuddy.data.model.HelpRequest
import com.devanshdroid.disasterbuddy.data.model.HelpRequestResponse
import com.devanshdroid.disasterbuddy.data.model.RequestsResponse
import com.devanshdroid.disasterbuddy.data.remote.api.HelpRequestApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HelpRequestRepository @Inject constructor(
    private val helpRequestApiService: HelpRequestApiService,
    private val preferenceManager: PreferenceManager
) {

    fun createHelpRequest(
        description: String,
        latitude: Double,
        longitude: Double
    ): Flow<NetworkResult<HelpRequestResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val request = HelpRequest(
                citizen_id = preferenceManager.userId ?: "",
                description = description,
                latitude = latitude,
                longitude = longitude,
                status = "Pending"
            )
            val response = helpRequestApiService.createHelpRequest(request)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to create request",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun getNearbyRequests(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Flow<NetworkResult<RequestsResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = helpRequestApiService.getNearbyRequests(latitude, longitude, radiusKm)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to fetch nearby requests",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun getMyRequests(): Flow<NetworkResult<RequestsResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = helpRequestApiService.getMyRequests()
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to fetch your requests",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun acceptRequest(requestId: String): Flow<NetworkResult<GeneralResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = helpRequestApiService.acceptRequest(requestId)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to accept request",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun resolveRequest(requestId: String): Flow<NetworkResult<GeneralResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = helpRequestApiService.resolveRequest(requestId)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to resolve request",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun getRequestById(requestId: String): Flow<NetworkResult<HelpRequestResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = helpRequestApiService.getRequestById(requestId)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to fetch request",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }
}