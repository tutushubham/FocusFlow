package com.tutushubham.studypartner.feature.profile.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
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

internal val StitchPresetSubjects =
    listOf("Mathematics", "Physics", "Computer Science", "History", "Biology", "Chemistry")

internal val StitchExamOptions =
    listOf(
        "SAT / ACT",
        "LSAT",
        "MCAT",
        "GRE / GMAT",
        "CFA / CPA",
        "Other Professional Exam",
    )

data class ProfileSetupUiState(
    val step: Int = 0,
    val name: String = "",
    val examPreparingFor: String = "",
    val selectedSubjects: Set<String> = emptySet(),
    val dailyGoalHours: Float = 4.5f,
    val studyTimes: Set<StudyTimeOfDay> = setOf(StudyTimeOfDay.Morning),
)

sealed interface ProfileSetupIntent {
    data class Name(val v: String) : ProfileSetupIntent
    data class Exam(val v: String) : ProfileSetupIntent
    data class ToggleSubject(val label: String) : ProfileSetupIntent
    data class DailyGoal(val v: Float) : ProfileSetupIntent
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
                            examPreparingFor = u.examPreparingFor,
                            selectedSubjects =
                                u.subjects.map { s -> s.label }.filter { s -> s in StitchPresetSubjects }.toSet(),
                            dailyGoalHours = u.dailyStudyGoalHours,
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
            is ProfileSetupIntent.Exam -> _state.update { it.copy(examPreparingFor = intent.v) }
            is ProfileSetupIntent.ToggleSubject ->
                _state.update { s ->
                    val next = s.selectedSubjects.toMutableSet()
                    if (!next.add(intent.label)) next.remove(intent.label)
                    s.copy(selectedSubjects = next)
                }
            is ProfileSetupIntent.DailyGoal ->
                _state.update { it.copy(dailyGoalHours = intent.v.coerceIn(1f, 10f)) }
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
                s.selectedSubjects.map { label ->
                    SubjectTag(id = label.lowercase().replace(' ', '-'), label = label)
                }
            val updated =
                User(
                    id = uid,
                    name = s.name,
                    email = cur.email,
                    photoUrl = cur.photoUrl,
                    examPreparingFor = s.examPreparingFor,
                    bio = cur.bio,
                    subjects = subjects,
                    studyTimePreferences = s.studyTimes,
                    dailyStudyGoalHours = s.dailyGoalHours,
                    experienceLevel = cur.experienceLevel,
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
