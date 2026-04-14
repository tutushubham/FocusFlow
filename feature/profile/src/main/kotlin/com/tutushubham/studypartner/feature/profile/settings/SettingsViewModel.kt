package com.tutushubham.studypartner.feature.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val cloudSyncEnabled: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val state: StateFlow<SettingsUiState> =
        combine(
            settingsRepository.observeNotificationsEnabled(),
            settingsRepository.observeCloudSyncEnabled(),
        ) { notifications, cloud ->
            SettingsUiState(notificationsEnabled = notifications, cloudSyncEnabled = cloud)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setNotifications(on: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(on) }
    }

    fun setCloudSync(on: Boolean) {
        viewModelScope.launch { settingsRepository.setCloudSyncEnabled(on) }
    }
}
