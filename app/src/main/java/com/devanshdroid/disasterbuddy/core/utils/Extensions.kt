package com.devanshdroid.disasterbuddy.core.utils

import android.content.Context
import android.widget.Toast
import retrofit2.Response

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <T> Response<T>.toNetworkResult(): com.devanshdroid.disasterbuddy.core.network.NetworkResult<T> {
    return if (isSuccessful && body() != null) {
        com.devanshdroid.disasterbuddy.core.network.NetworkResult.Success(body()!!)
    } else {
        com.devanshdroid.disasterbuddy.core.network.NetworkResult.Error(
            message = errorBody()?.string() ?: "Unknown error",
            code = code()
        )
    }
}