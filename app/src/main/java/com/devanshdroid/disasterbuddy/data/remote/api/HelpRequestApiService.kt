package com.devanshdroid.disasterbuddy.data.remote.api


import com.devanshdroid.disasterbuddy.data.model.GeneralResponse
import com.devanshdroid.disasterbuddy.data.model.HelpRequest
import com.devanshdroid.disasterbuddy.data.model.HelpRequestResponse
import com.devanshdroid.disasterbuddy.data.model.RequestsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HelpRequestApiService {

    @POST("api/requests")
    suspend fun createHelpRequest(
        @Body request: HelpRequest
    ): Response<HelpRequestResponse>

    @GET("api/requests/nearby")
    suspend fun getNearbyRequests(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radiusKm: Double = 10.0
    ): Response<RequestsResponse>

    @GET("api/requests/my")
    suspend fun getMyRequests(): Response<RequestsResponse>

    @PATCH("api/requests/{id}/accept")
    suspend fun acceptRequest(
        @Path("id") requestId: String
    ): Response<GeneralResponse>

    @PATCH("api/requests/{id}/resolve")
    suspend fun resolveRequest(
        @Path("id") requestId: String
    ): Response<GeneralResponse>

    @GET("api/requests/{id}")
    suspend fun getRequestById(
        @Path("id") requestId: String
    ): Response<HelpRequestResponse>
}