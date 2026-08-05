package com.andikas.wang.ui.components

import android.icu.text.NumberFormat
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.domain.model.vo.SelectableOption
import com.andikas.wang.ui.theme.WangTheme
import com.andikas.wang.ui.utils.bottomBorder
import java.util.Locale

private val ComponentRadius = 24.dp
private val ComponentShape = RoundedCornerShape(ComponentRadius)

@Composable
fun WInputButton(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(ComponentShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable(enabled = enabled) { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun WTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun <T : SelectableOption> WDropdownField(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    dropdownHeight: Dp = 256.dp
) {
    var expanded by remember { mutableStateOf(false) }
    val strings = LocalStrings.current

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (expanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.surfaceContainer
                    )
                    .clickable(enabled = enabled) { expanded = !expanded }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentIcon = selectedOption?.icon ?: leadingIcon
                        if (currentIcon != null) {
                            Icon(
                                imageVector = currentIcon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Text(
                            text = selectedOption?.getLocalizedLabel(
                                strings
                            ) ?: placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedOption != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.85f)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Column(
                    modifier = Modifier
                        .height(dropdownHeight)
                        .verticalScroll(rememberScrollState())
                ) {
                    options.forEach { option ->
                        val isSelected = option == selectedOption
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.getLocalizedLabel(strings),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else null,
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            },
                            modifier = Modifier.background(
                                color =
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.3f
                                    )
                                    else Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    minLines: Int = 3,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            minLines = minLines,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .clip(ComponentShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(vertical = 12.dp),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 2.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun WCurrencyInput(
    label: String?,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    currencyType: CurrencyType = CurrencyType.IDR,
    enabled: Boolean = true
) {
    val formattedForSize = remember(value, currencyType) { formatCurrency(value, currencyType) }

    val dynamicTextStyle = when {
        formattedForSize.length <= 12 -> MaterialTheme.typography.displayMedium
        formattedForSize.length <= 16 -> MaterialTheme.typography.headlineLarge
        formattedForSize.length <= 24 -> MaterialTheme.typography.headlineMedium
        else -> MaterialTheme.typography.headlineSmall
    }.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

    Column(modifier = modifier.bottomBorder(1.dp, MaterialTheme.colorScheme.primary)) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val decimalPoint = if (currencyType.locale == Locale.GERMANY) ',' else '.'
                val sanitized = if (currencyType.supportsDecimals) {
                    newValue.filter { it.isDigit() || it == decimalPoint }
                } else {
                    newValue.filter { it.isDigit() }
                }
                if (sanitized.count { it == decimalPoint } <= 1) onValueChange(sanitized)
            },
            enabled = enabled,
            textStyle = dynamicTextStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            visualTransformation = remember(currencyType) {
                CurrencyVisualTransformation(
                    currencyType
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (currencyType.supportsDecimals) KeyboardType.Decimal else KeyboardType.NumberPassword
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = currencyType.id,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    innerTextField()
                }
            }
        )
    }
}

class CurrencyVisualTransformation(private val currencyType: CurrencyType) :
    VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = formatCurrency(originalText, currencyType)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                val sub = originalText.substring(0, offset.coerceIn(0, originalText.length))
                return formatCurrency(sub, currencyType).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (formattedText.isEmpty()) return 0
                val sub = formattedText.substring(0, offset.coerceIn(0, formattedText.length))
                // Count only characters that are part of the original input
                return sub.count { it.isDigit() || it == '.' || it == ',' }
                    .coerceIn(0, originalText.length)
            }
        }

        return TransformedText(
            androidx.compose.ui.text.AnnotatedString(formattedText),
            offsetMapping
        )
    }
}

fun formatCurrency(amount: String, currency: CurrencyType): String {
    if (amount.isEmpty()) return "0"

    return try {
        // Handle localized decimal separators (e.g., EUR uses ',' for decimals)
        val normalizedAmount = if (currency.locale == Locale.GERMANY) {
            amount.replace(',', '.')
        } else amount

        val parsed = normalizedAmount.toBigDecimal()
        val formatter = NumberFormat.getNumberInstance(currency.locale)

        if (currency.supportsDecimals && amount.contains(if (currency.locale == Locale.GERMANY) ',' else '.')) {
            formatter.minimumFractionDigits = 0
            formatter.maximumFractionDigits = 2
        } else {
            formatter.maximumFractionDigits = 0
        }

        var result = formatter.format(parsed)

        // Keep the decimal separator visible while typing (e.g., "0." or "0,")
        val separator = if (currency.locale == Locale.GERMANY) "," else "."
        if (amount.endsWith(separator) && !result.contains(separator)) {
            result += separator
        }

        result
    } catch (_: Exception) {
        amount
    }
}

@Preview(showBackground = true)
@Composable
fun InputsPreview() {
    WangTheme {
        Surface {
            InputsPreviewContent()
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InputsDarkPreview() {
    WangTheme {
        Surface {
            InputsPreviewContent()
        }
    }
}

@Composable
private fun InputsPreviewContent() {
    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var amountIdr by remember { mutableStateOf("150000") }
    var amountUsd by remember { mutableStateOf("12.50") }
    var selectedCurrency by remember { mutableStateOf<CurrencyType?>(null) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WInputButton(
            label = "Date",
            value = "12 Oct 2023",
            onClick = {},
            leadingIcon = Icons.Default.DateRange
        )

        WTextField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            placeholder = "Enter your name",
            leadingIcon = Icons.Default.Person
        )

        WDropdownField(
            label = "Category",
            options = CurrencyType.entries,
            selectedOption = selectedCurrency,
            onOptionSelected = { selectedCurrency = it },
            placeholder = "Choose category",
            leadingIcon = Icons.Rounded.Category
        )

        WTextArea(
            label = "Note",
            value = note,
            onValueChange = { note = it },
            placeholder = "Add a note..."
        )

        WCurrencyInput(
            label = "Amount",
            value = amountIdr,
            onValueChange = { amountIdr = it },
            currencyType = CurrencyType.IDR
        )

        WCurrencyInput(
            label = "Amount (USD)",
            value = amountUsd,
            onValueChange = { amountUsd = it },
            currencyType = CurrencyType.USD
        )
    }
}