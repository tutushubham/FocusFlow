@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.tutushubham.studypartner.feature.partner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.model.StudyLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class PartnerFilters(
    val search: String = "",
    val searchFieldVisible: Boolean = false,
    val subjectRowVisible: Boolean = false,
    val levelRowVisible: Boolean = false,
    val allPartners: Boolean = true,
    val subjectId: String? = null,
    val level: StudyLevel? = null,
)

data class PartnerUiState(
    val search: String = "",
    val searchFieldVisible: Boolean = false,
    val subjectRowVisible: Boolean = false,
    val levelRowVisible: Boolean = false,
    val filterAllPartners: Boolean = true,
    val selectedSubjectId: String? = null,
    val subjectPresets: List<SubjectTag> = DEFAULT_SUBJECT_PRESETS,
    val filterLevel: StudyLevel? = null,
    val requests: List<StudySessionRequest> = emptyList(),
)

private val DEFAULT_SUBJECT_PRESETS =
    listOf(
        SubjectTag("cs", "Computer Science"),
        SubjectTag("math", "Mathematics"),
        SubjectTag("physics", "Physics"),
    )

sealed interface PartnerIntent {
    data class Search(val q: String) : PartnerIntent

    data object ToggleSearchField : PartnerIntent

    data object ToggleAllPartners : PartnerIntent

    data object ToggleSubjectRow : PartnerIntent

    data object ToggleLevelRow : PartnerIntent

    data object TimeFilterStub : PartnerIntent

    data class PickSubject(val subjectId: String) : PartnerIntent

    data class SetLevel(val level: StudyLevel?) : PartnerIntent

    data class StartSession(val requestId: String) : PartnerIntent
}

sealed interface PartnerEffect {
    data object JoinRequestSent : PartnerEffect
}

@HiltViewModel
class PartnerViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val filters = MutableStateFlow(PartnerFilters())
    private val _effects = Channel<PartnerEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<PartnerUiState> =
        combine(filters, authRepository.observeCurrentUserId()) { f, uid ->
            Pair(f, uid)
        }.flatMapLatest { (f, uid) ->
            sessionRepository.observePostedRequests(uid).map { requests ->
                PartnerUiState(
                    search = f.search,
                    searchFieldVisible = f.searchFieldVisible,
                    subjectRowVisible = f.subjectRowVisible,
                    levelRowVisible = f.levelRowVisible,
                    filterAllPartners = f.allPartners,
                    selectedSubjectId = f.subjectId,
                    subjectPresets = DEFAULT_SUBJECT_PRESETS,
                    filterLevel = f.level,
                    requests =
                        requests.filter { r ->
                            val textOk =
                                f.search.isBlank() ||
                                    r.subject.label.contains(f.search, true) ||
                                    r.description.contains(f.search, true) ||
                                    r.ownerDisplayName.contains(f.search, true)
                            val subjectOk = f.allPartners || f.subjectId == r.subject.id
                            val levelOk = f.level == null || r.level == f.level
                            textOk && subjectOk && levelOk
                        },
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PartnerUiState())

    fun onIntent(intent: PartnerIntent) {
        when (intent) {
            is PartnerIntent.Search -> filters.update { it.copy(search = intent.q) }
            PartnerIntent.ToggleSearchField ->
                filters.update { it.copy(searchFieldVisible = !it.searchFieldVisible) }
            PartnerIntent.ToggleAllPartners ->
                filters.update { cur ->
                    val nextAll = !cur.allPartners
                    cur.copy(
                        allPartners = nextAll,
                        subjectId = if (nextAll) null else (cur.subjectId ?: DEFAULT_SUBJECT_PRESETS.first().id),
                        subjectRowVisible = if (nextAll) false else cur.subjectRowVisible,
                    )
                }
            PartnerIntent.ToggleSubjectRow ->
                filters.update {
                    it.copy(
                        subjectRowVisible = !it.subjectRowVisible,
                        levelRowVisible = false,
                    )
                }
            PartnerIntent.ToggleLevelRow ->
                filters.update {
                    it.copy(
                        levelRowVisible = !it.levelRowVisible,
                        subjectRowVisible = false,
                    )
                }
            PartnerIntent.TimeFilterStub -> { /* Stitch Time pill — no backend filter yet */ }
            is PartnerIntent.PickSubject ->
                filters.update {
                    it.copy(allPartners = false, subjectId = intent.subjectId)
                }
            is PartnerIntent.SetLevel -> filters.update { it.copy(level = intent.level) }
            is PartnerIntent.StartSession ->
                viewModelScope.launch {
                    val uid = authRepository.observeCurrentUserId().first() ?: return@launch
                    sessionRepository.requestJoin(intent.requestId, uid) ?: return@launch
                    _effects.send(PartnerEffect.JoinRequestSent)
                }
        }
    }
}
