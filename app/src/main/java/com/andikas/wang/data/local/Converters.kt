package com.andikas.wang.data.local

import androidx.room.TypeConverter
import com.andikas.wang.domain.model.vo.TransactionType
import com.andikas.wang.domain.model.vo.WalletType

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = try {
        TransactionType.valueOf(value)
    } catch (e: Exception) {
        TransactionType.EXPENSE
    }

    @TypeConverter
    fun fromWalletType(type: WalletType): String = type.name

    @TypeConverter
    fun toWalletType(value: String): WalletType = try {
        WalletType.valueOf(value)
    } catch (e: Exception) {
        WalletType.CASH
    }
}
