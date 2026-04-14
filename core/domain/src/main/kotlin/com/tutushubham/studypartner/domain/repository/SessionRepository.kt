package com.tutushubham.studypartner.domain.repository

import com.tutushubham.studypartner.domain.model.StudySessionMatch
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observePostedRequests(excludeUserId: String?): Flow<List<StudySessionRequest>>
    fun observeMyRequests(userId: String): Flow<List<StudySessionRequest>>
    fun observeMatch(matchId: String): Flow<StudySessionMatch?>
    fun observeActiveOrUpcomingMatchForUser(userId: String): Flow<StudySessionMatch?>
    fun observeActiveOrPendingMatchesForUser(userId: String): Flow<List<StudySessionMatch>>
    fun observePendingJoinsAsOwner(ownerUserId: String): Flow<List<StudySessionMatch>>
    suspend fun createDraftRequest(request: StudySessionRequest)
    suspend fun postRequest(requestId: String)
    /** Creates a match in `PendingConfirmation`; owner must [acceptJoin] before chat is active. */
    suspend fun requestJoin(requestId: String, partnerUserId: String): String?
    suspend fun acceptJoin(matchId: String)
    suspend fun declineJoin(matchId: String)
    suspend fun finalizeMatchIfEnded(matchId: String, nowEpochMillis: Long)
}
