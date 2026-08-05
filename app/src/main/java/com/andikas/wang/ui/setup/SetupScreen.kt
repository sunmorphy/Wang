package com.andikas.wang.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.ui.components.LocalToastHostState
import com.andikas.wang.ui.components.WTonalButton
import com.andikas.wang.ui.components.WangHeader
import com.andikas.wang.ui.components.dialogs.BudgetDialog
import com.andikas.wang.ui.components.dialogs.CurrencyDialog
import com.andikas.wang.ui.components.dialogs.WalletDialog
import com.andikas.wang.ui.theme.WangTheme
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    onFinished: () -> Unit,
    viewModel: SetupViewModel = koinInject()
) {
    val strings = LocalStrings.current
    val isCurrencySetupDone by viewModel.isCurrencySetupDone.collectAsState()
    val isWalletSetupDone by viewModel.isWalletSetupDone.collectAsState()
    val isBudgetSetupDone by viewModel.isBudgetSetupDone.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showWalletDialog by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }

    val toastState = LocalToastHostState.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isCurrencySetupDone, isWalletSetupDone, isBudgetSetupDone) {
        if (isCurrencySetupDone && isWalletSetupDone && isBudgetSetupDone) {
            toastState.showSuccess(
                message = strings.setupDoneDesc,
                title = strings.success,
                scope = coroutineScope
            )
            viewModel.completeSetup { onFinished() }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WangHeader()

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = strings.setupTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    lineHeight = 36.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = strings.setupSubtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            SetupCard(
                title = strings.setupCurrencyCardTitle,
                description = strings.setupCurrencyCardDesc,
                icon = Icons.Rounded.CurrencyExchange,
                isDone = isCurrencySetupDone,
                onStartClick = { showCurrencyDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SetupCard(
                title = strings.setupWalletCardTitle,
                description = strings.setupWalletCardDesc,
                icon = Icons.Outlined.AccountBalanceWallet,
                isDone = isWalletSetupDone,
                onStartClick = {
                    if (!isCurrencySetupDone) {
                        toastState.showInfo(
                            message = strings.setupCurrencyRequiredInfo,
                            scope = coroutineScope
                        )
                        showCurrencyDialog = true
                    } else {
                        showWalletDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SetupCard(
                title = strings.setupBudgetCardTitle,
                description = strings.setupBudgetCardDesc,
                icon = Icons.Rounded.TrackChanges,
                isDone = isBudgetSetupDone,
                onStartClick = {
                    if (!isCurrencySetupDone) {
                        toastState.showInfo(
                            message = strings.setupCurrencyRequiredInfo,
                            scope = coroutineScope
                        )
                        showCurrencyDialog = true
                    } else {
                        showBudgetDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Dialogs
        if (showCurrencyDialog) {
            CurrencyDialog(
                currentCurrency = selectedCurrency,
                onDismiss = { showCurrencyDialog = false },
                onSave = { currencyType ->
                    viewModel.saveCurrency(currencyType)
                    showCurrencyDialog = false
                    toastState.showSuccess(
                        message = strings.currencySavedMsg(currencyType.name, currencyType.id),
                        title = strings.currencySavedTitle,
                        scope = coroutineScope
                    )
                }
            )
        }

        if (showWalletDialog) {
            WalletDialog(
                currentCurrency = selectedCurrency,
                onDismiss = { showWalletDialog = false },
                onSave = { name, type, balance ->
                    viewModel.saveWallet(name, type, balance, strings.defaultWalletName)
                    showWalletDialog = false
                    toastState.showSuccess(
                        message = strings.walletSavedMsg(name.ifBlank { strings.defaultWalletName }),
                        title = strings.walletCreatedTitle,
                        scope = coroutineScope
                    )
                }
            )
        }

        if (showBudgetDialog) {
            BudgetDialog(
                currentCurrency = selectedCurrency,
                categories = categories,
                onDismiss = { showBudgetDialog = false },
                onSave = { amount, categoryId, newCategoryName ->
                    viewModel.saveBudget(amount, categoryId, newCategoryName)
                    showBudgetDialog = false
                    toastState.showSuccess(
                        message = strings.budgetSavedMsg(selectedCurrency.name, amount),
                        title = strings.budgetSavedTitle,
                        scope = coroutineScope
                    )
                }
            )
        }
    }
}

@Composable
private fun SetupCard(
    title: String,
    description: String,
    icon: ImageVector,
    isDone: Boolean,
    onStartClick: () -> Unit
) {
    val strings = LocalStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (isDone) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = strings.completed,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!isDone) {
                WTonalButton(
                    text = strings.start,
                    onClick = onStartClick,
                    trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetupScreenPreview() {
    WangTheme {
        Surface {
            SetupScreen(onFinished = {})
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SetupScreenDarkPreview() {
    WangTheme {
        Surface {
            SetupScreen(onFinished = {})
        }
    }
}