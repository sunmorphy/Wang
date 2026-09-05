package com.andikas.wang.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.CategoryOption
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.ui.components.WCurrencyInput
import com.andikas.wang.ui.components.WDropdownField
import com.andikas.wang.ui.components.WPrimaryButton
import com.andikas.wang.ui.components.WTextField

@Composable
fun BudgetDialog(
    currentCurrency: CurrencyType,
    categories: List<CategoryOption> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (amount: Double, categoryId: String?, newCategoryName: String?) -> Unit
) {
    val strings = LocalStrings.current
    var budgetText by remember { mutableStateOf("") }
    val selectedCurrency by remember { mutableStateOf(currentCurrency) }
    var selectedCategory by remember { mutableStateOf<CategoryOption?>(null) }
    var customCategoryName by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf<String?>(null) }

    val isOtherSelected = selectedCategory?.id == "0" || selectedCategory?.label?.trim()?.lowercase() == "other"

    WMainDialog(
        onDismiss = onDismiss,
        icon = Icons.Rounded.TrackChanges,
        title = strings.manageBudgetTitle,
        description = strings.manageBudgetDesc
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WCurrencyInput(
                label = null,
                value = budgetText,
                onValueChange = { budgetText = it },
                currencyType = selectedCurrency
            )

            WDropdownField(
                label = strings.categoryLabel,
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = {
                    selectedCategory = it
                    categoryError = null
                },
                placeholder = strings.chooseCategoryPlaceholder
            )

            if (isOtherSelected) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    WTextField(
                        label = strings.newCategoryLabel,
                        value = customCategoryName,
                        onValueChange = {
                            customCategoryName = it
                            categoryError = null
                        },
                        placeholder = strings.newCategoryPlaceholder
                    )
                    if (categoryError != null) {
                        Text(
                            text = categoryError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WPrimaryButton(
                    text = strings.save,
                    onClick = {
                        val amount = budgetText.toDoubleOrNull() ?: 0.0
                        if (isOtherSelected) {
                            val trimmed = customCategoryName.trim()
                            if (trimmed.isEmpty()) {
                                categoryError = strings.categoryNameEmptyError
                                return@WPrimaryButton
                            }
                            val alreadyExists = categories.any { option ->
                                option.label.trim().equals(trimmed, ignoreCase = true) ||
                                option.getLocalizedLabel(strings).trim().equals(trimmed, ignoreCase = true)
                            }
                            if (alreadyExists) {
                                categoryError = strings.categoryAlreadyExistsError
                                return@WPrimaryButton
                            }
                        }
                        val newCat = if (isOtherSelected) customCategoryName.trim() else null
                        onSave(amount, selectedCategory?.id, newCat)
                    }
                )
            }
        }
    }
}