package com.tutushubham.studypartner.domain.repository

import com.tutushubham.studypartner.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeMessages(matchId: String): Flow<List<Message>>
    suspend fun sendMessage(matchId: String, senderUserId: String, text: String)
}
