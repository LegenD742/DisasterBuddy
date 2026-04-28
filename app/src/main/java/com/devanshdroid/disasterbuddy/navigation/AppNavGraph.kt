package com.devanshdroid.disasterbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devanshdroid.disasterbuddy.presentation.auth.screens.LoginScreen
import com.devanshdroid.disasterbuddy.presentation.auth.screens.RegisterScreen
import com.devanshdroid.disasterbuddy.presentation.dashboard.screens.DashboardScreen
import com.devanshdroid.disasterbuddy.presentation.helprequest.screens.CreateHelpRequestScreen
import com.devanshdroid.disasterbuddy.presentation.helprequest.screens.RequestStatusScreen
import com.devanshdroid.disasterbuddy.presentation.helprequest.screens.RequestsListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCreateRequest = {
                    navController.navigate(Screen.CreateHelpRequest.route)
                },
                onNavigateToRequestsList = {
                    navController.navigate(Screen.RequestsList.route)
                },
                onNavigateToRequestStatus = { requestId ->
                    navController.navigate(Screen.RequestStatus.createRoute(requestId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CreateHelpRequest.route) {
            CreateHelpRequestScreen(
                onRequestCreated = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RequestsList.route) {
            RequestsListScreen(
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestStatus.createRoute(requestId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.RequestStatus.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            RequestStatusScreen(
                requestId = requestId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}