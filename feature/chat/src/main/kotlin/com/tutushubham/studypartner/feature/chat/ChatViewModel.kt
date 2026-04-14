@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.tutushubham.studypartner.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.Message
import com.tutushubham.studypartner.domain.model.StudySessionMatch
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.ChatRepository
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.model.MatchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class ChatCore(
    val match: StudySessionMatch?,
    val currentUserId: String?,
    val messages: List<Message>,
    val nowMillis: Long,
    val inputText: String,
)

data class ChatUiState(
    val subject: String = "",
    val partnerName: String = "Partner",
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val nowMillis: Long = System.currentTimeMillis(),
    val scheduledStart: Long = 0L,
    val scheduledEnd: Long = 0L,
    val matchStatus: MatchStatus = MatchStatus.Active,
    val currentUserId: String? = null,
    val canSend: Boolean = false,
    val sessionEnded: Boolean = false,
    val preStartMessage: String? = null,
)

sealed interface ChatIntent {
    data class Input(val value: String) : ChatIntent

    data object Send : ChatIntent

    data object QuickFocus : ChatIntent

    data object QuickBreak : ChatIntent
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val matchId: String = checkNotNull(savedStateHandle["matchId"])

    private val clock = MutableStateFlow(System.currentTimeMillis())
    private val inputText = MutableStateFlow("")
    private var ticker: Job? = null

    private val coreFlow =
        combine(
            sessionRepository.observeMatch(matchId),
            authRepository.observeCurrentUserId(),
            chatRepository.observeMessages(matchId),
            clock,
            inputText,
        ) { match, uid, messages, now, input ->
            ChatCore(match, uid, messages, now, input)
        }

    val state: StateFlow<ChatUiState> =
        coreFlow
            .mapLatest { core ->
                val start = core.match?.scheduledStartEpochMillis ?: 0L
                val end = core.match?.scheduledEndEpochMillis ?: 0L
                val status = core.match?.status ?: MatchStatus.Completed
                val now = core.nowMillis
                val inWindow = now in start..end && status == MatchStatus.Active
                val ended = now > end || status == MatchStatus.Completed || status == MatchStatus.Expired
                val preStart =
                    when {
                        ended -> null
                        status == MatchStatus.PendingConfirmation ->
                            "Waiting for the host to confirm this session."
                        now < start -> "Chat opens at scheduled start."
                        else -> null
                    }
                val partnerName =
                    if (core.match == null || core.currentUserId == null) {
                        "Partner"
                    } else {
                        val pid =
                            if (core.currentUserId == core.match.ownerUserId) {
                                core.match.partnerUserId
                            } else {
                                core.match.ownerUserId
                            }
                        userRepository.getUserById(pid)?.name?.takeIf { it.isNotBlank() } ?: "Partner"
                    }
                ChatUiState(
                    subject = core.match?.subjectLabel.orEmpty(),
                    partnerName = partnerName,
                    messages = core.messages,
                    inputText = core.inputText,
                    nowMillis = now,
                    scheduledStart = start,
                    scheduledEnd = end,
                    matchStatus = status,
                    currentUserId = core.currentUserId,
                    canSend = inWindow,
                    sessionEnded = ended,
                    preStartMessage = preStart,
                )
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ChatUiState())

    init {
        ticker =
            viewModelScope.launch {
                while (isActive) {
                    delay(1_000)
                    val now = System.currentTimeMillis()
                    clock.value = now
                    sessionRepository.finalizeMatchIfEnded(matchId, now)
                }
            }
    }

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.Input -> inputText.update { intent.value }
            ChatIntent.Send -> send()
            ChatIntent.QuickFocus -> inputText.value = "Let's stay focused"
            ChatIntent.QuickBreak -> inputText.value = "Break time?"
        }
    }

    private fun send() {
        viewModelScope.launch {
            val uid = authRepository.observeCurrentUserId().first() ?: return@launch
            val text = inputText.value.trim()
            if (text.isNotEmpty() && state.value.canSend) {
                chatRepository.sendMessage(matchId, uid, text)
                inputText.value = ""
            }
        }
    }

    override fun onCleared() {
        ticker?.cancel()
        super.onCleared()
    }
}
