package com.andikas.wang.vo

import java.util.Locale

enum class CurrencyType(val prefix: String, val locale: Locale, val supportsDecimals: Boolean) {
    IDR("Rp", Locale.forLanguageTag("in-ID"), false),
    USD("$", Locale.US, true),
    AUD("A$", Locale.US, true),
    SGD("S$", Locale.US, true),
    EUR("€", Locale.GERMANY, true),
    GBP("£", Locale.UK, true),
    JPY("¥", Locale.JAPAN, false),
    MYR("RM", Locale.forLanguageTag("ms-MY"), true),
    PHP("₱", Locale.forLanguageTag("en-PH"), true),
    VND("₫", Locale.forLanguageTag("vi-VN"), false)
}
