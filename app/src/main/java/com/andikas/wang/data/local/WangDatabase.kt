package com.andikas.wang.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andikas.wang.data.local.dao.*
import com.andikas.wang.data.model.*

@Database(
    entities = [
        Wallet::class,
        Transaction::class,
        Budget::class,
        Goal::class,
        UserPreferences::class,
        Recommendation::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WangDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun transactionDao(): TransactionDao
    abstract fun recommendationDao(): RecommendationDao
}
