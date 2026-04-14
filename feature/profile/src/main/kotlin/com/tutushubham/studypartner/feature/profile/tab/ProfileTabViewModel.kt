package com.tutushubham.studypartner.feature.profile.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileTabUiState(
    val user: User? = null,
)

@HiltViewModel
class ProfileTabViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val state: StateFlow<ProfileTabUiState> =
        userRepository.observeCurrentUser().map { u -> ProfileTabUiState(user = u) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileTabUiState())

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
