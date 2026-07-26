package com.andikas.wang.data.local

import androidx.room.TypeConverter
import com.andikas.wang.data.model.GoalCategory
import com.andikas.wang.data.model.TransactionType
import com.andikas.wang.data.model.WalletType

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = try {
        TransactionType.valueOf(value)
    } catch (e: Exception) {
        TransactionType.PAYMENT
    }

    @TypeConverter
    fun fromWalletType(type: WalletType): String = type.name

    @TypeConverter
    fun toWalletType(value: String): WalletType = try {
        WalletType.valueOf(value)
    } catch (e: Exception) {
        WalletType.CASH
    }

    @TypeConverter
    fun fromGoalCategory(category: GoalCategory): String = category.name

    @TypeConverter
    fun toGoalCategory(value: String): GoalCategory = try {
        GoalCategory.valueOf(value)
    } catch (e: Exception) {
        GoalCategory.GENERAL
    }
}
