package com.tutushubham.studypartner.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.tutushubham.studypartner.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")

@Singleton
class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : OnboardingRepository {
    override fun observeOnboardingCompleted(): Flow<Boolean> =
        dataStore.data.map { it[KEY_ONBOARDING_DONE] == true }.distinctUntilChanged()

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[KEY_ONBOARDING_DONE] = completed }
    }
}
