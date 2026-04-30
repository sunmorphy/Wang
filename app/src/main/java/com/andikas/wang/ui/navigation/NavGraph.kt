package com.andikas.wang.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(
    startDestination: Screen = Screen.Splash
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Splash> {}
        composable<Screen.Onboarding> {}
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
