package com.andikas.wang.data.local.dao

import androidx.room.*
import com.andikas.wang.data.model.Transaction
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(val category: String, val total: Double)
data class MerchantTotal(val merchant: String, val total: Double)

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs")
    suspend fun deleteTransactionsByDateRange(startMs: Long, endMs: Long): Int

    @Query("SELECT * FROM transactions WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getTransactionsInRange(startTime: Long, endTime: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE walletId = :walletId ORDER BY timestamp DESC")
    fun getTransactionsByWallet(walletId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND type = 'INCOME'")
    fun getTotalIncomeBetween(startMs: Long, endMs: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND type = 'PAYMENT'")
    fun getTotalSpentBetween(startMs: Long, endMs: Long): Flow<Double?>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND type = 'PAYMENT' GROUP BY category ORDER BY total DESC LIMIT :limit")
    fun getTopCategoriesBetween(startMs: Long, endMs: Long, limit: Int): Flow<List<CategoryTotal>>

    @Query("SELECT merchant, SUM(amount) as total FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs AND type = 'PAYMENT' AND merchant IS NOT NULL GROUP BY merchant ORDER BY total DESC LIMIT :limit")
    fun getTopMerchantsBetween(startMs: Long, endMs: Long, limit: Int): Flow<List<MerchantTotal>>

    @Query("SELECT COUNT(*) FROM transactions WHERE timestamp >= :startMs AND timestamp <= :endMs")
    fun countTransactionsByDateRange(startMs: Long, endMs: Long): Flow<Int>

    @Query("SELECT * FROM transactions WHERE isAnomalous = 1 ORDER BY timestamp DESC")
    fun getAnomalousTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type AND timestamp >= :startMs AND timestamp <= :endMs ORDER BY timestamp DESC")
    fun getTransactionsByTypeAndDateRange(type: String, startMs: Long, endMs: Long): Flow<List<Transaction>>
}
