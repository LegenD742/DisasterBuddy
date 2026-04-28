package com.devanshdroid.disasterbuddy.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object CreateHelpRequest : Screen("create_help_request")
    object RequestsList : Screen("requests_list")
    object RequestStatus : Screen("request_status/{requestId}") {
        fun createRoute(requestId: String) = "request_status/$requestId"
    }
}