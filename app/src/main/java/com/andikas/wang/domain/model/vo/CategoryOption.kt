package com.andikas.wang.domain.model.vo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.andikas.wang.ui.i18n.AppStrings

data class CategoryOption(
    override val id: String,
    override val label: String,
    override val icon: ImageVector = getCategoryIcon(label),
    val isDefault: Boolean = false
) : SelectableOption {

    val isEditable: Boolean
        get() = !isDefault

    override fun getLocalizedLabel(strings: AppStrings): String {
        return when (label.trim().lowercase()) {
            "food" -> strings.categoryFood
            "transportation" -> strings.categoryTransportation
            "shopping" -> strings.categoryShopping
            "entertainment" -> strings.categoryEntertainment
            "bills" -> strings.categoryBills
            "other" -> strings.categoryOther
            else -> label
        }
    }

    companion object {
        fun getCategoryIcon(name: String): ImageVector {
            return when (name.trim().lowercase()) {
                "food" -> Icons.Rounded.Restaurant
                "transportation" -> Icons.Rounded.DirectionsCar
                "shopping" -> Icons.Rounded.ShoppingBag
                "entertainment" -> Icons.Rounded.SportsEsports
                "bills" -> Icons.Rounded.ReceiptLong
                else -> Icons.Rounded.Category
            }
        }
    }
}
