package com.devanshdroid.disasterbuddy.presentation.helprequest.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.data.model.HelpRequest
import com.devanshdroid.disasterbuddy.location.LocationPermissionHandler
import com.devanshdroid.disasterbuddy.presentation.components.ErrorDialog
import com.devanshdroid.disasterbuddy.presentation.components.LoadingIndicator
import com.devanshdroid.disasterbuddy.presentation.components.StatusBadge
import com.devanshdroid.disasterbuddy.presentation.helprequest.HelpRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsListScreen(
    onRequestClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: HelpRequestViewModel = hiltViewModel()
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val nearbyRequestsState by viewModel.nearbyRequestsState.collectAsStateWithLifecycle()

    LocationPermissionHandler(
        onPermissionGranted = {
            viewModel.fetchCurrentLocation()
            viewModel.fetchNearbyRequests()
        },
        onPermissionDenied = {
            errorMessage = "Location permission required to see nearby requests."
        }
    )

    errorMessage?.let {
        ErrorDialog(message = it, onDismiss = { errorMessage = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Help Requests", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchNearbyRequests() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = nearbyRequestsState) {
                is NetworkResult.Loading -> LoadingIndicator()
                is NetworkResult.Error -> {
                    Text(
                        text = "Failed to load requests: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                is NetworkResult.Success -> {
                    if (state.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✅ No nearby help requests",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(state.data) { request ->
                                HelpRequestCard(
                                    request = request,
                                    onClick = { onRequestClick(request._id) }
                                )
                            }
                        }
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
fun HelpRequestCard(
    request: HelpRequest,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = request.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${"%.4f".format(request.latitude)}, " +
                            "${"%.4f".format(request.longitude)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap to view details & accept",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}