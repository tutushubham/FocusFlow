package com.tutushubham.studypartner.feature.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

data class CreateSessionUiState(
    val subjectLabel: String = "Computer Science",
    /** Calendar day offset from today (0 = today). */
    val dayOffset: Int = 0,
    /** Minutes from midnight local for session start. */
    val startMinuteOfDay: Int = 14 * 60,
    val durationMinutes: Int = 90,
    val level: StudyLevel = StudyLevel.Intermediate,
    val note: String = "",
    val isPublic: Boolean = true,
)

sealed interface CreateSessionIntent {
    data class Subject(val v: String) : CreateSessionIntent

    data class DayOffset(val v: Int) : CreateSessionIntent

    data class StartHour(val hour: Int, val minute: Int) : CreateSessionIntent

    data class Duration(val v: Int) : CreateSessionIntent

    data class Level(val v: StudyLevel) : CreateSessionIntent

    data class Note(val v: String) : CreateSessionIntent

    data class Public(val v: Boolean) : CreateSessionIntent

    data object Submit : CreateSessionIntent
}

sealed interface CreateSessionEffect {
    data object Done : CreateSessionEffect
}

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateSessionUiState())
    val state: StateFlow<CreateSessionUiState> = _state.asStateFlow()
    private val _effects = Channel<CreateSessionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: CreateSessionIntent) {
        when (intent) {
            is CreateSessionIntent.Subject -> _state.update { it.copy(subjectLabel = intent.v) }
            is CreateSessionIntent.DayOffset ->
                _state.update { it.copy(dayOffset = intent.v.coerceIn(0, 14)) }
            is CreateSessionIntent.StartHour ->
                _state.update {
                    it.copy(startMinuteOfDay = (intent.hour * 60 + intent.minute).coerceIn(0, 24 * 60 - 1))
                }
            is CreateSessionIntent.Duration -> _state.update { it.copy(durationMinutes = intent.v.coerceIn(15, 480)) }
            is CreateSessionIntent.Level -> _state.update { it.copy(level = intent.v) }
            is CreateSessionIntent.Note -> _state.update { it.copy(note = intent.v) }
            is CreateSessionIntent.Public -> _state.update { it.copy(isPublic = intent.v) }
            CreateSessionIntent.Submit -> submit()
        }
    }

    private fun startEpochMillis(s: CreateSessionUiState): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, s.dayOffset)
        cal.set(Calendar.HOUR_OF_DAY, s.startMinuteOfDay / 60)
        cal.set(Calendar.MINUTE, s.startMinuteOfDay % 60)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun submit() {
        viewModelScope.launch {
            val uid = authRepository.observeCurrentUserId().first() ?: return@launch
            val s = _state.value
            val id = UUID.randomUUID().toString()
            val subject = SubjectTag(id = s.subjectLabel.lowercase().replace(' ', '-'), label = s.subjectLabel)
            val req =
                StudySessionRequest(
                    id = id,
                    ownerUserId = uid,
                    subject = subject,
                    title = null,
                    description = s.note.ifBlank { "Study session" },
                    startEpochMillis = startEpochMillis(s),
                    durationMinutes = s.durationMinutes,
                    level = s.level,
                    status = SessionRequestStatus.Draft,
                    isPublic = s.isPublic,
                )
            sessionRepository.createDraftRequest(req)
            sessionRepository.postRequest(id)
            _effects.send(CreateSessionEffect.Done)
        }
    }
}
