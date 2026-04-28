package com.devanshdroid.disasterbuddy.presentation.dashboard.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.core.utils.Constants
import com.devanshdroid.disasterbuddy.presentation.components.ErrorDialog
import com.devanshdroid.disasterbuddy.presentation.components.LoadingIndicator
import com.devanshdroid.disasterbuddy.presentation.dashboard.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCreateRequest: () -> Unit,
    onNavigateToRequestsList: () -> Unit,
    onNavigateToRequestStatus: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val alertsState by viewModel.alertsState.collectAsStateWithLifecycle()
    val userName = viewModel.preferenceManager.userName ?: "User"
    val userRole = viewModel.preferenceManager.userRole ?: Constants.ROLE_CITIZEN
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    onLogout()
                }) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    errorMessage?.let {
        ErrorDialog(message = it, onDismiss = { errorMessage = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🚨 DisasterBuddy", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Hello, $userName (${userRole.replaceFirstChar { it.uppercase() }})",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchAlerts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == Constants.ROLE_CITIZEN) {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToCreateRequest,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("Request Help") }
                )
            } else {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToRequestsList,
                    icon = { Icon(Icons.Default.List, contentDescription = "View") },
                    text = { Text("View Requests") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Active Disaster Alerts",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (val state = alertsState) {
                is NetworkResult.Loading -> LoadingIndicator()
                is NetworkResult.Error -> {
                    errorMessage = state.message
                    Text(
                        text = "Failed to load alerts. Pull to refresh.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                is NetworkResult.Success -> {
                    if (state.data.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✅ No active alerts in your area",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(state.data) { alert ->
                                AlertCard(alert = alert)
                            }
                        }
                    }
                }
                null -> {}
            }
        }
    }
}