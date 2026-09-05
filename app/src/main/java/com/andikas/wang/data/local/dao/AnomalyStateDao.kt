package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.AnomalyModelStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnomalyStateDao {
    @Query("SELECT * FROM anomaly_model_states WHERE categoryId = :categoryId LIMIT 1")
    fun getModelStateForCategory(categoryId: Int): Flow<AnomalyModelStateEntity?>

    @Query("SELECT * FROM anomaly_model_states")
    fun getAllModelStates(): Flow<List<AnomalyModelStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateState(state: AnomalyModelStateEntity): Long

    @Delete
    suspend fun deleteState(state: AnomalyModelStateEntity)
}
