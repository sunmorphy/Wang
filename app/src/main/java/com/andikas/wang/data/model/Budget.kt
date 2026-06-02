package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthYear: String, // format: "2026-06"
    val totalBudget: Double,
    val categoryBudgets: String, // JSON mapping of Category -> Limit
    val currencyCode: String = "IDR"
)
