package com.andikas.wang.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_events",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("transactionId")]
)
data class NotificationEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val title: String,
    val body: String,
    val receivedAt: Long = System.currentTimeMillis(),
    val isParsed: Boolean = false,
    val transactionId: Int? = null,
    val parseError: String? = null
)
