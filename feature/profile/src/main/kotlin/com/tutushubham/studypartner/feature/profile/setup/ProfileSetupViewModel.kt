package com.tutushubham.studypartner.feature.profile.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileSetupUiState(
    val step: Int = 0,
    val name: String = "",
    val subjectsCsv: String = "Computer Science, Math",
    val photoUrl: String = "",
    val dailyGoalHours: Float = 2f,
    val bio: String = "",
    val experienceLevel: StudyLevel = StudyLevel.Intermediate,
    val studyTimes: Set<StudyTimeOfDay> = setOf(StudyTimeOfDay.Morning, StudyTimeOfDay.Evening),
)

sealed interface ProfileSetupIntent {
    data class Name(val v: String) : ProfileSetupIntent
    data class SubjectsCsv(val v: String) : ProfileSetupIntent
    data class PhotoUrl(val v: String) : ProfileSetupIntent
    data class DailyGoal(val v: Float) : ProfileSetupIntent
    data class Bio(val v: String) : ProfileSetupIntent
    data class Experience(val v: StudyLevel) : ProfileSetupIntent
    data class ToggleStudyTime(val v: StudyTimeOfDay) : ProfileSetupIntent
    data object Next : ProfileSetupIntent
    data object Back : ProfileSetupIntent
    data object Finish : ProfileSetupIntent
}

sealed interface ProfileSetupEffect {
    data object NavigateMain : ProfileSetupEffect
}

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileSetupUiState())
    val state: StateFlow<ProfileSetupUiState> = _state.asStateFlow()
    private val _effects = Channel<ProfileSetupEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    private var seeded = false

    init {
        viewModelScope.launch {
            userRepository.observeCurrentUser().collect { u ->
                if (u != null && !seeded) {
                    seeded = true
                    _state.update {
                        it.copy(
                            name = u.name,
                            subjectsCsv = u.subjects.joinToString(", ") { s -> s.label },
                            photoUrl = u.photoUrl.orEmpty(),
                            dailyGoalHours = u.dailyStudyGoalHours,
                            bio = u.bio,
                            experienceLevel = u.experienceLevel,
                            studyTimes = u.studyTimePreferences,
                        )
                    }
                }
            }
        }
    }

    fun onIntent(intent: ProfileSetupIntent) {
        when (intent) {
            is ProfileSetupIntent.Name -> _state.update { it.copy(name = intent.v) }
            is ProfileSetupIntent.SubjectsCsv -> _state.update { it.copy(subjectsCsv = intent.v) }
            is ProfileSetupIntent.PhotoUrl -> _state.update { it.copy(photoUrl = intent.v) }
            is ProfileSetupIntent.DailyGoal -> _state.update { it.copy(dailyGoalHours = intent.v) }
            is ProfileSetupIntent.Bio -> _state.update { it.copy(bio = intent.v) }
            is ProfileSetupIntent.Experience -> _state.update { it.copy(experienceLevel = intent.v) }
            is ProfileSetupIntent.ToggleStudyTime ->
                _state.update { s ->
                    val next = s.studyTimes.toMutableSet()
                    if (!next.add(intent.v)) next.remove(intent.v)
                    s.copy(studyTimes = next)
                }
            ProfileSetupIntent.Next -> _state.update { it.copy(step = (it.step + 1).coerceAtMost(1)) }
            ProfileSetupIntent.Back -> _state.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }
            ProfileSetupIntent.Finish -> persist()
        }
    }

    private fun persist() {
        viewModelScope.launch {
            val uid = authRepository.observeCurrentUserId().first() ?: return@launch
            val cur = userRepository.observeCurrentUser().first() ?: return@launch
            val s = _state.value
            val subjects =
                s.subjectsCsv
                    .split(',')
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { label ->
                        SubjectTag(id = label.lowercase().replace(' ', '-'), label = label)
                    }
            val updated =
                User(
                    id = uid,
                    name = s.name,
                    email = cur.email,
                    photoUrl = s.photoUrl.ifBlank { null },
                    bio = s.bio,
                    subjects = subjects,
                    studyTimePreferences = s.studyTimes,
                    dailyStudyGoalHours = s.dailyGoalHours,
                    experienceLevel = s.experienceLevel,
                    timezone = cur.timezone,
                    sessionsCompleted = cur.sessionsCompleted,
                    totalStudyHours = cur.totalStudyHours,
                    streakDays = cur.streakDays,
                )
            userRepository.saveUser(updated)
            _effects.send(ProfileSetupEffect.NavigateMain)
        }
    }
}
