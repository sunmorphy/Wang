package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.andikas.wang.ui.i18n.AppStrings

enum class WalletType(
    override val id: String,
    override val label: String,
    override val icon: ImageVector
) : SelectableOption {
    BANK("1", "Bank", Icons.Rounded.AccountBalance) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.walletTypeBank
    },
    EWALLET("2", "E-Wallet", Icons.Rounded.Wallet) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.walletTypeEWallet
    },
    CASH("3", "Cash", Icons.Rounded.Money) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.walletTypeCash
    }
}