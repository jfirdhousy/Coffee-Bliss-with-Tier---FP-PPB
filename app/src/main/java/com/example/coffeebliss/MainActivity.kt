package com.example.coffeebliss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coffeebliss.ui.navigation.CoffeeBlissApp
import com.example.coffeebliss.ui.theme.CoffeeBlissTheme

/**
 * The single Activity that hosts the whole Compose app.
 *
 * In a Compose app we usually have just one Activity; every "screen" is a Composable
 * managed by Navigation Compose (see CoffeeBlissApp).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeBlissTheme {
                CoffeeBlissApp()
            }
        }
    }
}
