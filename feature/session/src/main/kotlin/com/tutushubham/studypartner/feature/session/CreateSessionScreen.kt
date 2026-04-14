package com.tutushubham.studypartner.feature.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.components.FocusFlowScreenHeader
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.model.StudyLevel

@Composable
fun CreateSessionRoute(onDone: () -> Unit, viewModel: CreateSessionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { if (it is CreateSessionEffect.Done) onDone() }
    }
    CreateSessionScreenContent(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateSessionScreenContent(
    state: CreateSessionUiState,
    onIntent: (CreateSessionIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FocusFlowScreenHeader(title = "Create session", subtitle = "Post a session to the marketplace")
        OutlinedTextField(
            value = state.subjectLabel,
            onValueChange = { onIntent(CreateSessionIntent.Subject(it)) },
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Date", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(0 to "Today", 1 to "Tomorrow", 2 to "+2d").forEach { (d, label) ->
                FilterChip(
                    selected = state.dayOffset == d,
                    onClick = { onIntent(CreateSessionIntent.DayOffset(d)) },
                    label = { Text(label) },
                )
            }
        }
        Text("Start time (local)", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(9 to 0, 12 to 0, 14 to 0, 18 to 0, 20 to 0).forEach { (h, m) ->
                val sel = state.startMinuteOfDay == h * 60 + m
                FilterChip(
                    selected = sel,
                    onClick = { onIntent(CreateSessionIntent.StartHour(h, m)) },
                    label = { Text(String.format("%02d:%02d", h, m)) },
                )
            }
        }
        OutlinedTextField(
            value = state.durationMinutes.toString(),
            onValueChange = { onIntent(CreateSessionIntent.Duration(it.toIntOrNull() ?: 60)) },
            label = { Text("Duration (min)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Level", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StudyLevel.entries.forEach { lvl ->
                FilterChip(
                    selected = state.level == lvl,
                    onClick = { onIntent(CreateSessionIntent.Level(lvl)) },
                    label = { Text(lvl.name) },
                )
            }
        }
        OutlinedTextField(
            value = state.note,
            onValueChange = { onIntent(CreateSessionIntent.Note(it)) },
            label = { Text("Optional note") },
            modifier = Modifier.fillMaxWidth(),
        )
        RowSwitch(
            label = "Public session",
            checked = state.isPublic,
            onCheckedChange = { onIntent(CreateSessionIntent.Public(it)) },
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onIntent(CreateSessionIntent.Submit) }, modifier = Modifier.fillMaxWidth()) {
            Text("Post session")
        }
    }
}

@Composable
private fun RowSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateSessionPreview() {
    FocusFlowTheme {
        CreateSessionScreenContent(state = CreateSessionUiState(), onIntent = {})
    }
}
