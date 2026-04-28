package com.devanshdroid.disasterbuddy.data.model

data class HelpRequest(
    val _id: String = "",
    val citizen_id: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "Pending",
    val volunteer_id: String = "",
    val createdAt: String = ""
)