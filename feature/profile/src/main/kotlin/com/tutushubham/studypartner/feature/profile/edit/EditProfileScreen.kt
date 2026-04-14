package com.tutushubham.studypartner.feature.profile.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme

@Composable
fun EditProfileRoute(onDone: () -> Unit, viewModel: EditProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            if (e is EditProfileEffect.Done) onDone()
        }
    }
    EditProfileScreenContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun EditProfileScreenContent(
    state: EditProfileUiState,
    onIntent: (EditProfileIntent) -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Edit profile", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = { onIntent(EditProfileIntent.Name(it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.subjectsCsv,
            onValueChange = { onIntent(EditProfileIntent.SubjectsCsv(it)) },
            label = { Text("Subjects (comma-separated)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.bio,
            onValueChange = { onIntent(EditProfileIntent.Bio(it)) },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onIntent(EditProfileIntent.Save) }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfilePreview() {
    FocusFlowTheme {
        EditProfileScreenContent(state = EditProfileUiState(name = "Alex", subjectsCsv = "CS", bio = "Hi"), onIntent = {})
    }
}
