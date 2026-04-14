package com.tutushubham.studypartner.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tutushubham.studypartner.feature.auth.AuthRoute
import com.tutushubham.studypartner.feature.chat.ChatRoute
import com.tutushubham.studypartner.feature.home.HomeRoute
import com.tutushubham.studypartner.feature.onboarding.OnboardingRoute
import com.tutushubham.studypartner.feature.partner.PartnerRoute
import com.tutushubham.studypartner.feature.profile.edit.EditProfileRoute
import com.tutushubham.studypartner.feature.profile.settings.SettingsRoute
import com.tutushubham.studypartner.feature.profile.setup.ProfileSetupRoute
import com.tutushubham.studypartner.feature.profile.tab.ProfileTabRoute
import com.tutushubham.studypartner.feature.session.CreateSessionRoute

object Routes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Auth = "auth"
    const val ProfileSetup = "profile_setup"
    const val Main = "main"
    const val CreateSession = "create_session"
    const val Chat = "chat/{matchId}"
    const val Settings = "settings"
    const val EditProfile = "edit_profile"

    fun chat(matchId: String) = "chat/$matchId"
}

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val vm: SplashViewModel = hiltViewModel()
            val dest by vm.destination.collectAsState()
            LaunchedEffect(dest) {
                when (dest) {
                    SplashDestination.Onboarding ->
                        navController.navigate(Routes.Onboarding) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    SplashDestination.Auth ->
                        navController.navigate(Routes.Auth) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    SplashDestination.ProfileSetup ->
                        navController.navigate(Routes.ProfileSetup) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    SplashDestination.Main ->
                        navController.navigate(Routes.Main) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                }
            }
        }
        composable(Routes.Onboarding) {
            OnboardingRoute(
                onFinishedToAuth = {
                    navController.navigate(Routes.Auth) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.Auth) {
            AuthRoute(
                onAuthedToProfileSetup = {
                    navController.navigate(Routes.ProfileSetup) {
                        popUpTo(Routes.Auth) { inclusive = true }
                    }
                },
                onAuthedToMain = {
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.Auth) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ProfileSetup) {
            ProfileSetupRoute(
                onFinished = {
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.ProfileSetup) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.Main) {
            MainTabsScaffold(
                rootNavController = navController,
            )
        }
        composable(Routes.CreateSession) {
            CreateSessionRoute(
                onDone = { navController.popBackStack() },
            )
        }
        composable(
            Routes.Chat,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType }),
        ) {
            ChatRoute(
                onBack = { navController.popBackStack() },
                onBackToHome = {
                    navController.popBackStack(Routes.Main, inclusive = false)
                },
                onFindPartner = {
                    navController.popBackStack(Routes.Main, inclusive = false)
                },
            )
        }
        composable(Routes.Settings) {
            SettingsRoute(onBack = { navController.popBackStack() })
        }
        composable(Routes.EditProfile) {
            EditProfileRoute(onDone = { navController.popBackStack() })
        }
    }
}

@Composable
private fun MainTabsScaffold(
    rootNavController: androidx.navigation.NavHostController,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Partner") },
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                )
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (tab) {
                0 ->
                    HomeRoute(
                        onCreateSession = { rootNavController.navigate(Routes.CreateSession) },
                        onJoinStudyRoom = { tab = 1 },
                        onOpenChat = { id -> rootNavController.navigate(Routes.chat(id)) },
                    )
                1 ->
                    PartnerRoute(
                        onJoinRequestSent = { tab = 0 },
                    )
                else ->
                    ProfileTabRoute(
                        onSettings = { rootNavController.navigate(Routes.Settings) },
                        onEditProfile = { rootNavController.navigate(Routes.EditProfile) },
                        onLoggedOut = {
                            rootNavController.navigate(Routes.Auth) {
                                popUpTo(Routes.Main) { inclusive = true }
                            }
                        },
                    )
            }
        }
    }
}
