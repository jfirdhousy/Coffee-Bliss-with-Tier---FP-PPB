package com.example.coffeebliss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One member of Coffee Bliss.
 *
 * @Entity tells Room this class is a table. Each property below is one column,
 * matching the "Tabel Members" design in the PRD.
 */
@Entity(tableName = "members")
data class Member(
    // Room creates the id automatically (1, 2, 3, ...). We start it at 0 so Room knows
    // it is a "new, unsaved" member until it gets a real id.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    // Spendable balance. Goes up on a purchase, down when a reward is redeemed.
    val points: Int = 0,
    // Lifetime points the member has ever earned. Only ever goes UP (purchases),
    // never down when redeeming. This is what decides the membership tier, so a
    // member never loses their Silver/Gold/Platinum status by spending points.
    val totalPointsEarned: Int = 0
)

/** The membership tier (status) for this member, derived from lifetime points earned. */
val Member.tier: MemberTier
    get() = MemberTier.forPoints(totalPointsEarned)
