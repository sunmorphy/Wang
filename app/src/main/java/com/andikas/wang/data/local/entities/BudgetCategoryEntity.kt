package com.andikas.wang.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget_categories",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("budgetId"), Index("categoryId")]
)
data class BudgetCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val budgetId: Int,
    val categoryId: Int,
    val allocatedAmount: Float,
)
