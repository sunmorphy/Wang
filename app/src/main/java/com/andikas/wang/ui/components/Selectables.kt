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
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingBag
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
import com.andikas.wang.ui.theme.WangTheme

private val ComponentRadius = 24.dp
private val ComponentShape = RoundedCornerShape(ComponentRadius)

@Composable
fun <T> WangSelectionItem(
    item: T,
    isSelected: Boolean,
    onClick: () -> Unit,
    prefix: (T) -> String,
    label: (T) -> String,
    subLabel: ((T) -> String),
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
                    text = "${label(item)} - ${subLabel(item)}",
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
fun <T> WangRadioGroup(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    prefix: (T) -> String,
    label: (T) -> String,
    subLabel: ((T) -> String),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            WangSelectionItem(
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
fun <T> WangGridSelectionItem(
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
            contentDescription = null,
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

private data class SampleItem(
    val id: String,
    val prefix: String,
    val label: String,
    val subLabel: String
)

private data class GridItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun SelectablesPreviewContent() {
    val items = remember {
        listOf(
            SampleItem("1", "Rp", "IDR", "Indonesian Rupiah"),
            SampleItem("2", "$", "USD", "US Dollar"),
            SampleItem("3", "€", "EUR", "Euro")
        )
    }
    var selectedItem by remember { mutableStateOf(items[0]) }

    val gridItems = remember {
        listOf(
            GridItem("1", "Food", Icons.Rounded.Restaurant),
            GridItem("2", "Shopping", Icons.Rounded.ShoppingBag),
            GridItem("3", "Others", Icons.Rounded.Category)
        )
    }
    var selectedGridItem by remember { mutableStateOf(gridItems[0]) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Selection Items", style = MaterialTheme.typography.labelLarge)
        WangRadioGroup(
            items = items,
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            prefix = { it.prefix },
            label = { it.label },
            subLabel = { it.subLabel }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Grid Selection Items", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            gridItems.forEach { item ->
                Box(modifier = Modifier.weight(1f)) {
                    WangGridSelectionItem(
                        item = item,
                        isSelected = item == selectedGridItem,
                        onClick = { selectedGridItem = item },
                        icon = { it.icon },
                        label = { it.label }
                    )
                }
            }
            WangAddButton(onClick = {}, modifier = Modifier.weight(1f))
        }
    }
}
