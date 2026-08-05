package com.andikas.wang.ui.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    private val currencyToLocale = mapOf(
        "IDR" to Locale.forLanguageTag("in-ID"),
        "USD" to Locale.US,
        "EUR" to Locale.GERMANY,
        "GBP" to Locale.UK,
        "JPY" to Locale.JAPAN,
        "SGD" to Locale.forLanguageTag("en-SG"),
        "MYR" to Locale.forLanguageTag("ms-MY")
    )

    fun format(amount: Double, currencyCode: String): String {
        return try {
            val locale = currencyToLocale[currencyCode.uppercase()] ?: Locale.US
            val currency = Currency.getInstance(currencyCode.uppercase())
            val formatter = NumberFormat.getCurrencyInstance(locale)
            formatter.currency = currency
            
            // Adjust fractions for currencies like IDR or JPY that don't use decimals in standard display
            if (currencyCode.uppercase() == "IDR" || currencyCode.uppercase() == "JPY") {
                formatter.maximumFractionDigits = 0
            } else {
                formatter.maximumFractionDigits = 2
            }
            
            formatter.format(amount)
        } catch (_: Exception) {
            // Fallback to custom formatting
            val symbol = when (currencyCode.uppercase()) {
                "IDR" -> "Rp"
                "USD" -> "$"
                "EUR" -> "€"
                "GBP" -> "£"
                "JPY" -> "¥"
                else -> "$currencyCode "
            }
            if (currencyCode.uppercase() == "IDR" || currencyCode.uppercase() == "JPY") {
                String.format(Locale.US, "%s%,.0f", symbol, amount)
            } else {
                String.format(Locale.US, "%s%,.2f", symbol, amount)
            }
        }
    }
}
