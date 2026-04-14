package com.tutushubham.studypartner.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isRegister: Boolean = false,
    val error: String? = null,
    val loading: Boolean = false,
    /** Stitch: email/password after “Continue with Email”. */
    val emailFormVisible: Boolean = false,
)

sealed interface AuthIntent {
    data class Email(val value: String) : AuthIntent
    data class Password(val value: String) : AuthIntent
    data object ToggleRegister : AuthIntent
    data object Submit : AuthIntent
    data object GoogleStub : AuthIntent
    data object ShowEmailForm : AuthIntent
}

sealed interface AuthEffect {
    data object NavigateProfileSetup : AuthEffect
    data object NavigateMain : AuthEffect
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val isProfileComplete: com.tutushubham.studypartner.domain.usecase.IsProfileComplete,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()
    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Email -> _state.update { it.copy(email = intent.value, error = null) }
            is AuthIntent.Password -> _state.update { it.copy(password = intent.value, error = null) }
            AuthIntent.ToggleRegister -> _state.update { it.copy(isRegister = !it.isRegister, error = null) }
            AuthIntent.GoogleStub -> _state.update { it.copy(error = "Google sign-in coming soon") }
            AuthIntent.ShowEmailForm -> _state.update { it.copy(emailFormVisible = true, error = null) }
            AuthIntent.Submit -> submit()
        }
    }

    private fun submit() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val result =
                if (s.isRegister) {
                    authRepository.register(s.email, s.password)
                } else {
                    authRepository.login(s.email, s.password)
                }
            _state.update { it.copy(loading = false) }
            if (result.isFailure) {
                _state.update { it.copy(error = result.exceptionOrNull()?.message ?: "Error") }
            } else {
                val complete = isProfileComplete()
                _effects.send(if (complete) AuthEffect.NavigateMain else AuthEffect.NavigateProfileSetup)
            }
        }
    }
}
