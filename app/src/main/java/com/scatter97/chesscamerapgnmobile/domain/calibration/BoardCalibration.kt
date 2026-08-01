package com.scatter97.chesscamerapgnmobile.domain.calibration

import kotlin.math.max
import kotlin.math.min

data class NormalizedPoint(
    val x: Float,
    val y: Float,
) {
    init {
        require(x in 0f..1f) { "x must be normalized" }
        require(y in 0f..1f) { "y must be normalized" }
    }
}

data class BoardCalibrationState(
    val points: List<NormalizedPoint> = emptyList(),
) {
    val isComplete: Boolean get() = points.size == REQUIRED_POINTS

    fun add(point: NormalizedPoint): BoardCalibrationState {
        if (isComplete) return this
        return copy(points = points + point)
    }

    fun undo(): BoardCalibrationState = copy(points = points.dropLast(1))

    fun reset(): BoardCalibrationState = BoardCalibrationState()

    companion object {
        const val REQUIRED_POINTS = 4
    }
}

fun bilinearPoint(
    topLeft: NormalizedPoint,
    topRight: NormalizedPoint,
    bottomRight: NormalizedPoint,
    bottomLeft: NormalizedPoint,
    u: Float,
    v: Float,
): NormalizedPoint {
    val safeU = min(1f, max(0f, u))
    val safeV = min(1f, max(0f, v))
    val x =
        topLeft.x * (1f - safeU) * (1f - safeV) +
            topRight.x * safeU * (1f - safeV) +
            bottomRight.x * safeU * safeV +
            bottomLeft.x * (1f - safeU) * safeV
    val y =
        topLeft.y * (1f - safeU) * (1f - safeV) +
            topRight.y * safeU * (1f - safeV) +
            bottomRight.y * safeU * safeV +
            bottomLeft.y * (1f - safeU) * safeV
    return NormalizedPoint(x, y)
}
