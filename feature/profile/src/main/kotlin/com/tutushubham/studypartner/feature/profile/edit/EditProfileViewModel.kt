package com.tutushubham.studypartner.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.feature.profile.setup.StitchPresetSubjects
import com.tutushubham.studypartner.model.StudyTimeOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val name: String = "",
    val examPreparingFor: String = "",
    val selectedSubjects: Set<String> = emptySet(),
    val dailyGoalHours: Float = 2f,
    val studyTimes: Set<StudyTimeOfDay> = emptySet(),
    val bio: String = "",
)

sealed interface EditProfileIntent {
    data class Name(val v: String) : EditProfileIntent
    data class Exam(val v: String) : EditProfileIntent
    data class ToggleSubject(val label: String) : EditProfileIntent
    data class DailyGoal(val v: Float) : EditProfileIntent
    data class ToggleStudyTime(val v: StudyTimeOfDay) : EditProfileIntent
    data class Bio(val v: String) : EditProfileIntent
    data object Save : EditProfileIntent
}

sealed interface EditProfileEffect {
    data object Done : EditProfileEffect
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileUiState())
    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()
    private val _effects = Channel<EditProfileEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val u = userRepository.observeCurrentUser().filterNotNull().first()
            _state.update {
                EditProfileUiState(
                    name = u.name,
                    examPreparingFor = u.examPreparingFor,
                    selectedSubjects =
                        u.subjects.map { s -> s.label }.filter { s -> s in StitchPresetSubjects }.toSet(),
                    dailyGoalHours = u.dailyStudyGoalHours,
                    studyTimes = u.studyTimePreferences,
                    bio = u.bio,
                )
            }
        }
    }

    fun onIntent(intent: EditProfileIntent) {
        when (intent) {
            is EditProfileIntent.Name -> _state.update { it.copy(name = intent.v) }
            is EditProfileIntent.Exam -> _state.update { it.copy(examPreparingFor = intent.v) }
            is EditProfileIntent.ToggleSubject ->
                _state.update { s ->
                    val next = s.selectedSubjects.toMutableSet()
                    if (!next.add(intent.label)) next.remove(intent.label)
                    s.copy(selectedSubjects = next)
                }
            is EditProfileIntent.DailyGoal -> _state.update { it.copy(dailyGoalHours = intent.v.coerceIn(1f, 10f)) }
            is EditProfileIntent.ToggleStudyTime ->
                _state.update { s ->
                    val next = s.studyTimes.toMutableSet()
                    if (!next.add(intent.v)) next.remove(intent.v)
                    s.copy(studyTimes = next)
                }
            is EditProfileIntent.Bio -> _state.update { it.copy(bio = intent.v) }
            EditProfileIntent.Save -> save()
        }
    }

    private fun save() {
        viewModelScope.launch {
            val uid = authRepository.observeCurrentUserId().first() ?: return@launch
            val cur = userRepository.observeCurrentUser().first() ?: return@launch
            val s = _state.value
            val presetSubjects =
                s.selectedSubjects.map { label ->
                    SubjectTag(id = label.lowercase().replace(' ', '-'), label = label)
                }
            val extraFromUser =
                cur.subjects.filter { sub -> sub.label !in StitchPresetSubjects }
            val subjects = (presetSubjects + extraFromUser).distinctBy { it.id }
            userRepository.saveUser(
                User(
                    id = uid,
                    name = s.name,
                    email = cur.email,
                    photoUrl = cur.photoUrl,
                    examPreparingFor = s.examPreparingFor,
                    bio = s.bio,
                    subjects = subjects,
                    studyTimePreferences = s.studyTimes,
                    dailyStudyGoalHours = s.dailyGoalHours,
                    experienceLevel = cur.experienceLevel,
                    timezone = cur.timezone,
                    sessionsCompleted = cur.sessionsCompleted,
                    totalStudyHours = cur.totalStudyHours,
                    streakDays = cur.streakDays,
                ),
            )
            _effects.send(EditProfileEffect.Done)
        }
    }
}
