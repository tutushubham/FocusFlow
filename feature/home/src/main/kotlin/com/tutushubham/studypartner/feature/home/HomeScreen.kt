package com.tutushubham.studypartner.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.components.FocusFlowElevatedCard
import com.tutushubham.studypartner.core.ui.components.FocusFlowSectionLabel
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeRoute(
    onCreateSession: () -> Unit,
    onJoinStudyRoom: () -> Unit,
    onOpenChat: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    HomeScreenContent(
        state = state,
        onCreateSession = onCreateSession,
        onJoinStudyRoom = onJoinStudyRoom,
        onOpenChat = onOpenChat,
        onIntent = viewModel::onIntent,
    )
}

private fun greetingPeriod(): String =
    when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Morning"
        in 12..16 -> "Afternoon"
        in 17..20 -> "Evening"
        else -> "Night"
    }

private fun displayName(raw: String): String =
    raw.replaceFirstChar { c ->
        if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString()
    }

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onCreateSession: () -> Unit,
    onJoinStudyRoom: () -> Unit,
    onOpenChat: (String) -> Unit,
    onIntent: (HomeIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = FocusPrimary.copy(alpha = 0.1f),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        tint = FocusPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text("Focus Flow", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StitchPalette.OnlineGreen),
                    )
                    Text(
                        "12k studying now",
                        style = MaterialTheme.typography.labelMedium,
                        color = StitchPalette.Slate500,
                    )
                }
            }
            IconButton(onClick = { /* Stitch notifications */ }) {
                Surface(shape = RoundedCornerShape(8.dp), color = StitchPalette.Slate100) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = StitchPalette.Slate700,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                "Good ${greetingPeriod()}, ${displayName(state.greetingName)}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp),
                color = StitchPalette.Slate900,
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = StitchPalette.Orange100,
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Default.LocalFireDepartment, null, tint = StitchPalette.Orange700, modifier = Modifier.size(18.dp))
                    Text(
                        "Streak: ${state.streakDays} days 🔥",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = StitchPalette.Orange700,
                    )
                }
            }
        }
        Column(Modifier.padding(16.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, StitchPalette.Slate100),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("Today's progress", style = MaterialTheme.typography.bodyMedium, color = StitchPalette.Slate500)
                        Spacer(Modifier.height(4.dp))
                        Text("${state.progressPercent}%", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                        Text(
                            if (state.progressPercent >= 100) "Goal met!" else "Almost there!",
                            style = MaterialTheme.typography.bodySmall,
                            color = FocusPrimary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                        CircularProgressIndicator(
                            progress = { state.progressPercent / 100f },
                            modifier = Modifier.size(80.dp),
                            color = FocusPrimary,
                            trackColor = StitchPalette.Slate200,
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                        )
                        Icon(Icons.Default.AutoGraph, null, tint = FocusPrimary, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = FocusPrimary.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, FocusPrimary.copy(alpha = 0.2f)),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "TOTAL STUDY HOURS TODAY",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp),
                            color = StitchPalette.Slate700,
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                "%.1f".format(state.hoursTowardDailyGoal) + "h",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            )
                            Text(
                                " / ${"%.0f".format(state.dailyGoalHours)}h goal",
                                style = MaterialTheme.typography.bodySmall,
                                color = StitchPalette.Slate500,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                            )
                        }
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = FocusPrimary) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
            }
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onCreateSession,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PersonSearch, null)
                        Text("Create Study Session", fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
            OutlinedButton(
                onClick = onJoinStudyRoom,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, StitchPalette.Slate200),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = StitchPalette.Slate900),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Groups, null)
                        Text("Join Study Room", fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
        state.upcomingTitle?.let { title ->
            Column(Modifier.padding(horizontal = 16.dp)) {
                FocusFlowSectionLabel("Today's Focus Map")
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(StitchPalette.Slate200),
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(FocusPrimary.copy(alpha = 0.2f), Color.Transparent),
                                ),
                            ),
                    )
                    Icon(
                        Icons.Default.Map,
                        null,
                        tint = StitchPalette.Slate400,
                        modifier = Modifier.align(Alignment.Center).size(48.dp),
                    )
                    Surface(
                        modifier =
                            Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, StitchPalette.Slate200),
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text("CURRENT SESSION", style = MaterialTheme.typography.labelSmall, color = StitchPalette.Slate500, fontWeight = FontWeight.Bold)
                            Text(title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, maxLines = 2)
                        }
                    }
                }
                val activeId = state.activeMatchId
                if (activeId != null && title.startsWith("Active:")) {
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { onOpenChat(activeId) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Open chat")
                    }
                }
            }
        }
        if (state.pendingJoins.isNotEmpty()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FocusFlowSectionLabel("Join requests")
                state.pendingJoins.forEach { p ->
                    FocusFlowElevatedCard {
                        Text("${p.partnerName} wants to join · ${p.subjectLabel}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { onIntent(HomeIntent.AcceptJoin(p.matchId)) },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Accept")
                            }
                            OutlinedButton(
                                onClick = { onIntent(HomeIntent.DeclineJoin(p.matchId)) },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Decline")
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    FocusFlowTheme {
        HomeScreenContent(
            state =
                HomeUiState(
                    greetingName = "Alex",
                    progressPercent = 65,
                    totalHours = 18.5f,
                    streakDays = 7,
                    dailyGoalHours = 6f,
                    hoursTowardDailyGoal = 4.5f,
                    upcomingTitle = "Deep Work: Computer Science",
                    activeMatchId = "m1",
                    pendingJoins = emptyList(),
                ),
            onCreateSession = {},
            onJoinStudyRoom = {},
            onOpenChat = {},
            onIntent = {},
        )
    }
}
