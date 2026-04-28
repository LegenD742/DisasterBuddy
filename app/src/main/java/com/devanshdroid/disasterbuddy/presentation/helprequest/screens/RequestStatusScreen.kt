package com.devanshdroid.disasterbuddy.presentation.helprequest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.core.utils.Constants
import com.devanshdroid.disasterbuddy.presentation.components.ErrorDialog
import com.devanshdroid.disasterbuddy.presentation.components.LoadingIndicator
import com.devanshdroid.disasterbuddy.presentation.components.StatusBadge
import com.devanshdroid.disasterbuddy.presentation.helprequest.HelpRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestStatusScreen(
    requestId: String,
    onBack: () -> Unit,
    viewModel: HelpRequestViewModel = hiltViewModel()
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val selectedRequest by viewModel.selectedRequest.collectAsStateWithLifecycle()
    val acceptRequestState by viewModel.acceptRequestState.collectAsStateWithLifecycle()

    LaunchedEffect(requestId) {
        viewModel.fetchRequestById(requestId)
    }

    LaunchedEffect(acceptRequestState) {
        when (val state = acceptRequestState) {
            is NetworkResult.Success -> {
                viewModel.resetAcceptState()
                viewModel.fetchRequestById(requestId)
            }
            is NetworkResult.Error -> {
                errorMessage = state.message
                viewModel.resetAcceptState()
            }
            else -> {}
        }
    }

    errorMessage?.let {
        ErrorDialog(message = it, onDismiss = { errorMessage = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (selectedRequest == null) {
            LoadingIndicator()
            return@Scaffold
        }

        val request = selectedRequest!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Status Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Status",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    StatusBadge(status = request.status)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Description",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = request.description,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Location",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Lat: ${"%.6f".format(request.latitude)}, " +
                                "Lng: ${"%.6f".format(request.longitude)}",
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Volunteer info
            if (request.volunteer_id.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Volunteer",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Assigned Volunteer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = request.volunteer_id, fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Status Timeline
            Text(
                text = "Progress",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            listOf(
                Constants.STATUS_PENDING,
                Constants.STATUS_ACCEPTED,
                Constants.STATUS_RESOLVED
            ).forEachIndexed { index, step ->
                val isCompleted = when (request.status) {
                    Constants.STATUS_PENDING -> index == 0
                    Constants.STATUS_ACCEPTED -> index <= 1
                    Constants.STATUS_RESOLVED -> true
                    else -> false
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = step,
                        tint = if (isCompleted) Color(0xFF198754)
                        else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = step,
                        fontWeight = if (request.status == step)
                            FontWeight.Bold else FontWeight.Normal,
                        color = if (isCompleted) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.outlineVariant,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Accept Button (for volunteers on pending requests)
            if (request.status == Constants.STATUS_PENDING) {
                if (acceptRequestState is NetworkResult.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = { viewModel.acceptRequest(request._id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF198754)
                        )
                    ) {
                        Text(
                            "Accept This Request",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (request.status == Constants.STATUS_RESOLVED) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD4EDDA)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Resolved",
                            tint = Color(0xFF198754)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "This request has been resolved.",
                            color = Color(0xFF155724),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}