package com.example.coffeebliss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Database operations for transactions.
 */
@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    // Newest transactions first (ORDER BY id DESC).
    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY id DESC")
    fun getTransactionsForMember(memberId: Long): Flow<List<Transaction>>
}
