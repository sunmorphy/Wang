package com.andikas.wang.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Splash : Screen()
    @Serializable
    data object Onboarding : Screen()
    @Serializable
    data object BudgetSetup : Screen()
    @Serializable
    data object Main : Screen()
    @Serializable
    data object Analytics : Screen()
    @Serializable
    data object ManageBudget : Screen()
    @Serializable
    data object WalletSetup : Screen()
    @Serializable
    data object GoalSetup : Screen()
    @Serializable
    data object Notification : Screen()
    @Serializable
    data object Transaction : Screen()
    @Serializable
    data class TransactionDetail(val id: Long) : Screen()
    @Serializable
    data object TransactionSetup : Screen()
    @Serializable
    data object Setting : Screen()
    @Serializable
    data object LanguageSelection : Screen()
    @Serializable
    data object CurrencySelection : Screen()
    @Serializable
    data object PinSetup : Screen()
    @Serializable
    data object FinSetup : Screen()
    @Serializable
    data object ManageData : Screen()
}
