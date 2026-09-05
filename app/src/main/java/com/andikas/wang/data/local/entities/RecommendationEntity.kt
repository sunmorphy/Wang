package com.andikas.wang.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendations")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val generatedAt: Long = System.currentTimeMillis(),
    val isAiGenerated: Boolean = false,
    val promptVersion: Int = 1,
    val source: String = "FIN_STATISTICAL"
)
