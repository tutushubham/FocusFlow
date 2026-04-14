package com.tutushubham.studypartner.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)

    fun observeCloudSyncEnabled(): Flow<Boolean>
    suspend fun setCloudSyncEnabled(enabled: Boolean)
}
