package com.scatter97.chesscamerapgnmobile.ui.camera

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CameraGuideOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val side = minOf(size.width, size.height) * 0.82f
        val left = (size.width - side) / 2f
        val top = (size.height - side) / 2f
        val step = side / 8f
        val lineColor = Color.White.copy(alpha = 0.46f)

        drawRect(
            color = Color.White.copy(alpha = 0.8f),
            topLeft = Offset(left, top),
            size = Size(side, side),
            style = Stroke(width = 3f),
        )
        for (index in 1 until 8) {
            val x = left + index * step
            val y = top + index * step
            drawLine(lineColor, Offset(x, top), Offset(x, top + side), strokeWidth = 1.5f)
            drawLine(lineColor, Offset(left, y), Offset(left + side, y), strokeWidth = 1.5f)
        }
    }
}
