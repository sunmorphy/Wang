package com.andikas.wang.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.domain.model.vo.SelectableOption
import com.andikas.wang.domain.model.vo.WalletType
import com.andikas.wang.ui.theme.WangTheme

private val ComponentRadius = 24.dp
private val ComponentShape = RoundedCornerShape(ComponentRadius)

@Composable
fun <T> WSelectionItem(
    item: T,
    isSelected: Boolean,
    onClick: () -> Unit,
    prefix: (T) -> String,
    label: (T) -> String,
    subLabel: ((T) -> String)?,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainer

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Circle
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(
                    if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = prefix(item),
                style = MaterialTheme.typography.titleMedium,
                color =
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label(item) + if (subLabel != null) " - ${subLabel(item)}" else "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor
                )
            }
        }

        // Selection Checkmark
        if (isSelected) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun <T> WRadioGroup(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    prefix: (T) -> String,
    label: (T) -> String,
    subLabel: ((T) -> String)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            WSelectionItem(
                item = item,
                isSelected = item == selectedItem,
                onClick = { onItemSelected(item) },
                label = label,
                prefix = prefix,
                subLabel = subLabel
            )
        }
    }
}

@Composable
fun WGridSelection(
    label: String?,
    items: List<SelectableOption>,
    onItemSelected: (SelectableOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val chunkedItems = items.chunked(3)
    var selectedGridItem by remember { mutableStateOf(items[0]) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )
        }

        chunkedItems.forEach { chunkedItem ->
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chunkedItem.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        WGridSelectionItem(
                            item = item,
                            isSelected = item == selectedGridItem,
                            onClick = {
                                selectedGridItem = item
                                onItemSelected(selectedGridItem)
                            },
                            icon = { it.icon },
                            label = { it.getLocalizedLabel(strings) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T> WGridSelectionItem(
    item: T,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: (T) -> ImageVector,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainer

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(ComponentShape)
            .background(backgroundColor)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon(item),
            contentDescription = label(item),
            modifier = Modifier.size(24.dp),
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label(item),
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectablesPreview() {
    WangTheme {
        Surface {
            SelectablesPreviewContent()
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectablesDarkPreview() {
    WangTheme {
        Surface {
            SelectablesPreviewContent()
        }
    }
}

@Composable
private fun SelectablesPreviewContent() {
    var selectedItem by remember { mutableStateOf(CurrencyType.entries[0]) }
    var selectedGridItem by remember { mutableStateOf(WalletType.entries[0]) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Selection Items", style = MaterialTheme.typography.labelLarge)
        WRadioGroup(
            items = CurrencyType.entries,
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            prefix = { it.id },
            label = { it.label },
            subLabel = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Grid Selection Items", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WalletType.entries.forEach { item ->
                Box(modifier = Modifier.weight(1f)) {
                    WGridSelectionItem(
                        item = item,
                        isSelected = item == selectedGridItem,
                        onClick = { selectedGridItem = item },
                        icon = { it.icon },
                        label = { it.label }
                    )
                }
            }
            WAddButton(onClick = {}, modifier = Modifier.weight(1f))
        }
    }
}
