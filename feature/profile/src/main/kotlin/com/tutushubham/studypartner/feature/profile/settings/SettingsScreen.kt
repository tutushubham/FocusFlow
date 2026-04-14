package com.tutushubham.studypartner.feature.profile.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.layout.StitchMaxWidthColumn
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette

@Composable
fun SettingsRoute(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    SettingsScreenContent(
        state = state,
        onNotifications = viewModel::setNotifications,
        onCloudSync = viewModel::setCloudSync,
        onBack = onBack,
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsUiState,
    onNotifications: (Boolean) -> Unit,
    onCloudSync: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    val card = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxSize().background(bg)) {
        StitchMaxWidthColumn(
            modifier = Modifier.fillMaxSize(),
            maxContentWidth = 448.dp,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(bg)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = onBg)
                }
                Text(
                    "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = onBg,
                )
                Spacer(Modifier.size(48.dp))
            }
            HorizontalDivider(color = outline.copy(alpha = 0.5f))
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .padding(bottom = 32.dp),
            ) {
                Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = FocusPrimary.copy(alpha = 0.1f),
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoGraph, null, tint = FocusPrimary, modifier = Modifier.size(40.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Focus Flow", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onBg)
                    Text("v2.4.0 Professional", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SectionHeader("General")
                SettingsCardSurface(card, outline) {
                    ToggleSettingsRow(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Alerts and reminders",
                        checked = state.notificationsEnabled,
                        onCheckedChange = onNotifications,
                        showDividerBelow = true,
                    )
                    NavSettingsRow(
                        icon = Icons.Default.Timer,
                        title = "Focus Mode Settings",
                        subtitle = "Durations, breaks, and auto-start",
                        showDividerBelow = true,
                        onClick = { },
                    )
                    NavSettingsRow(
                        icon = Icons.Default.VolumeUp,
                        title = "Alert Sounds",
                        subtitle = "Chimes and white noise",
                        showDividerBelow = false,
                        onClick = { },
                    )
                }
                SectionHeader("Account")
                SettingsCardSurface(card, outline) {
                    NavSettingsRow(
                        icon = Icons.Default.Person,
                        title = "Profile Details",
                        subtitle = "Manage your personal info",
                        showDividerBelow = true,
                        onClick = { },
                    )
                    ToggleSettingsRow(
                        icon = Icons.Default.Sync,
                        title = "Cloud Sync",
                        subtitle = "Sync across devices",
                        checked = state.cloudSyncEnabled,
                        onCheckedChange = onCloudSync,
                        showDividerBelow = false,
                    )
                }
                SectionHeader("Support")
                SettingsCardSurface(card, outline) {
                    NavSettingsRowCustomTrailing(
                        icon = Icons.Default.Description,
                        title = "Privacy Policy",
                        showDividerBelow = true,
                        onClick = { },
                        trailing = { Icon(Icons.Default.OpenInNew, null, tint = StitchPalette.Slate400) },
                    )
                    NavSettingsRowCustomTrailing(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Help Center",
                        showDividerBelow = false,
                        onClick = { },
                        trailing = { Icon(Icons.Default.ChevronRight, null, tint = StitchPalette.Slate400) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
    )
}

@Composable
private fun SettingsCardSurface(
    cardColor: Color,
    outlineColor: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = cardColor,
        border = BorderStroke(1.dp, outlineColor.copy(alpha = 0.65f)),
    ) {
        Column(Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
private fun ToggleSettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showDividerBelow: Boolean,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            IconBadge(icon)
            Column(Modifier.padding(start = 12.dp)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = FocusPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
        )
    }
    if (showDividerBelow) {
        HorizontalDivider(
            modifier = Modifier.padding(start = 68.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        )
    }
}

@Composable
private fun NavSettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showDividerBelow: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            IconBadge(icon)
            Column(Modifier.padding(start = 12.dp)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = StitchPalette.Slate400)
    }
    if (showDividerBelow) {
        HorizontalDivider(
            modifier = Modifier.padding(start = 68.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        )
    }
}

@Composable
private fun NavSettingsRowCustomTrailing(
    icon: ImageVector,
    title: String,
    showDividerBelow: Boolean,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            IconBadge(icon)
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
        trailing()
    }
    if (showDividerBelow) {
        HorizontalDivider(
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        )
    }
}

@Composable
private fun IconBadge(icon: ImageVector) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = RoundedCornerShape(8.dp),
        color = FocusPrimary.copy(alpha = 0.1f),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = FocusPrimary, modifier = Modifier.size(22.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    FocusFlowTheme {
        SettingsScreenContent(state = SettingsUiState(), onNotifications = {}, onCloudSync = {}, onBack = {})
    }
}
