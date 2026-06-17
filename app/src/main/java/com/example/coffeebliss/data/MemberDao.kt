package com.example.coffeebliss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object. This is where we declare the database operations for members.
 * Room writes the actual SQLite code for us at build time.
 *
 * Functions that return [Flow] keep emitting new values whenever the data changes,
 * so the UI updates itself automatically.
 */
@Dao
interface MemberDao {

    @Insert
    suspend fun insert(member: Member): Long // returns the new member's id

    @Update
    suspend fun update(member: Member)

    @Query("SELECT * FROM members ORDER BY id ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :memberId")
    fun getMemberById(memberId: Long): Flow<Member?>

    // A one-time read (not a Flow). Used to re-check points before a redeem.
    @Query("SELECT * FROM members WHERE id = :memberId")
    suspend fun getMemberByIdOnce(memberId: Long): Member?

    // Adds points directly in the database (use a negative value to subtract). Doing the
    // math in SQL is always correct, even if the screen's copy is a little out of date.
    // Used for redeeming rewards (the spendable balance only).
    @Query("UPDATE members SET points = points + :delta WHERE id = :memberId")
    suspend fun addPoints(memberId: Long, delta: Int)

    // Used for a purchase: adds to the spendable balance AND the lifetime total
    // (the lifetime total is what drives the membership tier).
    @Query(
        "UPDATE members SET points = points + :delta, " +
            "totalPointsEarned = totalPointsEarned + :delta WHERE id = :memberId"
    )
    suspend fun addEarnedPoints(memberId: Long, delta: Int)
}
