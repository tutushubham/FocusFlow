package com.tutushubham.studypartner.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnboardingIntent {
    data object Complete : OnboardingIntent

    /** Stitch “Skip” — same completion as finishing onboarding. */
    data object Skip : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object NavigateAuth : OnboardingEffect
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _effects = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Complete,
            OnboardingIntent.Skip,
            ->
                viewModelScope.launch {
                    onboardingRepository.setOnboardingCompleted(true)
                    _effects.send(OnboardingEffect.NavigateAuth)
                }
        }
    }
}
