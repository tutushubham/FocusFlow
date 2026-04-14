package com.tutushubham.studypartner.data.repository

import com.tutushubham.studypartner.data.local.MessageDao
import com.tutushubham.studypartner.data.local.MessageEntity
import com.tutushubham.studypartner.data.mapper.toDomain
import com.tutushubham.studypartner.domain.model.Message
import com.tutushubham.studypartner.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
) : ChatRepository {
    override fun observeMessages(matchId: String): Flow<List<Message>> =
        messageDao.observeForMatch(matchId).map { list -> list.map { it.toDomain() } }

    override suspend fun sendMessage(matchId: String, senderUserId: String, text: String) {
        messageDao.insert(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                sessionMatchId = matchId,
                senderUserId = senderUserId,
                text = text,
                sentAtEpochMillis = System.currentTimeMillis(),
            ),
        )
    }
}
