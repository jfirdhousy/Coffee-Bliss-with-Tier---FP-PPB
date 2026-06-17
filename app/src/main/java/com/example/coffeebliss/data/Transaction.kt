package com.example.coffeebliss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One entry in a member's point history, matching the "Tabel Transactions" design in the PRD.
 *
 * It covers BOTH purchases and reward redemptions:
 *  - Purchase  -> positive [pointEarned] (points added), [amount] is the rupiah spent.
 *  - Redeem    -> negative [pointEarned] (points subtracted), [amount] is 0.
 *
 * `memberId` links this entry back to the Member it belongs to
 * (a "foreign key" in database terms).
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memberId: Long,
    val amount: Double,
    val pointEarned: Int,
    val date: String,
    // Short label for the history list, e.g. "Pembelian" or "Tukar reward: Espresso".
    val description: String = ""
) {
    /** True when this entry is a reward redemption (points were spent, not earned). */
    val isRedemption: Boolean get() = pointEarned < 0
}
