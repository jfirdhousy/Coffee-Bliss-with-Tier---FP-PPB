package com.example.coffeebliss.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * The Coffee Bliss color scheme (Material 3).
 *
 * We use a single fixed light scheme so the coffee branding looks the same on every
 * device. (Dynamic "Material You" colors are intentionally turned off here.)
 */
private val CoffeeColorScheme = lightColorScheme(
    primary = CoffeeGreen,
    onPrimary = Color.White,
    primaryContainer = CoffeeGreenLight,
    onPrimaryContainer = CoffeeGreenDark,
    secondary = CoffeeBrown,
    onSecondary = Color.White,
    secondaryContainer = CoffeeBrownLight,
    onSecondaryContainer = CoffeeBrown,
    tertiary = CoffeeAmber,
    onTertiary = Color.White,
    tertiaryContainer = CoffeeAmberLight,
    onTertiaryContainer = CoffeeBrown,
    background = CoffeeCream,
    onBackground = CoffeeText,
    surface = Color.White,
    onSurface = CoffeeText,
    surfaceVariant = CoffeeGreenLight,
    onSurfaceVariant = CoffeeGreenDark
)

@Composable
fun CoffeeBlissTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography = Typography,
        content = content
    )
}
