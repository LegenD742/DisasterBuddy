package com.devanshdroid.disasterbuddy.data.model

data class User(
    val _id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val fcm_token: String = ""
)