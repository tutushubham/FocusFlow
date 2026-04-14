package com.tutushubham.studypartner.feature.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette
import com.tutushubham.studypartner.domain.model.Message
import com.tutushubham.studypartner.model.MatchStatus

@Composable
fun ChatRoute(
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onFindPartner: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ChatScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack,
        onBackToHome = onBackToHome,
        onFindPartner = onFindPartner,
    )
}

@Composable
fun ChatScreenContent(
    state: ChatUiState,
    onIntent: (ChatIntent) -> Unit,
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onFindPartner: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = StitchPalette.Slate600)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Study Partner Chat",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = StitchPalette.Slate900,
                )
                Text(
                    "FOCUS FLOW",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Medium),
                    color = StitchPalette.Slate400,
                )
            }
            IconButton(onClick = { /* Stitch overflow */ }) {
                Icon(Icons.Default.MoreVert, null, tint = StitchPalette.Slate600)
            }
        }
        HorizontalDivider(color = StitchPalette.Slate200)
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(StitchPalette.BackgroundLight)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(shape = RoundedCornerShape(999.dp), color = StitchPalette.Slate100) {
                        Text(
                            "TODAY",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = StitchPalette.Slate500,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
            items(state.messages, key = { it.id }) { m ->
                val mine = state.currentUserId != null && m.senderUserId == state.currentUserId
                val label = if (mine) "You" else state.partnerName
                if (mine) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.widthIn(max = 320.dp)) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = StitchPalette.Slate400, modifier = Modifier.padding(end = 4.dp))
                            Surface(
                                color = FocusPrimary,
                                shape =
                                    RoundedCornerShape(
                                        topStart = 12.dp,
                                        topEnd = 12.dp,
                                        bottomStart = 12.dp,
                                        bottomEnd = 4.dp,
                                    ),
                                shadowElevation = 1.dp,
                            ) {
                                Text(
                                    m.text,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                )
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = FocusPrimary.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, FocusPrimary.copy(alpha = 0.1f)),
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Y", color = FocusPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = StitchPalette.Slate200,
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    state.partnerName.firstOrNull { !it.isWhitespace() }?.uppercaseChar()?.toString() ?: "?",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = StitchPalette.Slate500,
                                )
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                        Column(modifier = Modifier.widthIn(max = 320.dp)) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = StitchPalette.Slate400, modifier = Modifier.padding(start = 4.dp))
                            Surface(
                                color = StitchPalette.Slate100,
                                shape =
                                    RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 12.dp,
                                        bottomStart = 12.dp,
                                        bottomEnd = 12.dp,
                                    ),
                                shadowElevation = 1.dp,
                            ) {
                                Text(
                                    m.text,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = StitchPalette.Slate800,
                                )
                            }
                        }
                    }
                }
            }
        }
        state.preStartMessage?.let { msg ->
            Text(
                msg,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = StitchPalette.Slate600,
            )
        }
        if (state.sessionEnded) {
            Column(Modifier.padding(16.dp)) {
                Text("Session ended", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onBackToHome, colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary)) {
                        Text("Back to Home")
                    }
                    OutlinedButton(onClick = onFindPartner) { Text("Find new partner") }
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = StitchPalette.Slate100.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, StitchPalette.Slate200),
                ) {
                    Row(
                        Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { /* add attachment stub */ }) {
                            Icon(Icons.Default.Add, null, tint = StitchPalette.Slate400)
                        }
                        TextField(
                            value = state.inputText,
                            onValueChange = { onIntent(ChatIntent.Input(it)) },
                            modifier = Modifier.weight(1f),
                            enabled = state.canSend,
                            placeholder = { Text("Type a message...", color = StitchPalette.Slate400) },
                            colors =
                                TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                            singleLine = true,
                        )
                        IconButton(
                            onClick = { onIntent(ChatIntent.Send) },
                            enabled = state.canSend && state.inputText.isNotBlank(),
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (state.canSend && state.inputText.isNotBlank()) FocusPrimary else StitchPalette.Slate300,
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier.padding(10.dp).size(20.dp),
                                )
                            }
                        }
                    }
                }
                Text(
                    if (state.canSend) " " else "Chat disabled during active focus sessions",
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = StitchPalette.Slate400,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatPreview() {
    FocusFlowTheme {
        ChatScreenContent(
            state =
                ChatUiState(
                    subject = "Computer Science",
                    partnerName = "Alex Chen",
                    messages =
                        listOf(
                            Message("1", "m", "u1", "Hi", System.currentTimeMillis()),
                            Message("2", "m", "me", "Hey back", System.currentTimeMillis()),
                        ),
                    currentUserId = "me",
                    canSend = true,
                    sessionEnded = false,
                ),
            onIntent = {},
            onBack = {},
            onBackToHome = {},
            onFindPartner = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatEndedPreview() {
    FocusFlowTheme {
        ChatScreenContent(
            state =
                ChatUiState(
                    subject = "Math",
                    sessionEnded = true,
                    matchStatus = MatchStatus.Completed,
                ),
            onIntent = {},
            onBack = {},
            onBackToHome = {},
            onFindPartner = {},
        )
    }
}
