package com.tutushubham.studypartner.domain.usecase

import com.tutushubham.studypartner.domain.repository.OnboardingRepository

class CompleteOnboarding(private val onboardingRepository: OnboardingRepository) {
    suspend operator fun invoke() = onboardingRepository.setOnboardingCompleted(true)
}
