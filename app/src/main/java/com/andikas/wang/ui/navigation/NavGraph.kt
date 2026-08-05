package com.andikas.wang.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andikas.wang.data.local.PreferenceManager
import com.andikas.wang.ui.onboarding.OnboardingScreen
import com.andikas.wang.ui.setup.SetupScreen
import com.andikas.wang.ui.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
fun NavGraph(
    startDestination: Screen = Screen.Splash
) {
    val navController = rememberNavController()
    val preferenceManager = koinInject<PreferenceManager>()

    val isOnboardingCompleted by preferenceManager.isOnboardingCompleted.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Splash> {
            SplashScreen(onTimeout = {
                navController.popBackStack()
                if (isOnboardingCompleted) {
                    navController.navigate(Screen.Main)
                } else {
                    navController.navigate(Screen.Onboarding)
                }
            })
        }
        composable<Screen.Onboarding> {
            OnboardingScreen(
                onFinished = {
                    navController.popBackStack()
                    navController.navigate(Screen.Setup)
                }
            )
        }
        composable<Screen.Setup> {
            SetupScreen(
                onFinished = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main)
                }
            )
        }
        composable<Screen.Main> {}
        composable<Screen.Analytics> {}
        composable<Screen.ManageBudget> {}
        composable<Screen.WalletSetup> {}
        composable<Screen.GoalSetup> {}
        composable<Screen.Notification> {}
        composable<Screen.Transaction> {}
        composable<Screen.TransactionDetail> {}
        composable<Screen.TransactionSetup> {}
        composable<Screen.Setting> {}
        composable<Screen.LanguageSelection> {}
        composable<Screen.CurrencySelection> {}
        composable<Screen.PinSetup> {}
        composable<Screen.FinSetup> {}
        composable<Screen.ManageData> {}
    }
}
