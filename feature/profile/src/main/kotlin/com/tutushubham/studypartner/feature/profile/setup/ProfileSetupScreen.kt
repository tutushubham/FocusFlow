package com.tutushubham.studypartner.feature.profile.setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.tutushubham.studypartner.model.StudyTimeOfDay

@Composable
fun ProfileSetupRoute(
    onFinished: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            when (e) {
                ProfileSetupEffect.NavigateMain -> onFinished()
            }
        }
    }
    ProfileSetupScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileSetupScreenContent(
    state: ProfileSetupUiState,
    onIntent: (ProfileSetupIntent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    val outline = MaterialTheme.colorScheme.outline
    val canContinueBasic = state.name.isNotBlank() && state.examPreparingFor.isNotBlank()
    val canFinishGoals = state.selectedSubjects.isNotEmpty()
    val progress = if (state.step == 0) 0.5f else 1f
    val stepLabel = if (state.step == 0) "Step 1 of 2" else "Step 2 of 2"
    val percentLabel = if (state.step == 0) "50%" else "100%"

    Scaffold(
        containerColor = bg,
        bottomBar = {
            val gradientColors =
                listOf(
                    Color.Transparent,
                    bg.copy(alpha = 0.92f),
                    bg,
                )
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(gradientColors))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                if (state.step == 0) {
                    Button(
                        onClick = { onIntent(ProfileSetupIntent.Next) },
                        enabled = canContinueBasic,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary),
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.padding(start = 8.dp))
                    }
                    Text(
                        "You can update these details later in settings.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    )
                } else {
                    Button(
                        onClick = { onIntent(ProfileSetupIntent.Finish) },
                        enabled = canFinishGoals,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary),
                    ) {
                        Text("Complete Profile", fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.padding(start = 8.dp))
                    }
                    Text(
                        "You can change these settings later in your profile.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    )
                }
            }
        },
    ) { innerPadding ->
        StitchMaxWidthColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            maxContentWidth = if (state.step == 0) 512.dp else 448.dp,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        if (state.step > 0) {
                            onIntent(ProfileSetupIntent.Back)
                        } else {
                            onNavigateBack()
                        }
                    },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = onBg)
                }
                Text(
                    if (state.step == 0) "Profile Setup" else "Focus Flow",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f).padding(end = 48.dp),
                    textAlign = TextAlign.Center,
                    color = onBg,
                )
            }
            Column(Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        stepLabel,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                        ),
                        color = onBg,
                    )
                    Text(percentLabel, style = MaterialTheme.typography.labelLarge, color = FocusPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = FocusPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            val scroll = rememberScrollState()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 8.dp),
            ) {
                if (state.step == 0) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Welcome to Focus Flow",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = onBg,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Let's start by personalizing your study environment. We'll help you stay on track for your upcoming challenges.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(32.dp))
                    Text("Full Name", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = onBg)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onIntent(ProfileSetupIntent.Name(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("e.g. Alex Johnson") },
                        shape = RoundedCornerShape(12.dp),
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FocusPrimary,
                                unfocusedBorderColor = outline,
                            ),
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Exam preparing for",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = onBg,
                    )
                    Spacer(Modifier.height(8.dp))
                    var examMenuOpen by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = examMenuOpen,
                        onExpandedChange = { examMenuOpen = it },
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            readOnly = true,
                            value = state.examPreparingFor,
                            onValueChange = {},
                            placeholder = { Text("Select an exam") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = examMenuOpen) },
                            shape = RoundedCornerShape(12.dp),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FocusPrimary,
                                    unfocusedBorderColor = outline,
                                ),
                        )
                        DropdownMenu(expanded = examMenuOpen, onDismissRequest = { examMenuOpen = false }) {
                            StitchExamOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        onIntent(ProfileSetupIntent.Exam(opt))
                                        examMenuOpen = false
                                    },
                                )
                            }
                        }
                    }
                } else {
                    Column(Modifier.padding(top = 8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "Profile Setup",
                                style = MaterialTheme.typography.bodyLarge,
                                color = onBg,
                            )
                            Text("2 of 2", style = MaterialTheme.typography.bodyMedium, color = onBg)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Subjects and Goals",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            "What are you studying?",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = onBg,
                        )
                        Spacer(Modifier.height(16.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            StitchPresetSubjects.forEach { label ->
                                val selected = label in state.selectedSubjects
                                Surface(
                                    modifier = Modifier.clickable { onIntent(ProfileSetupIntent.ToggleSubject(label)) },
                                    shape = RoundedCornerShape(8.dp),
                                    color =
                                        if (selected) {
                                            FocusPrimary.copy(alpha = if (MaterialTheme.colorScheme.background == StitchPalette.BackgroundDark) 0.2f else 0.1f)
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        },
                                    border =
                                        if (selected) {
                                            BorderStroke(1.dp, FocusPrimary.copy(alpha = 0.3f))
                                        } else {
                                            null
                                        },
                                ) {
                                    Row(
                                        Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Icon(
                                            if (selected) Icons.Default.Check else Icons.Default.Add,
                                            null,
                                            tint = if (selected) FocusPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp),
                                        )
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                            color = if (selected) FocusPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(28.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "Daily study goal",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = onBg,
                            )
                            Text(
                                buildString {
                                    append(String.format("%.1f", state.dailyGoalHours))
                                    append(" ")
                                    append("hours")
                                },
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = FocusPrimary,
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Slider(
                            value = state.dailyGoalHours,
                            onValueChange = { onIntent(ProfileSetupIntent.DailyGoal(it)) },
                            valueRange = 1f..10f,
                            steps = 17,
                            colors =
                                SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = FocusPrimary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                                ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("1 hr", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("10 hrs", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(28.dp))
                        Text(
                            "Preferred study time",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = onBg,
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StudyTimeCard(
                                label = "Morning",
                                imageVector = Icons.Default.LightMode,
                                selected = StudyTimeOfDay.Morning in state.studyTimes,
                                onClick = { onIntent(ProfileSetupIntent.ToggleStudyTime(StudyTimeOfDay.Morning)) },
                                modifier = Modifier.weight(1f),
                            )
                            StudyTimeCard(
                                label = "Evening",
                                imageVector = Icons.Default.WbTwilight,
                                selected = StudyTimeOfDay.Evening in state.studyTimes,
                                onClick = { onIntent(ProfileSetupIntent.ToggleStudyTime(StudyTimeOfDay.Evening)) },
                                modifier = Modifier.weight(1f),
                            )
                            StudyTimeCard(
                                label = "Night",
                                imageVector = Icons.Default.DarkMode,
                                selected = StudyTimeOfDay.Night in state.studyTimes,
                                onClick = { onIntent(ProfileSetupIntent.ToggleStudyTime(StudyTimeOfDay.Night)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StudyTimeCard(
    label: String,
    imageVector: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) FocusPrimary else Color.Transparent
    val bg =
        if (selected) {
            FocusPrimary.copy(alpha = 0.08f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    Surface(
        modifier =
            modifier
                .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = bg,
    ) {
        Column(
            Modifier.padding(vertical = 12.dp, horizontal = 8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.padding(bottom = 4.dp)) {
                val tint = if (selected) FocusPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                Icon(imageVector = imageVector, contentDescription = null, tint = tint)
            }
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (selected) FocusPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupPreview() {
    FocusFlowTheme {
        ProfileSetupScreenContent(state = ProfileSetupUiState(step = 0), onIntent = {}, onNavigateBack = {})
    }
}
