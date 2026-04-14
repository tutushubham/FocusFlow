package com.tutushubham.studypartner.feature.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.components.FocusFlowScreenHeader
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme

@Composable
fun SettingsRoute(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    SettingsScreenContent(
        state = state,
        onNotifications = viewModel::setNotifications,
        onBack = onBack,
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsUiState,
    onNotifications: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        FocusFlowScreenHeader(title = "Settings", subtitle = "Notifications, focus mode, and account")
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Notifications")
            Switch(checked = state.notificationsEnabled, onCheckedChange = onNotifications)
        }
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        Text("Focus Mode", style = MaterialTheme.typography.titleMedium)
        Text("Coming soon — deep work timers and app blocking.", style = MaterialTheme.typography.bodyMedium)
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        Text("Account", style = MaterialTheme.typography.titleMedium)
        Text("Manage email and password (mock).", style = MaterialTheme.typography.bodyMedium)
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        Text("Privacy", style = MaterialTheme.typography.titleMedium)
        Text("Placeholder privacy summary.", style = MaterialTheme.typography.bodyMedium)
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        Text("Help", style = MaterialTheme.typography.titleMedium)
        Text("FAQ and support (placeholder).", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        androidx.compose.material3.TextButton(onClick = onBack) { Text("Back") }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    FocusFlowTheme {
        SettingsScreenContent(state = SettingsUiState(), onNotifications = {}, onBack = {})
    }
}
