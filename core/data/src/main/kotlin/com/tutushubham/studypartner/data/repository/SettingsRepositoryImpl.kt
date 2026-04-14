package com.tutushubham.studypartner.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.tutushubham.studypartner.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    override fun observeNotificationsEnabled(): Flow<Boolean> =
        dataStore.data.map { it[KEY_NOTIFICATIONS] != false }.distinctUntilChanged()

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_NOTIFICATIONS] = enabled }
    }
}
