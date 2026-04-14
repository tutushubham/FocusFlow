package com.tutushubham.studypartner.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Target
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tutushubham.studypartner.core.ui.theme.FocusFlowTheme
import com.tutushubham.studypartner.core.ui.theme.FocusPrimary
import com.tutushubham.studypartner.core.ui.theme.StitchPalette

@Composable
fun AuthRoute(
    onAuthedToProfileSetup: () -> Unit,
    onAuthedToMain: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { e ->
            when (e) {
                AuthEffect.NavigateProfileSetup -> onAuthedToProfileSetup()
                AuthEffect.NavigateMain -> onAuthedToMain()
            }
        }
    }
    AuthScreenContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun AuthScreenContent(
    state: AuthUiState,
    onIntent: (AuthIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(StitchPalette.BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(12.dp),
                color = FocusPrimary,
                shadowElevation = 6.dp,
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Target, null, tint = Color(0xFFF1F5F9), modifier = Modifier.size(36.dp))
                }
            }
            Text("Focus Flow", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp))
            Text(
                "Find your productivity rhythm",
                style = MaterialTheme.typography.bodyLarge,
                color = StitchPalette.Slate500,
            )
        }
        Spacer(Modifier.height(32.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
            shape = RoundedCornerShape(12.dp),
            color = FocusPrimary.copy(alpha = 0.05f),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(FocusPrimary.copy(alpha = 0.2f), FocusPrimary.copy(alpha = 0.05f), Color.Transparent),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.AutoGraph, null, tint = FocusPrimary.copy(alpha = 0.3f), modifier = Modifier.size(88.dp))
            }
        }
        Spacer(Modifier.height(32.dp))
        OutlinedButton(
            onClick = { onIntent(AuthIntent.GoogleStub) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, StitchPalette.Slate200),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = StitchPalette.Slate700),
        ) {
            Text("Continue with Google", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onIntent(AuthIntent.ShowEmailForm) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary, contentColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        ) {
            Icon(Icons.Default.Mail, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.size(8.dp))
            Text("Continue with Email", fontWeight = FontWeight.SemiBold)
        }
        if (state.emailFormVisible) {
            Spacer(Modifier.height(24.dp))
            Text(
                if (state.isRegister) "Create account" else "Sign in with email",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = { onIntent(AuthIntent.Email(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(AuthIntent.Password(it)) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onIntent(AuthIntent.Submit) },
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary),
            ) {
                Text(if (state.isRegister) "Register" else "Login", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = { onIntent(AuthIntent.ToggleRegister) }, modifier = Modifier.fillMaxWidth()) {
                Text(if (state.isRegister) "Have an account? Sign in" else "Need an account? Register")
            }
        }
        Spacer(Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(Modifier.weight(1f), color = StitchPalette.Slate200)
            Text(
                "New here? Join the flow",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = StitchPalette.Slate400,
            )
            HorizontalDivider(Modifier.weight(1f), color = StitchPalette.Slate200)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "By continuing, you agree to our Terms of Service and Privacy Policy.",
            style = MaterialTheme.typography.bodySmall,
            color = StitchPalette.Slate400,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPreview() {
    FocusFlowTheme {
        AuthScreenContent(state = AuthUiState(), onIntent = {})
    }
}
