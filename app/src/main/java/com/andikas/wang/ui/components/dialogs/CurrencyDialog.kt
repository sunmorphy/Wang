package com.andikas.wang.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.ui.components.WPrimaryButton
import com.andikas.wang.ui.components.WRadioGroup

@Composable
fun CurrencyDialog(
    currentCurrency: CurrencyType,
    onSave: (CurrencyType) -> Unit,
    onDismiss: () -> Unit
) {
    val strings = LocalStrings.current
    var selectedCurrency by remember { mutableStateOf(currentCurrency) }

    WMainDialog(
        onDismiss = onDismiss,
        icon = Icons.Rounded.CurrencyExchange,
        title = strings.manageCurrencyTitle,
        description = strings.manageCurrencyDesc
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = strings.manageCurrencyInfo,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WRadioGroup(
                    items = CurrencyType.entries,
                    selectedItem = selectedCurrency,
                    onItemSelected = { selectedCurrency = it },
                    prefix = { it.id },
                    label = { it.label },
                    subLabel = null
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                WPrimaryButton(
                    text = strings.save,
                    onClick = { onSave(selectedCurrency) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}