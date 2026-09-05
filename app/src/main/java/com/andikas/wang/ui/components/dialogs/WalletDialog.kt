package com.andikas.wang.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.domain.model.vo.WalletType
import com.andikas.wang.ui.components.WCurrencyInput
import com.andikas.wang.ui.components.WGridSelection
import com.andikas.wang.ui.components.WPrimaryButton
import com.andikas.wang.ui.components.WTextField

@Composable
fun WalletDialog(
    currentCurrency: CurrencyType,
    onDismiss: () -> Unit,
    onSave: (name: String, type: WalletType, balance: Double) -> Unit
) {
    val strings = LocalStrings.current
    var balanceText by remember { mutableStateOf("") }
    var walletName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(WalletType.BANK) }
    val selectedCurrency by remember { mutableStateOf(currentCurrency) }

    WMainDialog(
        onDismiss = onDismiss,
        icon = Icons.Rounded.AccountBalanceWallet,
        title = strings.manageWalletTitle,
        description = strings.manageWalletDesc,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WCurrencyInput(
                label = null,
                value = balanceText,
                onValueChange = { balanceText = it },
                currencyType = selectedCurrency
            )

            WTextField(
                label = strings.walletNameLabel,
                value = walletName,
                onValueChange = { walletName = it },
                placeholder = strings.walletNamePlaceholder
            )

            WGridSelection(
                label = strings.walletTypeLabel,
                items = WalletType.entries,
                onItemSelected = { item ->
                    if (item is WalletType) {
                        selectedType = item
                    }
                }
            )

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
                        val balance = balanceText.toDoubleOrNull() ?: 0.0
                        onSave(walletName, selectedType, balance)
                    }
                )
            }
        }
    }
}