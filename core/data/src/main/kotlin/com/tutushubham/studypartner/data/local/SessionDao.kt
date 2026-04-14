package com.tutushubham.studypartner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutushubham.studypartner.model.SessionRequestStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query(
        "SELECT * FROM session_requests WHERE status = 'Posted' AND (:exclude IS NULL OR ownerUserId != :exclude) ORDER BY startEpochMillis ASC",
    )
    fun observePosted(exclude: String?): Flow<List<SessionRequestEntity>>

    @Query("SELECT * FROM session_requests WHERE ownerUserId = :userId ORDER BY startEpochMillis DESC")
    fun observeMine(userId: String): Flow<List<SessionRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRequest(entity: SessionRequestEntity)

    @Query("SELECT * FROM session_requests WHERE id = :id LIMIT 1")
    suspend fun getRequest(id: String): SessionRequestEntity?

    @Query("UPDATE session_requests SET status = :status WHERE id = :id")
    suspend fun updateRequestStatus(id: String, status: SessionRequestStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMatch(entity: MatchEntity)

    @Query("SELECT * FROM session_matches WHERE id = :id LIMIT 1")
    fun observeMatch(id: String): Flow<MatchEntity?>

    @Query("SELECT * FROM session_matches WHERE id = :id LIMIT 1")
    suspend fun getMatch(id: String): MatchEntity?

    @Query(
        "SELECT * FROM session_matches WHERE (ownerUserId = :userId OR partnerUserId = :userId) AND status IN ('PendingConfirmation','Active') ORDER BY scheduledStartEpochMillis ASC",
    )
    fun observeActiveMatches(userId: String): Flow<List<MatchEntity>>

    @Query(
        "SELECT * FROM session_matches WHERE ownerUserId = :ownerUserId AND status = 'PendingConfirmation' ORDER BY scheduledStartEpochMillis ASC",
    )
    fun observePendingJoinsForOwner(ownerUserId: String): Flow<List<MatchEntity>>

    @Query("UPDATE session_matches SET status = :status WHERE id = :id")
    suspend fun updateMatchStatus(id: String, status: com.tutushubham.studypartner.model.MatchStatus)

}
