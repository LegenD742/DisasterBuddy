package com.devanshdroid.disasterbuddy.data.remote.api


import com.devanshdroid.disasterbuddy.data.model.AlertsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlertApiService {

    @GET("api/alerts")
    suspend fun getAlerts(): Response<AlertsResponse>

    @GET("api/alerts/nearby")
    suspend fun getNearbyAlerts(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radiusKm: Double = 10.0
    ): Response<AlertsResponse>
}