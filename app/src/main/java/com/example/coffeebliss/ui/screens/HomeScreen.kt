package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.data.tier
import com.example.coffeebliss.ui.components.BottomTab
import com.example.coffeebliss.ui.components.CoffeeBottomBar
import com.example.coffeebliss.ui.components.PointsCard
import com.example.coffeebliss.ui.components.TierBadge
import com.example.coffeebliss.ui.viewmodel.CoffeeBlissViewModel

/**
 * Screen 2: the dashboard. Greets the active member, shows their points, and links to
 * the card, transactions, rewards and profile screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CoffeeBlissViewModel,
    onSelectTab: (BottomTab) -> Unit,
    onOpenCard: () -> Unit,
    onOpenTransactions: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenProfile: () -> Unit,
    onAddMember: () -> Unit
) {
    // collectAsStateWithLifecycle reads the ViewModel's StateFlow and re-draws when it changes.
    val member by viewModel.activeMember.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coffee Bliss", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { CoffeeBottomBar(current = BottomTab.HOME, onSelect = onSelectTab) }
    ) { padding ->
        val currentMember = member
        if (currentMember == null) {
            EmptyMembersState(
                modifier = Modifier.padding(padding),
                onAddMember = onAddMember
            )
        } else {
            DashboardContent(
                modifier = Modifier.padding(padding),
                member = currentMember,
                onOpenCard = onOpenCard,
                onOpenTransactions = onOpenTransactions,
                onOpenRewards = onOpenRewards,
                onOpenProfile = onOpenProfile
            )
        }
    }
}

@Composable
private fun DashboardContent(
    member: Member,
    onOpenCard: () -> Unit,
    onOpenTransactions: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Hi, ${member.name.substringBefore(' ')} 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        // Membership status / tier.
        TierBadge(tier = member.tier)
        PointsCard(points = member.points)

        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        DashboardMenuItem(
            title = "Kartu Member",
            subtitle = "Lihat kartu digital & QR code",
            icon = Icons.Default.AccountBox,
            onClick = onOpenCard
        )
        DashboardMenuItem(
            title = "Riwayat Transaksi",
            subtitle = "Lihat transaksi & tambah poin",
            icon = Icons.Default.ShoppingCart,
            onClick = onOpenTransactions
        )
        DashboardMenuItem(
            title = "Reward",
            subtitle = "Tukar poin dengan hadiah",
            icon = Icons.Default.Star,
            onClick = onOpenRewards
        )
        DashboardMenuItem(
            title = "Profil",
            subtitle = "Lihat & kelola akun",
            icon = Icons.Default.Person,
            onClick = onOpenProfile
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Shown when there are no members yet (e.g. if you start the app with no demo data). */
@Composable
private fun EmptyMembersState(
    onAddMember: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "☕", style = MaterialTheme.typography.displayMedium)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Belum ada member",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Daftarkan member pertama untuk mulai mengumpulkan poin.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onAddMember) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Daftar Member")
        }
    }
}
