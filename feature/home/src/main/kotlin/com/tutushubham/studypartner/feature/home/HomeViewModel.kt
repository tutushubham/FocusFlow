@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.tutushubham.studypartner.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.model.MatchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PendingJoinCard(
    val matchId: String,
    val partnerName: String,
    val subjectLabel: String,
)

data class HomeUiState(
    val greetingName: String = "there",
    /** Progress ring 0–100 (Stitch “Today’s progress”). */
    val progressPercent: Int = 40,
    val totalHours: Float = 12f,
    val streakDays: Int = 0,
    val dailyGoalHours: Float = 6f,
    /** Hours shown in the primary stats card toward today’s goal (derived). */
    val hoursTowardDailyGoal: Float = 0f,
    val upcomingTitle: String? = null,
    val activeMatchId: String? = null,
    val pendingJoins: List<PendingJoinCard> = emptyList(),
)

sealed interface HomeIntent {
    data class AcceptJoin(val matchId: String) : HomeIntent
    data class DeclineJoin(val matchId: String) : HomeIntent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    private val pendingJoinsFlow =
        authRepository.observeCurrentUserId().flatMapLatest { uid ->
            if (uid == null) {
                flowOf(emptyList())
            } else {
                sessionRepository.observePendingJoinsAsOwner(uid).mapLatest { matches ->
                    coroutineScope {
                        matches
                            .map { m ->
                                async {
                                    val partner = userRepository.getUserById(m.partnerUserId)
                                    PendingJoinCard(
                                        matchId = m.id,
                                        partnerName =
                                            partner?.name?.takeIf { it.isNotBlank() } ?: "Partner",
                                        subjectLabel = m.subjectLabel,
                                    )
                                }
                            }.awaitAll()
                    }
                }
            }
        }

    private val matchesSummaryFlow =
        authRepository.observeCurrentUserId().flatMapLatest { uid ->
            if (uid == null) {
                flowOf(null to null)
            } else {
                sessionRepository.observeActiveOrPendingMatchesForUser(uid).mapLatest { matches ->
                    val active =
                        matches
                            .filter { it.status == MatchStatus.Active }
                            .minByOrNull { it.scheduledStartEpochMillis }
                    if (active != null) {
                        val other =
                            userRepository.getUserById(
                                if (active.ownerUserId == uid) active.partnerUserId else active.ownerUserId,
                            )
                        val name = other?.name?.takeIf { it.isNotBlank() } ?: "Partner"
                        "Active: ${active.subjectLabel} with $name" to active.id
                    } else {
                        val pending =
                            matches
                                .filter { it.status == MatchStatus.PendingConfirmation }
                                .minByOrNull { it.scheduledStartEpochMillis }
                        if (pending != null) {
                            val other =
                                userRepository.getUserById(
                                    if (pending.ownerUserId == uid) pending.partnerUserId else pending.ownerUserId,
                                )
                            val name = other?.name?.takeIf { it.isNotBlank() } ?: "Partner"
                            "Awaiting confirmation: ${pending.subjectLabel} · $name" to pending.id
                        } else {
                            null to null
                        }
                    }
                }
            }
        }

    val state =
        combine(
            userRepository.observeCurrentUser(),
            pendingJoinsFlow,
            matchesSummaryFlow,
        ) { user, pending, summary ->
            val name = user?.name?.takeIf { it.isNotBlank() } ?: "there"
            val goal = user?.dailyStudyGoalHours?.takeIf { it > 0.05f } ?: 6f
            val total = user?.totalStudyHours ?: 0f
            val toward = total % goal
            val pct =
                if (goal > 0f) {
                    ((toward / goal) * 100f).toInt().coerceIn(12, 94)
                } else {
                    40
                }
            HomeUiState(
                greetingName = name,
                progressPercent = pct,
                totalHours = total,
                streakDays = user?.streakDays ?: 0,
                dailyGoalHours = goal,
                hoursTowardDailyGoal = toward,
                upcomingTitle = summary.first,
                activeMatchId = summary.second,
                pendingJoins = pending,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.AcceptJoin ->
                viewModelScope.launch {
                    sessionRepository.acceptJoin(intent.matchId)
                }
            is HomeIntent.DeclineJoin ->
                viewModelScope.launch {
                    sessionRepository.declineJoin(intent.matchId)
                }
        }
    }
}
