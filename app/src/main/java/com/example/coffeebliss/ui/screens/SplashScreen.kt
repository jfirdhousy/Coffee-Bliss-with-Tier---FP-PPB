package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Screen 1: the splash screen. It shows the logo for 2 seconds, then calls [onTimeout]
 * to move on to the dashboard.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // LaunchedEffect runs once when the screen appears. delay() waits without freezing the UI.
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "☕", fontSize = 96.sp) // ☕
            Spacer(Modifier.height(16.dp))
            Text(
                text = "COFFEE BLISS",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "MEMBERSHIP",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                letterSpacing = 4.sp
            )
        }
    }
}
