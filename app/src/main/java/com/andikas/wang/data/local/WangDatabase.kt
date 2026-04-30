package com.andikas.wang.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andikas.wang.data.local.dao.*
import com.andikas.wang.data.model.*

@Database(
    entities = [
        Wallet::class,
        Transaction::class,
        Budget::class,
        Goal::class,
        UserPreferences::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WangDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun transactionDao(): TransactionDao
}
