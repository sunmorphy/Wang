package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType { PAYMENT, TRANSFER, TOP_UP, INCOME, REFUND, RECURRING, BILL }

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val currencyCode: String = "IDR",
    val merchant: String?,
    val category: String,
    val type: TransactionType,
    val sourceApp: String = "Wang",
    val walletId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val isManual: Boolean = true,
    val isAnomalous: Boolean = false,
    val anomalyScore: Float = 0.0f,
    val notes: String? = null,
    val tags: String = "",
    val rawNotificationText: String? = null
)
