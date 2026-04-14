package com.tutushubham.studypartner.domain.model

data class Message(
    val id: String,
    val sessionMatchId: String,
    val senderUserId: String,
    val text: String,
    val sentAtEpochMillis: Long,
)
