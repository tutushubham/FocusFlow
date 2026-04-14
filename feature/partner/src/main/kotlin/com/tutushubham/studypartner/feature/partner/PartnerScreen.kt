package com.tutushubham.studypartner.feature.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel

/* Stitch Remix HTML tokens: ae397c70928b475eb02bdafd1646ae3d/screen.html */
private val StitchSlate100 = Color(0xFFF1F5F9)
private val StitchSlate200 = Color(0xFFE2E8F0)
private val StitchSlate400 = Color(0xFF94A3B8)
private val StitchSlate500 = Color(0xFF64748B)
private val StitchSlate700 = Color(0xFF334155)
private val StitchSlate900 = Color(0xFF0F172A)
private val StitchOnlineGreen = Color(0xFF22C55E)

@Composable
fun PartnerRoute(
    onJoinRequestSent: () -> Unit,
    viewModel: PartnerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            when (e) {
                PartnerEffect.JoinRequestSent -> onJoinRequestSent()
            }
        }
    }
    PartnerScreenContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun PartnerScreenContent(
    state: PartnerUiState,
    onIntent: (PartnerIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { /* Stitch back — root tabs have no stack */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StitchSlate900,
                        )
                    }
                    Text(
                        "Focus Flow",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.2).sp),
                        color = StitchSlate900,
                    )
                    IconButton(onClick = { onIntent(PartnerIntent.ToggleSearchField) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = StitchSlate900)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StitchFilterPill(
                        text = "All Partners",
                        selected = state.filterAllPartners,
                        onClick = { onIntent(PartnerIntent.ToggleAllPartners) },
                    )
                    StitchFilterPill(
                        text = "Time",
                        selected = false,
                        onClick = { onIntent(PartnerIntent.TimeFilterStub) },
                        trailing = {
                            Icon(
                                Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = StitchSlate700,
                            )
                        },
                    )
                    StitchFilterPill(
                        text = "Subject",
                        selected = !state.filterAllPartners || state.subjectRowVisible,
                        onClick = { onIntent(PartnerIntent.ToggleSubjectRow) },
                        trailing = {
                            Icon(
                                Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint =
                                    if (!state.filterAllPartners || state.subjectRowVisible) {
                                        Color.White
                                    } else {
                                        StitchSlate700
                                    },
                            )
                        },
                    )
                    StitchFilterPill(
                        text = "Level",
                        selected = state.filterLevel != null || state.levelRowVisible,
                        onClick = { onIntent(PartnerIntent.ToggleLevelRow) },
                        trailing = {
                            Icon(
                                Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint =
                                    if (state.filterLevel != null || state.levelRowVisible) {
                                        Color.White
                                    } else {
                                        StitchSlate700
                                    },
                            )
                        },
                    )
                }
                if (state.searchFieldVisible) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.search,
                        onValueChange = { onIntent(PartnerIntent.Search(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search subject or name", color = StitchSlate500) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                    )
                }
                if (state.subjectRowVisible) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        state.subjectPresets.forEach { tag ->
                            StitchFilterPill(
                                text = tag.label,
                                selected = state.selectedSubjectId == tag.id,
                                onClick = { onIntent(PartnerIntent.PickSubject(tag.id)) },
                            )
                        }
                    }
                }
                if (state.levelRowVisible) {
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StudyLevel.entries.forEach { lvl ->
                            StitchFilterPill(
                                text = lvl.name,
                                selected = state.filterLevel == lvl,
                                onClick = {
                                    onIntent(
                                        PartnerIntent.SetLevel(if (state.filterLevel == lvl) null else lvl),
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(color = StitchSlate200, thickness = 1.dp)
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Recommended Partners",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = StitchSlate900,
                    )
                    Text(
                        "${state.requests.size} Online Now",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = FocusPrimary,
                    )
                }
            }
            items(state.requests, key = { it.id }) { req ->
                PartnerSessionCard(request = req, onStart = { onIntent(PartnerIntent.StartSession(req.id)) })
            }
        }
    }
}

@Composable
private fun StitchFilterPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    val bg = if (selected) MaterialTheme.colorScheme.primary else StitchSlate100
    val fg = if (selected) Color.White else StitchSlate700
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = bg,
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
                color = fg,
            )
            trailing?.invoke()
        }
    }
}

@Composable
private fun PartnerSessionCard(
    request: StudySessionRequest,
    onStart: () -> Unit,
) {
    val name = request.ownerDisplayName.ifBlank { "Study partner" }
    val initial = name.firstOrNull { !it.isWhitespace() }?.uppercaseChar() ?: '?'
    val subtitle =
        "${request.subject.label} • ${request.durationMinutes} min · ${request.level.name}"
    val bioLine =
        request.ownerBioPreview.ifBlank { request.description }.lines().firstOrNull().orEmpty()

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(1.dp, StitchSlate200, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box {
                        Box(
                            modifier =
                                Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, FocusPrimary.copy(alpha = 0.1f), CircleShape),
                        ) {
                            if (!request.ownerPhotoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = request.ownerPhotoUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .background(StitchSlate100),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        initial.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = FocusPrimary,
                                    )
                                }
                            }
                        }
                        Box(
                            modifier =
                                Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-2).dp, y = (-2).dp)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(StitchOnlineGreen)
                                    .border(2.dp, Color.White, CircleShape),
                        ) {}
                    }
                    Column {
                        Text(
                            name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = StitchSlate900,
                        )
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = StitchSlate500,
                        )
                        if (bioLine.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                bioLine,
                                style = MaterialTheme.typography.bodySmall,
                                color = StitchSlate500,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                IconButton(onClick = { /* Stitch overflow */ }) {
                    Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = StitchSlate400)
                }
            }
            HorizontalDivider(
                Modifier.padding(top = 12.dp),
                color = Color(0xFFF8FAFC),
                thickness = 1.dp,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Session request",
                    style = MaterialTheme.typography.labelSmall,
                    color = StitchSlate500,
                )
                Button(
                    onClick = onStart,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = FocusPrimary,
                            contentColor = Color.White,
                        ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Start Session", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartnerPreview() {
    FocusFlowTheme {
        PartnerScreenContent(
            state =
                PartnerUiState(
                    requests =
                        listOf(
                            StudySessionRequest(
                                id = "1",
                                ownerUserId = "o",
                                subject = SubjectTag("cs", "Computer Science"),
                                title = null,
                                description = "Algorithms block",
                                startEpochMillis = 0L,
                                durationMinutes = 120,
                                level = StudyLevel.Intermediate,
                                status = SessionRequestStatus.Posted,
                                isPublic = true,
                                ownerDisplayName = "Jordan",
                                ownerPhotoUrl = null,
                                ownerBioPreview = "Deep work on algorithms and interview prep.",
                            ),
                            StudySessionRequest(
                                id = "2",
                                ownerUserId = "o2",
                                subject = SubjectTag("math", "Mathematics"),
                                title = null,
                                description = "Calculus",
                                startEpochMillis = 0L,
                                durationMinutes = 90,
                                level = StudyLevel.Beginner,
                                status = SessionRequestStatus.Posted,
                                isPublic = true,
                                ownerDisplayName = "Sam",
                                ownerPhotoUrl = null,
                                ownerBioPreview = "Calculus study blocks with whiteboard notes.",
                            ),
                        ),
                ),
            onIntent = {},
        )
    }
}
