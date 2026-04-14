package com.tutushubham.studypartner.feature.profile.setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay

@Composable
fun ProfileSetupRoute(onFinished: () -> Unit, viewModel: ProfileSetupViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            when (e) {
                ProfileSetupEffect.NavigateMain -> onFinished()
            }
        }
    }
    ProfileSetupScreenContent(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSetupScreenContent(
    state: ProfileSetupUiState,
    onIntent: (ProfileSetupIntent) -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(if (state.step == 0) "Basic info" else "Goals", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        when (state.step) {
            0 -> {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onIntent(ProfileSetupIntent.Name(it)) },
                    label = { Text("Display name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.subjectsCsv,
                    onValueChange = { onIntent(ProfileSetupIntent.SubjectsCsv(it)) },
                    label = { Text("Subjects (comma-separated)") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.photoUrl,
                    onValueChange = { onIntent(ProfileSetupIntent.PhotoUrl(it)) },
                    label = { Text("Photo URL (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            else -> {
                OutlinedTextField(
                    value = state.dailyGoalHours.toString(),
                    onValueChange = { onIntent(ProfileSetupIntent.DailyGoal(it.toFloatOrNull() ?: 2f)) },
                    label = { Text("Daily goal (hours)") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.bio,
                    onValueChange = { onIntent(ProfileSetupIntent.Bio(it)) },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Text("Experience level", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                    StudyLevel.entries.forEach { lvl ->
                        FilterChip(
                            selected = state.experienceLevel == lvl,
                            onClick = { onIntent(ProfileSetupIntent.Experience(lvl)) },
                            label = { Text(lvl.name) },
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("Preferred study times", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                    StudyTimeOfDay.values().forEach { t ->
                        FilterChip(
                            selected = t in state.studyTimes,
                            onClick = { onIntent(ProfileSetupIntent.ToggleStudyTime(t)) },
                            label = { Text(t.name) },
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        if (state.step == 0) {
            Button(onClick = { onIntent(ProfileSetupIntent.Next) }, modifier = Modifier.fillMaxWidth()) {
                Text("Next")
            }
        } else {
            Button(onClick = { onIntent(ProfileSetupIntent.Finish) }, modifier = Modifier.fillMaxWidth()) {
                Text("Save & continue")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { onIntent(ProfileSetupIntent.Back) }, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupPreview() {
    FocusFlowTheme {
        ProfileSetupScreenContent(state = ProfileSetupUiState(step = 0), onIntent = {})
    }
}
