package com.tutushubham.studypartner.data.repository

import com.tutushubham.studypartner.data.local.UserDao
import com.tutushubham.studypartner.data.mapper.toDomain
import com.tutushubham.studypartner.data.mapper.toEntity
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authRepository: AuthRepository,
) : UserRepository {
    override fun observeCurrentUser(): Flow<User?> =
        authRepository.observeCurrentUserId().flatMapLatest { id ->
            if (id == null) flowOf(null) else userDao.observeUser(id).map { it?.toDomain() }
        }

    override suspend fun getUserById(id: String): User? = userDao.getUser(id)?.toDomain()

    override suspend fun saveUser(user: User) {
        userDao.upsert(user.toEntity())
    }

    override suspend fun isProfileComplete(): Boolean {
        val id = authRepository.observeCurrentUserId().first()
        if (id == null) return false
        val entity = userDao.getUser(id) ?: return false
        return entity.name.isNotBlank() &&
            entity.subjectsJson != "[]" &&
            entity.bio.isNotBlank()
    }
}
