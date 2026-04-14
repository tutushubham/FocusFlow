package com.tutushubham.studypartner.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tutushubham.studypartner.model.MatchStatus

@Entity(
    tableName = "session_matches",
    indices = [Index("ownerUserId"), Index("partnerUserId"), Index("status")],
)
data class MatchEntity(
    @PrimaryKey val id: String,
    val requestId: String,
    val ownerUserId: String,
    val partnerUserId: String,
    val scheduledStartEpochMillis: Long,
    val scheduledEndEpochMillis: Long,
    val status: MatchStatus,
    val subjectLabel: String,
)
