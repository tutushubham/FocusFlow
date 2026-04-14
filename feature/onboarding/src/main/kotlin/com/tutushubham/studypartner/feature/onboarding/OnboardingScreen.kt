package com.tutushubham.studypartner.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.launch

@Composable
fun OnboardingRoute(
    onFinishedToAuth: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateAuth -> onFinishedToAuth()
            }
        }
    }
    OnboardingScreenContent(onIntent = viewModel::onIntent)
}

@Composable
fun OnboardingScreenContent(
    onIntent: (OnboardingIntent) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(StitchPalette.BackgroundLight),
    ) {
        when (pagerState.currentPage) {
            0 ->
                OnboardingTopBar(
                    leading = {
                        Spacer(Modifier.width(40.dp))
                    },
                    title = "Focus Flow",
                    trailing = {
                        TextButton(onClick = { onIntent(OnboardingIntent.Skip) }) {
                            Text("Skip", color = FocusPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    },
                )
            1 ->
                OnboardingTopBar(
                    leading = {
                        IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(0) } }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = StitchPalette.Slate900)
                        }
                    },
                    title = "Focus Flow",
                    trailing = { Spacer(Modifier.width(40.dp)) },
                )
            else ->
                OnboardingTopBar(
                    leading = {
                        IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(1) } }) {
                            Icon(Icons.Default.ChevronLeft, null, tint = StitchPalette.Slate900)
                        }
                    },
                    title = "Focus Flow",
                    trailing = { Spacer(Modifier.width(40.dp)) },
                )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> OnboardingPageFindPartner()
                1 -> OnboardingPageAccountable()
                else -> OnboardingPageReadyToFocus()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(3) { i ->
                Box(
                    modifier =
                        Modifier
                            .padding(horizontal = 6.dp)
                            .height(10.dp)
                            .width(if (i == pagerState.currentPage) 24.dp else 10.dp)
                            .clip(CircleShape)
                            .background(
                                if (i == pagerState.currentPage) FocusPrimary else StitchPalette.Slate300,
                            ),
                )
            }
        }
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
            if (pagerState.currentPage < 2) {
                Button(
                    onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                ) {
                    Text("Next", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            } else {
                Button(
                    onClick = { onIntent(OnboardingIntent.Complete) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FocusPrimary, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                ) {
                    Text("Get Started", fontWeight = FontWeight.SemiBold)
                }
            }
            if (pagerState.currentPage == 1) {
                TextButton(onClick = { onIntent(OnboardingIntent.Skip) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Skip", color = StitchPalette.Slate400)
                }
            }
        }
    }
}

@Composable
private fun OnboardingTopBar(
    leading: @Composable () -> Unit,
    title: String,
    trailing: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading()
        Text(
            title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = StitchPalette.Slate900,
        )
        trailing()
    }
}

@Composable
private fun OnboardingPageFindPartner() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(FocusPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier =
                    Modifier
                        .fillMaxSize(fraction = 0.8f)
                        .clip(RoundedCornerShape(12.dp)),
                shadowElevation = 8.dp,
                border = BorderStroke(4.dp, Color.White),
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(FocusPrimary.copy(alpha = 0.35f), StitchPalette.Slate100),
                            ),
                        ),
                )
            }
            Surface(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp,
                color = Color.White,
            ) {
                Icon(Icons.Default.Groups, null, tint = FocusPrimary, modifier = Modifier.padding(12.dp))
            }
            Surface(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp,
                color = Color.White,
            ) {
                Icon(Icons.Default.Schedule, null, tint = FocusPrimary, modifier = Modifier.padding(12.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Find Your Perfect Study Partner",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp),
            textAlign = TextAlign.Center,
            color = StitchPalette.Slate900,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Connect with students worldwide who share your goals and study schedules.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = StitchPalette.Slate600,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun OnboardingPageAccountable() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { i ->
                Box(
                    Modifier
                        .height(8.dp)
                        .width(if (i == 2) 32.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (i == 2) FocusPrimary else FocusPrimary.copy(alpha = 0.2f)),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Surface(
            modifier = Modifier.size(280.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, StitchPalette.Slate200),
        ) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Row(
                    Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    val barHeights = listOf(48.dp, 64.dp, 36.dp, 72.dp, 96.dp)
                    val alphas = listOf(0.1f, 0.2f, 0.1f, 0.4f, 1f)
                    barHeights.forEachIndexed { idx, h ->
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(h)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(FocusPrimary.copy(alpha = alphas[idx])),
                        )
                    }
                }
                Surface(shape = RoundedCornerShape(8.dp), color = FocusPrimary.copy(alpha = 0.05f)) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, null, tint = FocusPrimary, modifier = Modifier.size(36.dp))
                        Column {
                            Text("CURRENT STREAK", style = MaterialTheme.typography.labelSmall, color = StitchPalette.Slate500, fontWeight = FontWeight.Bold)
                            Text("12 Days", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Stay Accountable",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = StitchPalette.Slate900,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Track your study hours, maintain daily streaks, and watch your progress grow.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = StitchPalette.Slate600,
        )
    }
}

@Composable
private fun OnboardingPageReadyToFocus() {
    Column(Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(FocusPrimary.copy(alpha = 0.05f)),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, StitchPalette.BackgroundLight.copy(alpha = 0.85f)),
                        ),
                    ),
            )
        }
        Column(
            Modifier.padding(horizontal = 24.dp).padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Ready to Focus?",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 32.sp),
                textAlign = TextAlign.Center,
                color = StitchPalette.Slate900,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Your journey towards better study habits starts now. Join thousands of students improving their productivity.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = StitchPalette.Slate600,
            )
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { i ->
                    Box(
                        Modifier
                            .height(6.dp)
                            .width(if (i == 3) 32.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (i == 3) FocusPrimary else FocusPrimary.copy(alpha = 0.2f)),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    FocusFlowTheme {
        OnboardingScreenContent(onIntent = {})
    }
}
