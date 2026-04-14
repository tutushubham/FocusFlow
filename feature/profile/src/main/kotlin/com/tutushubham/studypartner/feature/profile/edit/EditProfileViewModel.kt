package com.tutushubham.studypartner.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
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
    val subjectsCsv: String = "",
    val bio: String = "",
)

sealed interface EditProfileIntent {
    data class Name(val v: String) : EditProfileIntent
    data class SubjectsCsv(val v: String) : EditProfileIntent
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
                    subjectsCsv = u.subjects.joinToString(", ") { s -> s.label },
                    bio = u.bio,
                )
            }
        }
    }

    fun onIntent(intent: EditProfileIntent) {
        when (intent) {
            is EditProfileIntent.Name -> _state.update { it.copy(name = intent.v) }
            is EditProfileIntent.SubjectsCsv -> _state.update { it.copy(subjectsCsv = intent.v) }
            is EditProfileIntent.Bio -> _state.update { it.copy(bio = intent.v) }
            EditProfileIntent.Save -> save()
        }
    }

    private fun save() {
        viewModelScope.launch {
            val uid = authRepository.observeCurrentUserId().first() ?: return@launch
            val cur = userRepository.observeCurrentUser().first() ?: return@launch
            val s = _state.value
            val subjects =
                s.subjectsCsv.split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { label ->
                    SubjectTag(id = label.lowercase().replace(' ', '-'), label = label)
                }
            userRepository.saveUser(
                User(
                    id = uid,
                    name = s.name,
                    email = cur.email,
                    photoUrl = cur.photoUrl,
                    bio = s.bio,
                    subjects = subjects,
                    studyTimePreferences = cur.studyTimePreferences,
                    dailyStudyGoalHours = cur.dailyStudyGoalHours,
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
