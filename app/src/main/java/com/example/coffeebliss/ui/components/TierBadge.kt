package com.example.coffeebliss.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeebliss.data.MemberTier
import com.example.coffeebliss.ui.theme.TierGold
import com.example.coffeebliss.ui.theme.TierPlatinum
import com.example.coffeebliss.ui.theme.TierSilver

/** The pill background color for a tier. */
fun tierColor(tier: MemberTier): Color = when (tier) {
    MemberTier.SILVER -> TierSilver
    MemberTier.GOLD -> TierGold
    MemberTier.PLATINUM -> TierPlatinum
}

/**
 * A small colored pill showing the member's status / tier, e.g. "🥇 Gold Member".
 * Used on the dashboard, the membership card and the profile screen.
 */
@Composable
fun TierBadge(
    tier: MemberTier,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = tierColor(tier), shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = tier.emoji, fontSize = 13.sp)
        Text(
            text = "${tier.label} Member",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            fontSize = 13.sp
        )
    }
}
