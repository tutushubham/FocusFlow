package com.tutushubham.studypartner.domain.model

import com.tutushubham.studypartner.model.MatchStatus

data class StudySessionMatch(
    val id: String,
    val requestId: String,
    val ownerUserId: String,
    val partnerUserId: String,
    val scheduledStartEpochMillis: Long,
    val scheduledEndEpochMillis: Long,
    val status: MatchStatus,
    val subjectLabel: String,
)
