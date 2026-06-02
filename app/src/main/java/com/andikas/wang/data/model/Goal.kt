package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalCategory { TRAVEL, HOME, CAR, GENERAL, CUSTOM }

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: GoalCategory = GoalCategory.GENERAL,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val targetDate: Long? = null,
    val currencyCode: String = "IDR",
    val createdAt: Long = System.currentTimeMillis()
)
