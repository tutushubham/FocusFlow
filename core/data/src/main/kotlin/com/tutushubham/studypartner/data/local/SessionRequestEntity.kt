package com.tutushubham.studypartner.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel

@Entity(
    tableName = "session_requests",
    indices = [Index("ownerUserId"), Index("status")],
)
data class SessionRequestEntity(
    @PrimaryKey val id: String,
    val ownerUserId: String,
    val subjectId: String,
    val subjectLabel: String,
    val title: String?,
    val description: String,
    val startEpochMillis: Long,
    val durationMinutes: Int,
    val level: StudyLevel,
    val status: SessionRequestStatus,
    val isPublic: Boolean,
)
