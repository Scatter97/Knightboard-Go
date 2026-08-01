package com.scatter97.chesscamerapgnmobile.ui.calibration

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.scatter97.chesscamerapgnmobile.domain.calibration.BoardCalibrationState
import com.scatter97.chesscamerapgnmobile.domain.calibration.NormalizedPoint
import com.scatter97.chesscamerapgnmobile.domain.calibration.bilinearPoint

@Composable
fun CalibrationOverlay(
    state: BoardCalibrationState,
    onPointAdded: (NormalizedPoint) -> Unit,
    modifier: Modifier = Modifier,
) {
    var measuredSize by remember { mutableStateOf(IntSize.Zero) }
    val labelPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 18f
            isFakeBoldText = true
        }
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { measuredSize = it }
            .pointerInput(state.points, measuredSize) {
                detectTapGestures { offset ->
                    if (!state.isComplete && measuredSize.width > 0 && measuredSize.height > 0) {
                        onPointAdded(
                            NormalizedPoint(
                                x = (offset.x / measuredSize.width).coerceIn(0f, 1f),
                                y = (offset.y / measuredSize.height).coerceIn(0f, 1f),
                            ),
                        )
                    }
                }
            },
    ) {
        fun NormalizedPoint.toOffset() = Offset(x * size.width, y * size.height)

        state.points.forEachIndexed { index, point ->
            val center = point.toOffset()
            drawCircle(Color(0xFFFFC857), radius = 14f, center = center)
            drawCircle(Color.Black, radius = 14f, center = center, style = Stroke(width = 3f))
            drawContext.canvas.nativeCanvas.drawText(
                (index + 1).toString(),
                center.x - 5f,
                center.y + 6f,
                labelPaint,
            )
        }

        if (state.points.size >= 2) {
            val path = Path().apply {
                moveTo(state.points.first().x * size.width, state.points.first().y * size.height)
                state.points.drop(1).forEach { lineTo(it.x * size.width, it.y * size.height) }
                if (state.isComplete) close()
            }
            drawPath(path, Color(0xFFFFC857), style = Stroke(width = 5f))
        }

        if (state.isComplete) {
            val (topLeft, topRight, bottomRight, bottomLeft) = state.points
            for (index in 1 until 8) {
                val ratio = index / 8f
                val verticalStart = bilinearPoint(
                    topLeft, topRight, bottomRight, bottomLeft, ratio, 0f,
                ).toOffset()
                val verticalEnd = bilinearPoint(
                    topLeft, topRight, bottomRight, bottomLeft, ratio, 1f,
                ).toOffset()
                val horizontalStart = bilinearPoint(
                    topLeft, topRight, bottomRight, bottomLeft, 0f, ratio,
                ).toOffset()
                val horizontalEnd = bilinearPoint(
                    topLeft, topRight, bottomRight, bottomLeft, 1f, ratio,
                ).toOffset()
                drawLine(Color.White.copy(alpha = 0.62f), verticalStart, verticalEnd, 2f)
                drawLine(Color.White.copy(alpha = 0.62f), horizontalStart, horizontalEnd, 2f)
            }
        }
    }
}
