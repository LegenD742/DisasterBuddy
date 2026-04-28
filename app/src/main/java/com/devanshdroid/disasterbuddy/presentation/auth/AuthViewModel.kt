package com.devanshdroid.disasterbuddy.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devanshdroid.disasterbuddy.core.network.NetworkResult
import com.devanshdroid.disasterbuddy.data.model.AuthResponse
import com.devanshdroid.disasterbuddy.domain.usecase.LoginUseCase
import com.devanshdroid.disasterbuddy.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<NetworkResult<AuthResponse>?>(null)
    val authState: StateFlow<NetworkResult<AuthResponse>?> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                _authState.value = result
            }
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            registerUseCase(name, email, password, role).collect { result ->
                _authState.value = result
            }
        }
    }

    fun resetState() {
        _authState.value = null
    }
}