package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(val categoryId: Int, val categoryName: String, val total: Float)
data class MerchantTotal(val merchant: String, val total: Float)

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs")
    suspend fun deleteTransactionsByDateRange(startMs: Long, endMs: Long): Int

    @Query("SELECT * FROM transactions WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getTransactionsInRange(startTime: Long, endTime: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE walletId = :walletId ORDER BY timestamp DESC")
    fun getTransactionsByWallet(walletId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY timestamp DESC")
    fun getTransactionsByCategory(categoryId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND type = 'INCOME'")
    fun getTotalIncomeBetween(startMs: Long, endMs: Long): Flow<Float?>

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND (type = 'EXPENSES' OR type = 'PAYMENT')")
    fun getTotalSpentBetween(startMs: Long, endMs: Long): Flow<Float?>

    @Query("""
        SELECT t.categoryId, c.name AS categoryName, SUM(t.amount) as total 
        FROM transactions t 
        JOIN categories c ON t.categoryId = c.id 
        WHERE t.timestamp >= :startMs AND t.timestamp <= :endMs AND (t.type = 'EXPENSES' OR t.type = 'PAYMENT') 
        GROUP BY t.categoryId 
        ORDER BY total DESC 
        LIMIT :limit
    """)
    fun getTopCategoriesBetween(startMs: Long, endMs: Long, limit: Int): Flow<List<CategoryTotal>>

    @Query("""
        SELECT merchant, SUM(amount) as total 
        FROM transactions 
        WHERE timestamp >= :startMs AND timestamp <= :endMs AND (type = 'EXPENSES' OR type = 'PAYMENT') AND merchant IS NOT NULL 
        GROUP BY merchant 
        ORDER BY total DESC 
        LIMIT :limit
    """)
    fun getTopMerchantsBetween(startMs: Long, endMs: Long, limit: Int): Flow<List<MerchantTotal>>

    @Query("SELECT COUNT(*) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs")
    fun countTransactionsByDateRange(startMs: Long, endMs: Long): Flow<Int>

    @Query("SELECT * FROM transactions WHERE isAnomalous = 1 ORDER BY timestamp DESC")
    fun getAnomalousTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type AND timestamp >= :startMs AND timestamp <= :endMs ORDER BY timestamp DESC")
    fun getTransactionsByTypeAndDateRange(type: String, startMs: Long, endMs: Long): Flow<List<TransactionEntity>>
}
