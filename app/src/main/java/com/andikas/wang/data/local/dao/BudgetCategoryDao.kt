package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.BudgetCategoryEntity
import kotlinx.coroutines.flow.Flow

data class CategoryBudgetBreakdown(
    val id: Int,
    val budgetId: Int,
    val categoryId: Int,
    val categoryName: String,
    val allocatedAmount: Float,
)

@Dao
interface BudgetCategoryDao {
    @Query("SELECT * FROM budget_categories WHERE budgetId = :budgetId")
    fun getBudgetCategoriesForBudget(budgetId: Int): Flow<List<BudgetCategoryEntity>>

    @Query("""
        SELECT bc.id, bc.budgetId, bc.categoryId, c.name AS categoryName, bc.allocatedAmount
        FROM budget_categories bc
        JOIN categories c ON bc.categoryId = c.id
        WHERE bc.budgetId = :budgetId
    """)
    fun getBudgetCategoryBreakdowns(budgetId: Int): Flow<List<CategoryBudgetBreakdown>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetCategory(budgetCategory: BudgetCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetCategories(budgetCategoryList: List<BudgetCategoryEntity>)

    @Update
    suspend fun updateBudgetCategory(budgetCategory: BudgetCategoryEntity)

    @Delete
    suspend fun deleteBudgetCategory(budgetCategory: BudgetCategoryEntity)
}
