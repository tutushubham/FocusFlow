package com.tutushubham.studypartner.domain.usecase

import com.tutushubham.studypartner.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsProfileCompleteTest {
    @Test
    fun `returns repository value`() =
        runBlocking {
            val uc = IsProfileComplete(FakeUserRepo(true))
            assertTrue(uc())
            val uc2 = IsProfileComplete(FakeUserRepo(false))
            assertFalse(uc2())
        }

    private class FakeUserRepo(private val value: Boolean) : UserRepository {
        override fun observeCurrentUser(): Flow<com.tutushubham.studypartner.domain.model.User?> = flowOf(null)

        override suspend fun saveUser(user: com.tutushubham.studypartner.domain.model.User) = Unit

        override suspend fun isProfileComplete(): Boolean = value
    }
}
