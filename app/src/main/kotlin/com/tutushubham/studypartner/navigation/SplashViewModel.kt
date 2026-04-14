package com.tutushubham.studypartner.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.OnboardingRepository
import com.tutushubham.studypartner.domain.usecase.IsProfileComplete
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class SplashDestination {
    Onboarding,
    Auth,
    ProfileSetup,
    Main,
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    onboardingRepository: OnboardingRepository,
    authRepository: AuthRepository,
    private val isProfileComplete: IsProfileComplete,
) : ViewModel() {
    val destination =
        combine(
            onboardingRepository.observeOnboardingCompleted(),
            authRepository.isLoggedIn,
            authRepository.observeCurrentUserId(),
        ) { onboardingDone, loggedIn, userId ->
            Triple(onboardingDone, loggedIn, userId)
        }.flatMapLatest { (onboardingDone, loggedIn, userId) ->
            flow {
                when {
                    !onboardingDone -> emit(SplashDestination.Onboarding)
                    !loggedIn || userId == null -> emit(SplashDestination.Auth)
                    else -> {
                        val ok = isProfileComplete()
                        emit(if (ok) SplashDestination.Main else SplashDestination.ProfileSetup)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, SplashDestination.Onboarding)
}
