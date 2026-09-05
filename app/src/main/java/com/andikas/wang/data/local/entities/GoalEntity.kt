package com.andikas.wang.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String = "GENERAL", // "TRAVEL", "HOME", "CAR", "GENERAL"
    val targetAmount: Float,
    val currentAmount: Float = 0f,
    val targetDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)