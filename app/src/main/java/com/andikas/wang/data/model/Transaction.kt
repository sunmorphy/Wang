package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType { INCOME, EXPENSE }

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val merchant: String,
    val category: String,
    val walletId: Long,
    val timestamp: Long,
    val notes: String? = null,
    val type: TransactionType
)
