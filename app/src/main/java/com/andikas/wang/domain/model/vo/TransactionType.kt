package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMade
import androidx.compose.material.icons.automirrored.rounded.CallReceived
import androidx.compose.ui.graphics.vector.ImageVector
import com.andikas.wang.ui.i18n.AppStrings

enum class TransactionType(
    override val id: String,
    override val label: String,
    override val icon: ImageVector
) : SelectableOption {
    INCOME("1", "Income", Icons.AutoMirrored.Rounded.CallReceived) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.transactionTypeIncome
    },
    EXPENSE("2", "Expense", Icons.AutoMirrored.Rounded.CallMade) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.transactionTypeExpense
    }
}