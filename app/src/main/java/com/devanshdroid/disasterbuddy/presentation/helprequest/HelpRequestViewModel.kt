package com.devanshdroid.disasterbuddy.presentation.helprequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.data.model.GeneralResponse
import com.devanshdroid.disasterbuddy.data.model.HelpRequest
import com.devanshdroid.disasterbuddy.data.model.HelpRequestResponse
import com.devanshdroid.disasterbuddy.data.repository.HelpRequestRepository
import com.devanshdroid.disasterbuddy.domain.usecase.AcceptRequestUseCase
import com.devanshdroid.disasterbuddy.domain.usecase.CreateHelpRequestUseCase
import com.devanshdroid.disasterbuddy.domain.usecase.GetNearbyRequestsUseCase
import com.devanshdroid.disasterbuddy.location.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpRequestViewModel @Inject constructor(
    private val createHelpRequestUseCase: CreateHelpRequestUseCase,
    private val getNearbyRequestsUseCase: GetNearbyRequestsUseCase,
    private val acceptRequestUseCase: AcceptRequestUseCase,
    private val helpRequestRepository: HelpRequestRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _createRequestState = MutableStateFlow<NetworkResult<HelpRequestResponse>?>(null)
    val createRequestState: StateFlow<NetworkResult<HelpRequestResponse>?> =
        _createRequestState.asStateFlow()

    private val _nearbyRequestsState = MutableStateFlow<NetworkResult<List<HelpRequest>>?>(null)
    val nearbyRequestsState: StateFlow<NetworkResult<List<HelpRequest>>?> =
        _nearbyRequestsState.asStateFlow()

    private val _acceptRequestState = MutableStateFlow<NetworkResult<GeneralResponse>?>(null)
    val acceptRequestState: StateFlow<NetworkResult<GeneralResponse>?> =
        _acceptRequestState.asStateFlow()

    private val _selectedRequest = MutableStateFlow<HelpRequest?>(null)
    val selectedRequest: StateFlow<HelpRequest?> = _selectedRequest.asStateFlow()

    private val _currentLat = MutableStateFlow(0.0)
    val currentLat: StateFlow<Double> = _currentLat.asStateFlow()

    private val _currentLng = MutableStateFlow(0.0)
    val currentLng: StateFlow<Double> = _currentLng.asStateFlow()

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationManager.getLastKnownLocation()
                location?.let {
                    _currentLat.value = it.latitude
                    _currentLng.value = it.longitude
                }
            } catch (e: Exception) {
                // Handle silently — UI shows coordinates as 0.0 if unavailable
            }
        }
    }

    fun createHelpRequest(description: String) {
        viewModelScope.launch {
            createHelpRequestUseCase(
                description = description,
                latitude = _currentLat.value,
                longitude = _currentLng.value
            ).collect { _createRequestState.value = it }
        }
    }

    fun fetchNearbyRequests() {
        viewModelScope.launch {
            getNearbyRequestsUseCase(
                latitude = _currentLat.value,
                longitude = _currentLng.value
            ).collect { result ->
                when (result) {
                    is NetworkResult.Loading ->
                        _nearbyRequestsState.value = NetworkResult.Loading
                    is NetworkResult.Success ->
                        _nearbyRequestsState.value = NetworkResult.Success(result.data.requests)
                    is NetworkResult.Error ->
                        _nearbyRequestsState.value = NetworkResult.Error(result.message, result.code)
                }
            }
        }
    }

    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            acceptRequestUseCase(requestId).collect { _acceptRequestState.value = it }
        }
    }

    fun fetchRequestById(requestId: String) {
        viewModelScope.launch {
            helpRequestRepository.getRequestById(requestId).collect { result ->
                if (result is NetworkResult.Success) {
                    _selectedRequest.value = result.data.request
                }
            }
        }
    }

    fun resetCreateState() { _createRequestState.value = null }
    fun resetAcceptState() { _acceptRequestState.value = null }
}