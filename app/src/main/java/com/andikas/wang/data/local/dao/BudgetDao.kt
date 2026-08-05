package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY monthYear DESC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE monthYear = :monthYear LIMIT 1")
    fun getBudgetForMonth(monthYear: String): Flow<BudgetEntity?>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budgetEntity: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budgetEntity: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budgetEntity: BudgetEntity)
}
