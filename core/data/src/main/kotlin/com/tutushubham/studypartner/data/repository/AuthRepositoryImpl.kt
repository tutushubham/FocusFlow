package com.tutushubham.studypartner.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tutushubham.studypartner.data.local.UserDao
import com.tutushubham.studypartner.data.local.UserEntity
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
private val KEY_USER_ID = stringPreferencesKey("user_id")

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao,
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean> =
        dataStore.data.map { it[KEY_LOGGED_IN] == true }.distinctUntilChanged()

    override fun observeCurrentUserId(): Flow<String?> =
        dataStore.data.map { it[KEY_USER_ID] }.distinctUntilChanged()

    override suspend fun login(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.length < 6) return Result.failure(IllegalArgumentException("Invalid credentials"))
        val userId = ensureUserRow(email)
        dataStore.edit {
            it[KEY_LOGGED_IN] = true
            it[KEY_USER_ID] = userId
        }
        return Result.success(Unit)
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.length < 6) return Result.failure(IllegalArgumentException("Invalid input"))
        val userId = ensureUserRow(email)
        dataStore.edit {
            it[KEY_LOGGED_IN] = true
            it[KEY_USER_ID] = userId
        }
        return Result.success(Unit)
    }

    override suspend fun logout() {
        dataStore.edit {
            it[KEY_LOGGED_IN] = false
            it.remove(KEY_USER_ID)
        }
    }

    private suspend fun ensureUserRow(email: String): String {
        val id = UUID.nameUUIDFromBytes(email.lowercase().toByteArray()).toString()
        val existing = userDao.getUser(id)
        if (existing == null) {
            userDao.upsert(
                UserEntity(
                    id = id,
                    name = "",
                    email = email,
                    photoUrl = null,
                    examPreparingFor = "",
                    bio = "",
                    subjectsJson = "[]",
                    studyTimes = emptySet(),
                    dailyStudyGoalHours = 2f,
                    experienceLevel = StudyLevel.Beginner,
                    timezone = java.util.TimeZone.getDefault().id,
                    sessionsCompleted = 0,
                    totalStudyHours = 0f,
                    streakDays = 0,
                ),
            )
        }
        return id
    }
}
