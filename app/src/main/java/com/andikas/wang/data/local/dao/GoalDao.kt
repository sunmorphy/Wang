package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM savings_goals ORDER BY targetDate ASC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM savings_goals WHERE id = :id")
    suspend fun getGoalById(id: Int): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goalEntity: GoalEntity): Long

    @Update
    suspend fun updateGoal(goalEntity: GoalEntity)

    @Delete
    suspend fun deleteGoal(goalEntity: GoalEntity)
}
