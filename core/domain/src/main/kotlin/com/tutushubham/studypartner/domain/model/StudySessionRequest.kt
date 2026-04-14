package com.tutushubham.studypartner.domain.model

import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel

data class StudySessionRequest(
    val id: String,
    val ownerUserId: String,
    val subject: SubjectTag,
    val title: String?,
    val description: String,
    val startEpochMillis: Long,
    val durationMinutes: Int,
    val level: StudyLevel,
    val status: SessionRequestStatus,
    val isPublic: Boolean,
    /** Enriched from `User` for partner cards; not persisted on the request row. */
    val ownerDisplayName: String = "",
    val ownerPhotoUrl: String? = null,
    val ownerBioPreview: String = "",
)
