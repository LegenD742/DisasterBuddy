package com.devanshdroid.disasterbuddy.data.model


data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val fcm_token: String
)

data class LoginRequest(
    val email: String,
    val password: String,
    val fcm_token: String
)