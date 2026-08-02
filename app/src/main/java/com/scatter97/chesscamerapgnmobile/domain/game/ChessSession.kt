package com.scatter97.chesscamerapgnmobile.domain.game

import io.github.alluhemanth.chess.core.ChessGame
import io.github.alluhemanth.chess.core.board.Square
import io.github.alluhemanth.chess.core.game.GameResult
import io.github.alluhemanth.chess.core.move.Move
import io.github.alluhemanth.chess.core.piece.PieceColor
import io.github.alluhemanth.chess.core.piece.PieceType

data class BoardPiece(
    val isWhite: Boolean,
    val type: PieceType,
)

sealed interface MoveOutcome {
    data class Applied(val uci: String) : MoveOutcome
    data class PromotionRequired(val options: List<PieceType>) : MoveOutcome
    data class Rejected(val message: String) : MoveOutcome
}

/**
 * UI-friendly wrapper around chess-core's complete legal-move engine.
 * It handles check legality, castling, en passant, promotion, and draw rules.
 */
class ChessSession(private val game: ChessGame = ChessGame()) {
    fun sideToMove(): PieceColor = game.getCurrentPlayer()

    fun pieceAt(square: String): BoardPiece? = game.getBoard()[Square(square)]?.let { piece ->
        BoardPiece(isWhite = piece.color == PieceColor.WHITE, type = piece.pieceType)
    }

    fun legalDestinations(from: String): Set<String> = game.getLegalMoves()
        .filter { it.from.toString() == from }
        .map { it.to.toString() }
        .toSet()

    fun attemptMove(from: String, to: String, promotion: PieceType? = null): MoveOutcome {
        val candidates = game.getLegalMoves().filter {
            it.from.toString() == from && it.to.toString() == to
        }
        if (candidates.isEmpty()) return MoveOutcome.Rejected("That move is not legal in this position.")
        val promotionOptions = candidates.mapNotNull { it.promotionPieceType }.distinct()
        if (promotionOptions.isNotEmpty() && promotion == null) {
            return MoveOutcome.PromotionRequired(promotionOptions)
        }
        val move = candidates.firstOrNull { it.promotionPieceType == promotion }
            ?: return MoveOutcome.Rejected("Choose a valid promotion piece.")
        return if (game.makeMove(move)) MoveOutcome.Applied(move.toUci())
        else MoveOutcome.Rejected("That move is not legal in this position.")
    }

    fun applyUci(uci: String): Boolean = game.makeUciMove(uci)

    fun fen(): String = game.getFen()

    fun result(): GameResult = game.getGameResult()

    fun isOver(): Boolean = game.isGameOver()

    fun moves(): List<String> = game.getMoveHistory().map(Move::toUci)

    fun pgn(): String = game.getPgn()

    fun resultLabel(): String = when (val result = result()) {
        GameResult.Ongoing -> "Game in progress"
        is GameResult.Win -> "Checkmate — ${result.winner.name.lowercase().replaceFirstChar { it.uppercase() }} wins"
        GameResult.Draw.Stalemate -> "Draw — stalemate"
        GameResult.Draw.ThreefoldRepetition -> "Draw — threefold repetition"
        GameResult.Draw.FiftyMoveRule -> "Draw — 50-move rule"
        GameResult.Draw.InsufficientMaterial -> "Draw — insufficient material"
    }
}
