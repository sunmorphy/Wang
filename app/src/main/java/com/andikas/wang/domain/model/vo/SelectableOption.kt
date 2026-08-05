package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.ui.graphics.vector.ImageVector

import com.andikas.wang.ui.i18n.AppStrings

interface SelectableOption {
    val id: String
    val label: String
    val icon: ImageVector
    fun getLocalizedLabel(strings: AppStrings): String = label
}
