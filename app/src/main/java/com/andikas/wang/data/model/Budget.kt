package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val period: String // e.g., "OCT 2023"
)
