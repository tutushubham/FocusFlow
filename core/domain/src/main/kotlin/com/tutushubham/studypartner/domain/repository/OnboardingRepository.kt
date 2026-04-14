package com.tutushubham.studypartner.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun observeOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
