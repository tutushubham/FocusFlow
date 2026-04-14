@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.tutushubham.studypartner.data.repository

import com.tutushubham.studypartner.data.local.MatchEntity
import com.tutushubham.studypartner.data.local.SessionDao
import com.tutushubham.studypartner.data.mapper.toDomain
import com.tutushubham.studypartner.data.mapper.toEntity
import com.tutushubham.studypartner.domain.model.StudySessionMatch
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.model.MatchStatus
import com.tutushubham.studypartner.model.SessionRequestStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository,
) : SessionRepository {
    override fun observePostedRequests(excludeUserId: String?): Flow<List<StudySessionRequest>> =
        sessionDao.observePosted(excludeUserId).mapLatest { entities ->
            coroutineScope {
                entities
                    .map { e ->
                        async {
                            val owner = userRepository.getUserById(e.ownerUserId)
                            val base = e.toDomain()
                            base.copy(
                                ownerDisplayName =
                                    owner?.name?.takeIf { it.isNotBlank() } ?: "Study partner",
                                ownerPhotoUrl = owner?.photoUrl,
                                ownerBioPreview =
                                    owner?.bio
                                        ?.lines()
                                        ?.firstOrNull()
                                        .orEmpty()
                                        .take(120),
                            )
                        }
                    }.awaitAll()
            }
        }

    override fun observeMyRequests(userId: String): Flow<List<StudySessionRequest>> =
        sessionDao.observeMine(userId).mapLatest { entities ->
            coroutineScope {
                entities
                    .map { e ->
                        async {
                            val owner = userRepository.getUserById(e.ownerUserId)
                            val base = e.toDomain()
                            base.copy(
                                ownerDisplayName =
                                    owner?.name?.takeIf { it.isNotBlank() } ?: "Study partner",
                                ownerPhotoUrl = owner?.photoUrl,
                                ownerBioPreview =
                                    owner?.bio
                                        ?.lines()
                                        ?.firstOrNull()
                                        .orEmpty()
                                        .take(120),
                            )
                        }
                    }.awaitAll()
            }
        }

    override fun observeMatch(matchId: String): Flow<StudySessionMatch?> =
        sessionDao.observeMatch(matchId).map { it?.toDomain() }

    override fun observeActiveOrUpcomingMatchForUser(userId: String): Flow<StudySessionMatch?> =
        sessionDao.observeActiveMatches(userId).map { list -> list.firstOrNull()?.toDomain() }

    override fun observePendingJoinsAsOwner(ownerUserId: String): Flow<List<StudySessionMatch>> =
        sessionDao.observePendingJoinsForOwner(ownerUserId).map { list -> list.map { it.toDomain() } }

    override fun observeActiveOrPendingMatchesForUser(userId: String): Flow<List<StudySessionMatch>> =
        sessionDao.observeActiveMatches(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun createDraftRequest(request: StudySessionRequest) {
        sessionDao.upsertRequest(request.toEntity())
    }

    override suspend fun postRequest(requestId: String) {
        val r = sessionDao.getRequest(requestId) ?: return
        sessionDao.upsertRequest(r.copy(status = SessionRequestStatus.Posted))
    }

    override suspend fun requestJoin(requestId: String, partnerUserId: String): String? {
        val r = sessionDao.getRequest(requestId) ?: return null
        if (r.status != SessionRequestStatus.Posted) return null
        val end = r.startEpochMillis + r.durationMinutes * 60_000L
        val match =
            MatchEntity(
                id = UUID.randomUUID().toString(),
                requestId = requestId,
                ownerUserId = r.ownerUserId,
                partnerUserId = partnerUserId,
                scheduledStartEpochMillis = r.startEpochMillis,
                scheduledEndEpochMillis = end,
                status = MatchStatus.PendingConfirmation,
                subjectLabel = r.subjectLabel,
            )
        sessionDao.upsertMatch(match)
        sessionDao.upsertRequest(r.copy(status = SessionRequestStatus.JoinRequested))
        return match.id
    }

    override suspend fun acceptJoin(matchId: String) {
        val m = sessionDao.getMatch(matchId) ?: return
        sessionDao.updateMatchStatus(matchId, MatchStatus.Active)
        sessionDao.updateRequestStatus(m.requestId, SessionRequestStatus.Active)
    }

    override suspend fun declineJoin(matchId: String) {
        val m = sessionDao.getMatch(matchId) ?: return
        sessionDao.updateMatchStatus(matchId, MatchStatus.Cancelled)
        sessionDao.updateRequestStatus(m.requestId, SessionRequestStatus.Posted)
    }

    override suspend fun finalizeMatchIfEnded(matchId: String, nowEpochMillis: Long) {
        val m = sessionDao.getMatch(matchId) ?: return
        if (m.scheduledEndEpochMillis <= nowEpochMillis && m.status == MatchStatus.Active) {
            sessionDao.updateMatchStatus(matchId, MatchStatus.Completed)
            sessionDao.updateRequestStatus(m.requestId, SessionRequestStatus.Completed)
        }
    }
}
