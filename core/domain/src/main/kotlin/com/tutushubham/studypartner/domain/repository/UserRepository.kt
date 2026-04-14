package com.tutushubham.studypartner.domain.repository

import com.tutushubham.studypartner.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeCurrentUser(): Flow<User?>
    suspend fun getUserById(id: String): User?
    suspend fun saveUser(user: User)
    suspend fun isProfileComplete(): Boolean
}
