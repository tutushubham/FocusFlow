package com.tutushubham.studypartner.feature.profile.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.layout.StitchMaxWidthColumn
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay

@Composable
fun ProfileTabRoute(
    onSettings: () -> Unit,
    onEditProfile: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: ProfileTabViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ProfileTabScreenContent(
        state = state,
        onSettings = onSettings,
        onEditProfile = onEditProfile,
        onLogout = {
            viewModel.logout()
            onLoggedOut()
        },
    )
}

private data class SubjectAccent(val container: Color, val onContainer: Color, val icon: ImageVector)

@Composable
private fun subjectAccent(label: String): SubjectAccent {
    val l = label.lowercase()
    return when {
        "math" in l || "calculus" in l ->
            SubjectAccent(
                StitchPalette.Orange100,
                StitchPalette.Orange700,
                Icons.Default.Functions,
            )
        "computer" in l || "code" in l || "cs" in l ->
            SubjectAccent(Color(0xFFDBEAFE), Color(0xFF2563EB), Icons.Default.Code)
        else ->
            SubjectAccent(Color(0xFFE9D5FF), Color(0xFF9333EA), Icons.Default.Translate)
    }
}

@Composable
fun ProfileTabScreenContent(
    state: ProfileTabUiState,
    onSettings: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    val u = state.user
    val pageBg = MaterialTheme.colorScheme.background
    val cardBg = MaterialTheme.colorScheme.surface
    val onCard = MaterialTheme.colorScheme.onSurface
    val outline = MaterialTheme.colorScheme.outline
    val monthlyGoalPct = u?.let { minOf(100, it.sessionsCompleted * 2) } ?: 0

    Surface(modifier = Modifier.fillMaxSize(), color = pageBg) {
        StitchMaxWidthColumn(
            modifier = Modifier.fillMaxSize(),
            maxContentWidth = 448.dp,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(cardBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(40.dp))
                Text(
                    "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = onCard,
                )
                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            HorizontalDivider(color = outline.copy(alpha = 0.5f))
            if (u == null) {
                Text("Not signed in", Modifier.padding(24.dp), color = MaterialTheme.colorScheme.onBackground)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                ) {
                    item {
                        Column(
                            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box {
                                Box(
                                    Modifier
                                        .size(128.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(4.dp, FocusPrimary.copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        u.name.take(1).uppercase(),
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = FocusPrimary,
                                    )
                                }
                                Surface(
                                    Modifier.align(Alignment.BottomEnd).size(32.dp),
                                    shape = CircleShape,
                                    color = FocusPrimary,
                                    border = BorderStroke(2.dp, cardBg),
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Verified, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(u.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = onCard)
                            Text(
                                "Focus Flow Pro Member",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = onEditProfile,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary),
                            ) {
                                Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Edit Profile", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    item {
                        Text(
                            "Study Stats",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            StatCard(
                                title = "Total Hours",
                                value = "${"%.0f".format(u.totalStudyHours)}h",
                                foot = "+12% this week",
                                icon = Icons.Default.Schedule,
                                modifier = Modifier.weight(1f),
                            )
                            StatCard(
                                title = "Streak",
                                value = "${u.streakDays} days",
                                foot = "New record!",
                                icon = Icons.Default.LocalFireDepartment,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    item {
                        Spacer(Modifier.height(12.dp))
                        SessionGoalCard(
                            sessions = u.sessionsCompleted,
                            monthlyPct = monthlyGoalPct,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "Subjects Studying",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                "Add New",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = FocusPrimary,
                                modifier = Modifier.clickable(onClick = onEditProfile),
                            )
                        }
                    }
                    items(u.subjects, key = { it.id }) { sub ->
                        val acc = subjectAccent(sub.label)
                        SubjectRow(
                            label = sub.label,
                            meta = "${"%.0f".format(u.totalStudyHours / maxOf(1, u.subjects.size))}h studied",
                            accent = acc,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                    item {
                        Spacer(Modifier.height(24.dp))
                        TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                            Text("Log out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    foot: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    val card = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = card,
        border = BorderStroke(1.dp, outline.copy(alpha = 0.6f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
                Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.TrendingUp, null, tint = StitchPalette.Emerald600, modifier = Modifier.size(14.dp))
                Text(foot, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = StitchPalette.Emerald600)
            }
        }
    }
}

@Composable
private fun SessionGoalCard(
    sessions: Int,
    monthlyPct: Int,
    modifier: Modifier = Modifier,
) {
    val card = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = card,
        border = BorderStroke(1.dp, outline.copy(alpha = 0.6f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.TaskAlt, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Focus Sessions", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("$sessions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { monthlyPct / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = FocusPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.KeyboardDoubleArrowUp, null, tint = StitchPalette.Emerald600, modifier = Modifier.size(14.dp))
                Text(
                    "$monthlyPct% of monthly goal reached",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SubjectRow(
    label: String,
    meta: String,
    accent: SubjectAccent,
    modifier: Modifier = Modifier,
) {
    val outline = MaterialTheme.colorScheme.outline
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, outline.copy(alpha = 0.6f)),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accent.container),
                contentAlignment = Alignment.Center,
            ) {
                Icon(accent.icon, null, tint = accent.onContainer)
            }
            Column(Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(meta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = StitchPalette.Slate400)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileTabPreview() {
    FocusFlowTheme {
        ProfileTabScreenContent(
            state =
                ProfileTabUiState(
                    user =
                        User(
                            id = "1",
                            name = "Alex Rivers",
                            email = "a@b.com",
                            photoUrl = null,
                            examPreparingFor = "GRE / GMAT",
                            bio = "Focused learner",
                            subjects =
                                listOf(
                                    SubjectTag("math", "Advanced Mathematics"),
                                    SubjectTag("cs", "Computer Science"),
                                ),
                            studyTimePreferences = setOf(StudyTimeOfDay.Morning),
                            dailyStudyGoalHours = 4.5f,
                            experienceLevel = StudyLevel.Intermediate,
                            timezone = "UTC",
                            sessionsCompleted = 42,
                            totalStudyHours = 128f,
                            streakDays = 12,
                        ),
                ),
            onSettings = {},
            onEditProfile = {},
            onLogout = {},
        )
    }
}
