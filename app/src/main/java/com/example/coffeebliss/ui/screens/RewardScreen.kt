package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coffeebliss.data.Reward
import com.example.coffeebliss.data.rewardList
import com.example.coffeebliss.ui.components.BottomTab
import com.example.coffeebliss.ui.components.CoffeeBottomBar
import com.example.coffeebliss.ui.viewmodel.CoffeeBlissViewModel
import kotlinx.coroutines.launch

/**
 * Screen 7: the reward list. Members redeem points for rewards. A confirmation dialog
 * shows the points before and after, and points are subtracted on success.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    viewModel: CoffeeBlissViewModel,
    onSelectTab: (BottomTab) -> Unit
) {
    val member by viewModel.activeMember.collectAsStateWithLifecycle()
    val points = member?.points ?: 0

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // When not null, the confirmation dialog for this reward is shown.
    var rewardToConfirm by remember { mutableStateOf<Reward?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reward") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { CoffeeBottomBar(current = BottomTab.REWARDS, onSelect = onSelectTab) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Poin Anda",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$points Poin",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            items(rewardList) { reward ->
                RewardRow(
                    reward = reward,
                    canRedeem = points >= reward.cost,
                    onRedeem = { rewardToConfirm = reward }
                )
            }
        }
    }

    // Confirmation dialog
    val reward = rewardToConfirm
    if (reward != null) {
        AlertDialog(
            onDismissRequest = { rewardToConfirm = null },
            title = { Text("Tukar reward ini?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${reward.emoji}  ${reward.name}  •  ${reward.cost} Poin",
                        fontWeight = FontWeight.Bold
                    )
                    Text("Total Poin: $points")
                    Text("Setelah ditukar: ${points - reward.cost}")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.redeemReward(reward) { success ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (success) "Reward '${reward.name}' berhasil ditukar!"
                                else "Poin tidak cukup untuk reward ini."
                            )
                        }
                    }
                    rewardToConfirm = null
                }) { Text("Konfirmasi") }
            },
            dismissButton = {
                TextButton(onClick = { rewardToConfirm = null }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun RewardRow(
    reward: Reward,
    canRedeem: Boolean,
    onRedeem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = reward.emoji, fontSize = 36.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = reward.name, fontWeight = FontWeight.Bold)
                Text(
                    text = "${reward.cost} Poin",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = onRedeem, enabled = canRedeem) {
                Text("Redeem")
            }
        }
    }
}
