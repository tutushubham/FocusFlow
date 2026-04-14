package com.tutushubham.studypartner.feature.profile.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.layout.StitchMaxWidthColumn
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette
import com.tutushubham.studypartner.feature.profile.setup.StitchExamOptions
import com.tutushubham.studypartner.feature.profile.setup.StitchPresetSubjects
import com.tutushubham.studypartner.model.StudyTimeOfDay

@Composable
fun EditProfileRoute(
    onDone: () -> Unit,
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            if (e is EditProfileEffect.Done) onDone()
        }
    }
    EditProfileScreenContent(state = state, onIntent = viewModel::onIntent, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreenContent(
    state: EditProfileUiState,
    onIntent: (EditProfileIntent) -> Unit,
    onBack: () -> Unit,
) {
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    val outline = MaterialTheme.colorScheme.outline
    val scroll = rememberScrollState()
    Scaffold(
        containerColor = bg,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = onBg,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
        bottomBar = {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = { onIntent(EditProfileIntent.Save) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary),
                ) {
                    Text("Save changes", fontWeight = FontWeight.Bold)
                }
            }
        },
    ) { innerPadding ->
        StitchMaxWidthColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            maxContentWidth = 512.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
            ) {
                Text("Basic info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(12.dp))
                Text("Full Name", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onIntent(EditProfileIntent.Name(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FocusPrimary,
                            unfocusedBorderColor = outline,
                        ),
                )
                Spacer(Modifier.height(16.dp))
                Text("Exam preparing for", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(6.dp))
                var examMenuOpen by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = examMenuOpen, onExpandedChange = { examMenuOpen = it }) {
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
                                    onIntent(EditProfileIntent.Exam(opt))
                                    examMenuOpen = false
                                },
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text("Study goals", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Subjects",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = onBg,
                )
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StitchPresetSubjects.forEach { label ->
                        val selected = label in state.selectedSubjects
                        Surface(
                            modifier = Modifier.clickable { onIntent(EditProfileIntent.ToggleSubject(label)) },
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
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily study goal", fontWeight = FontWeight.Bold, color = onBg)
                    Text(
                        "${"%.1f".format(state.dailyGoalHours)} hours",
                        fontWeight = FontWeight.Bold,
                        color = FocusPrimary,
                    )
                }
                Slider(
                    value = state.dailyGoalHours,
                    onValueChange = { onIntent(EditProfileIntent.DailyGoal(it)) },
                    valueRange = 1f..10f,
                    steps = 17,
                    colors =
                        SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = FocusPrimary,
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                )
                Spacer(Modifier.height(8.dp))
                Text("Preferred study time", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditStudyTimeChip("Morning", Icons.Default.LightMode, StudyTimeOfDay.Morning, state, onIntent, Modifier.weight(1f))
                    EditStudyTimeChip("Evening", Icons.Default.WbTwilight, StudyTimeOfDay.Evening, state, onIntent, Modifier.weight(1f))
                    EditStudyTimeChip("Night", Icons.Default.DarkMode, StudyTimeOfDay.Night, state, onIntent, Modifier.weight(1f))
                }
                Spacer(Modifier.height(20.dp))
                Text("Bio", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = onBg)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.bio,
                    onValueChange = { onIntent(EditProfileIntent.Bio(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FocusPrimary,
                            unfocusedBorderColor = outline,
                        ),
                )
            }
        }
    }
}

@Composable
private fun EditStudyTimeChip(
    label: String,
    icon: ImageVector,
    time: StudyTimeOfDay,
    state: EditProfileUiState,
    onIntent: (EditProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = time in state.studyTimes
    val borderColor = if (selected) FocusPrimary else Color.Transparent
    val bg =
        if (selected) FocusPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
    Surface(
        modifier =
            modifier
                .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                .clickable { onIntent(EditProfileIntent.ToggleStudyTime(time)) },
        shape = RoundedCornerShape(12.dp),
        color = bg,
    ) {
        Column(
            Modifier.padding(vertical = 10.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) FocusPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
private fun EditProfilePreview() {
    FocusFlowTheme {
        EditProfileScreenContent(
            state = EditProfileUiState(name = "Alex", examPreparingFor = "GRE / GMAT", selectedSubjects = setOf("Mathematics")),
            onIntent = {},
            onBack = {},
        )
    }
}
