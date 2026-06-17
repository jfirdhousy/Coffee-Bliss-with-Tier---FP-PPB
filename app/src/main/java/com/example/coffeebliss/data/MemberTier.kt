package com.example.coffeebliss.data

/**
 * The membership status / tier a member can reach. Higher tiers are unlocked by
 * earning more points over time (see [Member.totalPointsEarned]).
 *
 * [minPoints] is the lifetime-points threshold needed to reach the tier.
 * Tiers are listed from lowest to highest.
 */
enum class MemberTier(
    val label: String,
    val minPoints: Int,
    val emoji: String
) {
    SILVER("Silver", 0, "🥈"),
    GOLD("Gold", 100, "🥇"),
    PLATINUM("Platinum", 300, "💎");

    /** The next tier up, or null if this is already the highest tier. */
    val next: MemberTier?
        get() = entries.getOrNull(ordinal + 1)

    companion object {
        /** Picks the highest tier whose threshold the member has reached. */
        fun forPoints(totalPointsEarned: Int): MemberTier =
            entries.last { totalPointsEarned >= it.minPoints }
    }
}

/**
 * How many more lifetime points are needed to reach the next tier.
 * Returns 0 when the member is already at the top tier ([MemberTier.PLATINUM]).
 */
fun MemberTier.pointsToNext(totalPointsEarned: Int): Int {
    val next = next ?: return 0
    return (next.minPoints - totalPointsEarned).coerceAtLeast(0)
}
