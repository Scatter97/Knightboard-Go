package com.scatter97.chesscamerapgnmobile.domain.chess

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ChessPositionTest {
    @Test
    fun openingPawnMoveChangesSideAndRecordsMove() {
        val start = ChessPosition.initial()
        val result = start.attemptMove(Square(4, 1), Square(4, 3))

        assertEquals(Side.Black, result.position.sideToMove)
        assertEquals(Piece(Side.White, PieceType.Pawn), result.position.board[Square(4, 3)])
        assertNull(result.position.board[Square(4, 1)])
        assertNotNull(result.move)
    }

    @Test
    fun rookCannotMoveThroughOwnPawn() {
        val result = ChessPosition.initial().attemptMove(Square(0, 0), Square(0, 3))

        assertNull(result.move)
        assertNotNull(result.error)
    }
}
