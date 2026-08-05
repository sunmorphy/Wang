package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Locale

enum class CurrencyType(
    override val id: String,
    override val label: String,
    override val icon: ImageVector,
    val locale: Locale,
    val supportsDecimals: Boolean
) : SelectableOption {
    IDR(
        "Rp",
        "IDR — Indonesian Rupiah",
        Icons.Rounded.AttachMoney,
        Locale.forLanguageTag("in-ID"),
        false
    ),
    USD("$", "USD — US Dollar", Icons.Rounded.AttachMoney, Locale.US, true),
    AUD("$", "AUD — Australian Dollar", Icons.Rounded.AttachMoney, Locale.US, true),
    SGD("S$", "SGD — Singapore Dollar", Icons.Rounded.AttachMoney, Locale.US, true),
    EUR("€", "EUR — Euro", Icons.Rounded.AttachMoney, Locale.GERMANY, true),
    GBP("£", "GBP — British Pound", Icons.Rounded.AttachMoney, Locale.UK, true),
    JPY("¥", "JPY — Japanese Yen", Icons.Rounded.AttachMoney, Locale.JAPAN, false),
    MYR(
        "RM",
        "MYR — Malaysian Ringgit",
        Icons.Rounded.AttachMoney,
        Locale.forLanguageTag("ms-MY"),
        true
    ),
    PHP(
        "₱",
        "PHP — Philippine Peso",
        Icons.Rounded.AttachMoney,
        Locale.forLanguageTag("en-PH"),
        true
    ),
    VND(
        "₫",
        "VND — Vietnamese Dong",
        Icons.Rounded.AttachMoney,
        Locale.forLanguageTag("vi-VN"),
        false
    )
}