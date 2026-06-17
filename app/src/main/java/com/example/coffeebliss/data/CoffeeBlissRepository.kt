package com.example.coffeebliss.data

import kotlinx.coroutines.flow.Flow

/**
 * The Repository is the "single source of truth" for data.
 *
 * In MVVM, the ViewModel never talks to the database directly — it asks the Repository,
 * and the Repository asks the DAOs. This keeps data access in one tidy place.
 */
class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {

    // ----- Members -----
    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    fun getMemberById(memberId: Long): Flow<Member?> = memberDao.getMemberById(memberId)

    suspend fun insertMember(member: Member): Long = memberDao.insert(member)

    suspend fun updateMember(member: Member) = memberDao.update(member)

    suspend fun getMemberOnce(memberId: Long): Member? = memberDao.getMemberByIdOnce(memberId)

    suspend fun addPoints(memberId: Long, delta: Int) = memberDao.addPoints(memberId, delta)

    suspend fun addEarnedPoints(memberId: Long, delta: Int) =
        memberDao.addEarnedPoints(memberId, delta)

    // ----- Transactions -----
    fun getTransactionsForMember(memberId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsForMember(memberId)

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insert(transaction)
}
