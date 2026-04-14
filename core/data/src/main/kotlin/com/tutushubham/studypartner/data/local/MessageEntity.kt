package com.tutushubham.studypartner.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("sessionMatchId")],
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val sessionMatchId: String,
    val senderUserId: String,
    val text: String,
    val sentAtEpochMillis: Long,
)
