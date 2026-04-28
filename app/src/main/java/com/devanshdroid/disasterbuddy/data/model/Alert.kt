package com.devanshdroid.disasterbuddy.data.model


data class Alert(
    val _id: String = "",
    val type: String = "",
    val severity: String = "",
    val location: String = "",
    val description: String = "",
    val timestamp: String = ""
)