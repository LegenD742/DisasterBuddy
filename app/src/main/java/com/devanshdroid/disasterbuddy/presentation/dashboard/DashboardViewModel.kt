package com.devanshdroid.disasterbuddy.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import com.devanshdroid.disasterbuddy.data.model.Alert
import com.devanshdroid.disasterbuddy.data.repository.AuthRepository
import com.devanshdroid.disasterbuddy.domain.usecase.GetAlertsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAlertsUseCase: GetAlertsUseCase,
    private val authRepository: AuthRepository,
    val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _alertsState = MutableStateFlow<NetworkResult<List<Alert>>?>(null)
    val alertsState: StateFlow<NetworkResult<List<Alert>>?> = _alertsState.asStateFlow()

    init {
        fetchAlerts()
    }

    fun fetchAlerts() {
        viewModelScope.launch {
            getAlertsUseCase().collect { result ->
                when (result) {
                    is NetworkResult.Loading ->
                        _alertsState.value = NetworkResult.Loading
                    is NetworkResult.Success ->
                        _alertsState.value = NetworkResult.Success(result.data.alerts)
                    is NetworkResult.Error ->
                        _alertsState.value = NetworkResult.Error(result.message, result.code)
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}