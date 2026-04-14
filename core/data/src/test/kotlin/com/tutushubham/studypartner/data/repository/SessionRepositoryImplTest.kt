package com.tutushubham.studypartner.data.repository

import com.tutushubham.studypartner.data.local.SessionDao
import com.tutushubham.studypartner.data.local.SessionRequestEntity
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.model.MatchStatus
import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class SessionRepositoryImplTest {
    @Test
    fun requestJoinCreatesPendingMatchWithoutAutoActivating() =
        runTest {
            val sessionDao = mockk<SessionDao>(relaxed = true)
            val userRepository = mockk<UserRepository>(relaxed = true)
            coEvery { sessionDao.getRequest("req-1") } returns
                SessionRequestEntity(
                    id = "req-1",
                    ownerUserId = "owner-1",
                    subjectId = "cs",
                    subjectLabel = "Computer Science",
                    title = null,
                    description = "Note",
                    startEpochMillis = 1_800_000L,
                    durationMinutes = 60,
                    level = StudyLevel.Intermediate,
                    status = SessionRequestStatus.Posted,
                    isPublic = true,
                )
            val impl = SessionRepositoryImpl(sessionDao, userRepository)
            val matchId = impl.requestJoin("req-1", "partner-1")
            assertNotNull(matchId)
            coVerify(exactly = 1) { sessionDao.upsertMatch(any()) }
            coVerify(exactly = 1) { sessionDao.upsertRequest(any()) }
            coVerify(exactly = 0) { sessionDao.updateMatchStatus(any(), MatchStatus.Active) }
        }
}
