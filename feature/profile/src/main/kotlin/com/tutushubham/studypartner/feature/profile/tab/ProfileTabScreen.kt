package com.tutushubham.studypartner.feature.profile.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.components.FocusFlowScreenHeader
import com.tutushubham.studypartner.core.ui.components.FocusFlowSectionLabel
import com.tutushubham.studypartner.core.ui.components.FocusFlowStatRow
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
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

@Composable
fun ProfileTabScreenContent(
    state: ProfileTabUiState,
    onSettings: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    val u = state.user
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        FocusFlowScreenHeader(title = "Profile", subtitle = "Your study identity")
        Spacer(Modifier.height(12.dp))
        if (u == null) {
            Text("Not signed in")
        } else {
            Text(u.name, style = MaterialTheme.typography.titleMedium)
            if (u.bio.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(u.bio, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(16.dp))
            FocusFlowSectionLabel("Stats")
            FocusFlowStatRow("Total hours", "%.1f".format(u.totalStudyHours))
            Spacer(Modifier.height(8.dp))
            FocusFlowStatRow("Sessions completed", u.sessionsCompleted.toString())
            Spacer(Modifier.height(8.dp))
            FocusFlowStatRow("Streak", "${u.streakDays} days")
            Spacer(Modifier.height(16.dp))
            FocusFlowSectionLabel("Subjects")
            Text(u.subjects.joinToString { it.label }, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) { Text("Edit profile") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Log out") }
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
                            name = "Alex",
                            email = "a@b.com",
                            photoUrl = null,
                            bio = "Focused learner",
                            subjects = listOf(SubjectTag("cs", "CS")),
                            studyTimePreferences = setOf(StudyTimeOfDay.Morning),
                            dailyStudyGoalHours = 2f,
                            experienceLevel = StudyLevel.Intermediate,
                            timezone = "UTC",
                            sessionsCompleted = 12,
                            totalStudyHours = 24f,
                            streakDays = 3,
                        ),
                ),
            onSettings = {},
            onEditProfile = {},
            onLogout = {},
        )
    }
}
