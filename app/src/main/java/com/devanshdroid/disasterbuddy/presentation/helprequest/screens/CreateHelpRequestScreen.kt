package com.devanshdroid.disasterbuddy.presentation.helprequest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import com.devanshdroid.disasterbuddy.location.LocationPermissionHandler
import com.devanshdroid.disasterbuddy.presentation.components.ErrorDialog
import com.devanshdroid.disasterbuddy.presentation.components.LoadingIndicator
import com.devanshdroid.disasterbuddy.presentation.helprequest.HelpRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHelpRequestScreen(
    onRequestCreated: () -> Unit,
    onBack: () -> Unit,
    viewModel: HelpRequestViewModel = hiltViewModel()
) {
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var locationGranted by remember { mutableStateOf(false) }

    val createRequestState by viewModel.createRequestState.collectAsStateWithLifecycle()
    val currentLat by viewModel.currentLat.collectAsStateWithLifecycle()
    val currentLng by viewModel.currentLng.collectAsStateWithLifecycle()

    LocationPermissionHandler(
        onPermissionGranted = {
            locationGranted = true
            viewModel.fetchCurrentLocation()
        },
        onPermissionDenied = {
            errorMessage = "Location permission is required to submit a help request."
        }
    )

    LaunchedEffect(createRequestState) {
        when (val state = createRequestState) {
            is NetworkResult.Success -> {
                viewModel.resetCreateState()
                onRequestCreated()
            }
            is NetworkResult.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    errorMessage?.let {
        ErrorDialog(message = it, onDismiss = {
            errorMessage = null
            viewModel.resetCreateState()
        })
    }

    if (createRequestState is NetworkResult.Loading) {
        LoadingIndicator()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Help", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Describe your emergency",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g. Flood water rising, need evacuation...") },
                minLines = 4,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Your Location",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        if (currentLat == 0.0 && currentLng == 0.0) {
                            Text(
                                text = "Fetching location...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Lat: ${"%.5f".format(currentLat)}, " +
                                        "Lng: ${"%.5f".format(currentLng)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { viewModel.fetchCurrentLocation() }) {
                        Text("Refresh", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.createHelpRequest(description.trim()) },
                enabled = description.isNotBlank() &&
                        locationGranted &&
                        (currentLat != 0.0 || currentLng != 0.0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Submit Help Request", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}