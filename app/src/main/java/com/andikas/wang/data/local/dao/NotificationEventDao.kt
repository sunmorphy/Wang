package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.NotificationEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationEventDao {
    @Query("SELECT * FROM notification_events ORDER BY receivedAt DESC")
    fun getAllEvents(): Flow<List<NotificationEventEntity>>

    @Query("SELECT * FROM notification_events WHERE isParsed = 0 ORDER BY receivedAt ASC")
    fun getUnparsedEvents(): Flow<List<NotificationEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: NotificationEventEntity): Long

    @Update
    suspend fun updateEvent(event: NotificationEventEntity)

    @Delete
    suspend fun deleteEvent(event: NotificationEventEntity)
}
