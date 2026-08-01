package com.scatter97.chesscamerapgnmobile.domain.calibration

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardCalibrationTest {
    @Test
    fun calibrationCompletesAfterFourPointsAndRejectsExtraPoints() {
        val points = listOf(
            NormalizedPoint(0.1f, 0.1f),
            NormalizedPoint(0.9f, 0.1f),
            NormalizedPoint(0.9f, 0.9f),
            NormalizedPoint(0.1f, 0.9f),
            NormalizedPoint(0.5f, 0.5f),
        )

        val state = points.fold(BoardCalibrationState()) { current, point ->
            current.add(point)
        }

        assertTrue(state.isComplete)
        assertEquals(4, state.points.size)
    }

    @Test
    fun undoAndResetReturnToEditableStates() {
        val state = BoardCalibrationState()
            .add(NormalizedPoint(0.1f, 0.1f))
            .add(NormalizedPoint(0.9f, 0.1f))

        val undone = state.undo()
        assertEquals(1, undone.points.size)
        assertFalse(undone.isComplete)

        val reset = undone.reset()
        assertTrue(reset.points.isEmpty())
    }

    @Test
    fun bilinearGridUsesExpectedCenterForRectangle() {
        val center = bilinearPoint(
            topLeft = NormalizedPoint(0.1f, 0.2f),
            topRight = NormalizedPoint(0.9f, 0.2f),
            bottomRight = NormalizedPoint(0.9f, 0.8f),
            bottomLeft = NormalizedPoint(0.1f, 0.8f),
            u = 0.5f,
            v = 0.5f,
        )

        assertEquals(0.5f, center.x, 0.0001f)
        assertEquals(0.5f, center.y, 0.0001f)
    }
}
