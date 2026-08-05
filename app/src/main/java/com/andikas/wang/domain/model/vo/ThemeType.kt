package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.ui.graphics.vector.ImageVector
import com.andikas.wang.ui.i18n.AppStrings

enum class ThemeType(
    override val id: String,
    override val label: String,
    override val icon: ImageVector,
    val themeCode: String,
    val activeIcon: ImageVector
) : SelectableOption {
    SYSTEM("1", "System", Icons.Outlined.BrightnessAuto, "SYSTEM", Icons.Rounded.BrightnessAuto) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.system
    },
    LIGHT("2", "Light", Icons.Outlined.LightMode, "LIGHT", Icons.Rounded.LightMode) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.light
    },
    DARK("3", "Dark", Icons.Outlined.DarkMode, "DARK", Icons.Rounded.DarkMode) {
        override fun getLocalizedLabel(strings: AppStrings): String = strings.dark
    }
}