package com.example.coffeebliss.data

/**
 * A reward a member can redeem with their points.
 *
 * Rewards are a fixed menu (from the PRD), so they are plain Kotlin objects and are
 * NOT stored in the database.
 */
data class Reward(
    val name: String,
    val cost: Int,        // how many points it costs
    val emoji: String,    // small picture shown in the list
    val description: String
)

/** The reward list from the PRD: 50 → Espresso, 100 → Cappuccino, 150 → Latte Gratis. */
val rewardList = listOf(
    Reward(
        name = "Espresso",
        cost = 50,
        emoji = "☕", // ☕
        description = "Nikmati satu shot espresso hangat dari kami."
    ),
    Reward(
        name = "Cappuccino",
        cost = 100,
        emoji = "☕", // ☕
        description = "Secangkir cappuccino lembut dengan busa susu."
    ),
    Reward(
        name = "Latte Gratis",
        cost = 150,
        emoji = "🥛", // 🥛
        description = "Latte gratis sebagai hadiah loyalitas Anda."
    )
)
