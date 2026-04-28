package com.devanshdroid.disasterbuddy.data.repository


import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.data.model.AlertsResponse
import com.devanshdroid.disasterbuddy.data.remote.api.AlertApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlertRepository @Inject constructor(
    private val alertApiService: AlertApiService
) {

    fun getAlerts(): Flow<NetworkResult<AlertsResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = alertApiService.getAlerts()
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to fetch alerts",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }

    fun getNearbyAlerts(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): Flow<NetworkResult<AlertsResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = alertApiService.getNearbyAlerts(latitude, longitude, radiusKm)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error(
                    message = response.errorBody()?.string() ?: "Failed to fetch nearby alerts",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Network error"))
        }
    }
}