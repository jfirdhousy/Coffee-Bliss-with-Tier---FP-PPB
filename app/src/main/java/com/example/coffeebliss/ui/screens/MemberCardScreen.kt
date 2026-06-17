package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coffeebliss.data.tier
import com.example.coffeebliss.ui.components.QrCodePlaceholder
import com.example.coffeebliss.ui.components.TierBadge
import com.example.coffeebliss.ui.components.memberIdLabel
import com.example.coffeebliss.ui.viewmodel.CoffeeBlissViewModel

/**
 * Screen 3: the digital membership card, with name, member ID, a QR-style code and points.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    viewModel: CoffeeBlissViewModel,
    onBack: () -> Unit
) {
    val member by viewModel.activeMember.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Member") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val m = member
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (m == null) {
                Text("Memuat kartu...")
                return@Column
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "COFFEE BLISS",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "MEMBER CARD",
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 3.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.height(10.dp))
                    // Membership status / tier badge.
                    TierBadge(tier = m.tier)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = m.name.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ID : ${memberIdLabel(m.id)}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    // White panel behind the QR code so it stands out on the green card.
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        QrCodePlaceholder(
                            seed = m.id,
                            modifier = Modifier.padding(12.dp),
                            size = 160.dp,
                            // Real QR codes are black on white — keep it that way so it
                            // looks scannable, not tinted green.
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "POINTS",
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = m.points.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
