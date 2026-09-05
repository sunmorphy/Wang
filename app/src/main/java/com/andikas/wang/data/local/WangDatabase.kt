package com.andikas.wang.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.andikas.wang.data.local.dao.AnomalyStateDao
import com.andikas.wang.data.local.dao.BudgetCategoryDao
import com.andikas.wang.data.local.dao.BudgetDao
import com.andikas.wang.data.local.dao.CategoryDao
import com.andikas.wang.data.local.dao.GoalDao
import com.andikas.wang.data.local.dao.NotificationEventDao
import com.andikas.wang.data.local.dao.RecommendationDao
import com.andikas.wang.data.local.dao.TransactionDao
import com.andikas.wang.data.local.dao.WalletDao
import com.andikas.wang.data.local.entities.AnomalyModelStateEntity
import com.andikas.wang.data.local.entities.BudgetCategoryEntity
import com.andikas.wang.data.local.entities.BudgetEntity
import com.andikas.wang.data.local.entities.CategoryEntity
import com.andikas.wang.data.local.entities.GoalEntity
import com.andikas.wang.data.local.entities.NotificationEventEntity
import com.andikas.wang.data.local.entities.RecommendationEntity
import com.andikas.wang.data.local.entities.TransactionEntity
import com.andikas.wang.data.local.entities.WalletEntity
import java.util.concurrent.Executors

@Database(
    entities = [
        CategoryEntity::class,
        BudgetCategoryEntity::class,
        TransactionEntity::class,
        WalletEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        NotificationEventEntity::class,
        AnomalyModelStateEntity::class,
        RecommendationEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WangDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetCategoryDao(): BudgetCategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun walletDao(): WalletDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun notificationEventDao(): NotificationEventDao
    abstract fun anomalyStateDao(): AnomalyStateDao
    abstract fun recommendationDao(): RecommendationDao

    companion object {
        val CALLBACK = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed standard default categories on initial database creation
                Executors.newSingleThreadExecutor().execute {
                    val defaultCategories = listOf(
                        "Food",
                        "Transportation",
                        "Shopping",
                        "Entertainment",
                        "Bills"
                    )
                    val now = System.currentTimeMillis()
                    defaultCategories.forEach { categoryName ->
                        db.execSQL(
                            "INSERT INTO categories (name, isDefault, createdAt, updatedAt) VALUES ('$categoryName', 1, $now, $now)"
                        )
                    }
                }
            }
        }
    }
}
