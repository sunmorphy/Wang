package com.andikas.wang.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andikas.wang.ui.theme.WangTheme

private val ButtonRadius = 24.dp
private val ButtonShape = RoundedCornerShape(ButtonRadius)

@Composable
fun WangPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    ) {
        Text(text = text)
    }
}

@Composable
fun WangSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    ) {
        Text(text = text)
    }
}

@Composable
fun WangTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    ) {
        Text(text = text)
    }
}

@Composable
fun WangErrorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    ) {
        Text(text = text)
    }
}

@Composable
fun WangOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        ButtonContent(
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            content = { Text(text = text) }
        )
    }
}

@Composable
fun WangSurfaceButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    ) {
        Text(text = text)
    }
}

@Composable
fun WangIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    useBackground: Boolean = true,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier.size(
            if (useBackground) 48.dp else 24.dp
        ),
        enabled = enabled,
        shape = CircleShape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (useBackground) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
            contentColor = if (useBackground) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = ButtonShape,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = shape
    ) {
        ButtonContent(
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            content = content
        )
    }
}

@Composable
private fun RowScope.ButtonContent(
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    content: @Composable RowScope.() -> Unit
) {
    if (leadingIcon != null) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
    }

    content()

    if (trailingIcon != null) {
        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    WangTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WangPrimaryButton(text = "Primary Button", onClick = {})
                WangSecondaryButton(text = "Secondary Button", onClick = {})
                WangTonalButton(text = "Tonal Button", onClick = {})
                WangErrorButton(text = "Error Button", onClick = {})
                WangOutlinedButton(text = "Outlined Button", onClick = {})
                WangSurfaceButton(text = "Surface Button", onClick = {})
                WangIconButton(imageVector = Icons.Default.Add, onClick = {})

                WangPrimaryButton(
                    text = "With Icon",
                    onClick = {},
                    leadingIcon = Icons.Default.Add
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ButtonsDarkPreview() {
    WangTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WangPrimaryButton(text = "Primary Button", onClick = {})
                WangSecondaryButton(text = "Secondary Button", onClick = {})
                WangTonalButton(text = "Tonal Button", onClick = {})
                WangErrorButton(text = "Error Button", onClick = {})
                WangOutlinedButton(text = "Outlined Button", onClick = {})
                WangSurfaceButton(text = "Surface Button", onClick = {})
                WangIconButton(imageVector = Icons.Default.Add, onClick = {})

                WangPrimaryButton(
                    text = "With Icon",
                    onClick = {},
                    leadingIcon = Icons.Default.Add
                )
            }
        }
    }
}
