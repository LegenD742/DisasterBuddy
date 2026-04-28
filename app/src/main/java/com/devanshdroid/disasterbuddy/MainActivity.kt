package com.devanshdroid.disasterbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.navigation.AppNavGraph
import com.devanshdroid.disasterbuddy.navigation.Screen
import com.devanshdroid.disasterbuddy.ui.theme.DisasterBuddyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.devanshdroid.disasterbuddy.presentation.dashboard.screens.DashboardScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalLifecycleOwner provides this@MainActivity
            ) {
                DisasterBuddyTheme {
                    val navController = rememberNavController()
                    val startDestination = Screen.Dashboard.route
//                    val startDestination = if (preferenceManager.isLoggedIn())
//                        Screen.Dashboard.route
//                    else
//                        Screen.Login.route

                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}