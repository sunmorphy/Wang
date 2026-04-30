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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andikas.wang.ui.theme.WangTheme
import com.andikas.wang.vo.CurrencyType
import java.util.Locale

private val ComponentRadius = 24.dp
private val ComponentShape = RoundedCornerShape(ComponentRadius)

@Composable
fun WangInputButton(
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
            style = MaterialTheme.typography.titleSmall,
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
fun WangTextField(
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
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(ComponentShape)
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
fun WangTextArea(
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
            style = MaterialTheme.typography.titleSmall,
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
fun WangCurrencyInput(
    label: String,
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

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )

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
                        text = currencyType.prefix,
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

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WangInputButton(
            label = "Date",
            value = "12 Oct 2023",
            onClick = {},
            leadingIcon = Icons.Default.DateRange
        )

        WangTextField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            placeholder = "Enter your name",
            leadingIcon = Icons.Default.Person
        )

        WangTextArea(
            label = "Note",
            value = note,
            onValueChange = { note = it },
            placeholder = "Add a note..."
        )

        WangCurrencyInput(
            label = "Amount",
            value = amountIdr,
            onValueChange = { amountIdr = it },
            currencyType = CurrencyType.IDR
        )

        WangCurrencyInput(
            label = "Amount (USD)",
            value = amountUsd,
            onValueChange = { amountUsd = it },
            currencyType = CurrencyType.USD
        )
    }
}