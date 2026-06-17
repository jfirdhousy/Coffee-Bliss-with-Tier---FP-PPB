package com.example.coffeebliss.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Draws a QR-code-LOOKING square. It is decorative only — it is not a real, scannable
 * QR code (that would need an extra library). The pattern is generated from [seed]
 * (the member id), so each member always gets the same unique-looking square.
 */
@Composable
fun QrCodePlaceholder(
    seed: Long,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    color: Color = Color.Black
) {
    val cells = 11 // an 11 x 11 grid of squares
    Canvas(modifier = modifier.size(size)) {
        val cellSize = this.size.width / cells
        val random = Random(seed)
        for (row in 0 until cells) {
            for (col in 0 until cells) {
                // Fill the three corner "finder" blocks; randomize the rest.
                val filled = isCorner(row, col, cells) || random.nextBoolean()
                if (filled) {
                    drawRect(
                        color = color,
                        topLeft = Offset(col * cellSize, row * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }
    }
}

/** True if the cell is inside one of the three 3x3 corner blocks (like a real QR code). */
private fun isCorner(row: Int, col: Int, cells: Int): Boolean {
    val topLeft = row < 3 && col < 3
    val topRight = row < 3 && col >= cells - 3
    val bottomLeft = row >= cells - 3 && col < 3
    return topLeft || topRight || bottomLeft
}
