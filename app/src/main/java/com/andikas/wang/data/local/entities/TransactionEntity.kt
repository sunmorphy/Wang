package com.andikas.wang.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("categoryId"),
        Index("walletId"),
        Index("budgetId")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Float,
    val merchant: String? = null,
    val categoryId: Int,
    val type: String, // "INCOME" or "EXPENSES" (or "PAYMENT")
    val walletId: Int,
    val budgetId: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isManual: Boolean = true,
    val isAnomalous: Boolean = false,
    val anomalyScore: Float = 0.0f,
    val notes: String? = null,
    val tags: String = ""
)