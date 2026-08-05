package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.RecommendationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    @Query("SELECT * FROM recommendations ORDER BY generatedAt DESC")
    fun getAllRecommendations(): Flow<List<RecommendationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommendation: RecommendationEntity): Long

    @Delete
    suspend fun deleteRecommendation(recommendation: RecommendationEntity)

    @Query("DELETE FROM recommendations")
    suspend fun clearAll()
}
