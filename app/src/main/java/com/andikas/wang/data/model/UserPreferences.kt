package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey val id: Int = 1, // Singleton row
    val pin: String? = null,
    val language: String = "en",
    val currency: String = "USD",
    val isOnboardingCompleted: Boolean = false
)
