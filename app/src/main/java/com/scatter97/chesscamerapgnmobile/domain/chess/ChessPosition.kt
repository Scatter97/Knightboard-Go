package com.scatter97.chesscamerapgnmobile.domain.chess

enum class Side { White, Black;
    fun other() = if (this == White) Black else White
}

enum class PieceType { King, Queen, Rook, Bishop, Knight, Pawn }

data class Piece(val side: Side, val type: PieceType)

data class Square(val file: Int, val rank: Int) {
    init {
        require(file in 0..7 && rank in 0..7)
    }

    override fun toString(): String = "${('a'.code + file).toChar()}${rank + 1}"
}

data class Move(val from: Square, val to: Square) {
    override fun toString() = "$from-$to"
}

data class MoveAttempt(
    val position: ChessPosition,
    val move: Move? = null,
    val error: String? = null,
)

data class ChessPosition(
    val board: Map<Square, Piece>,
    val sideToMove: Side = Side.White,
    val moves: List<Move> = emptyList(),
) {
    fun attemptMove(from: Square, to: Square): MoveAttempt {
        val piece = board[from] ?: return MoveAttempt(this, error = "Choose one of ${sideToMove.name.lowercase()}’s pieces.")
        if (piece.side != sideToMove) return MoveAttempt(this, error = "It is ${sideToMove.name.lowercase()} to move.")
        if (board[to]?.side == piece.side) return MoveAttempt(this, error = "That square is occupied by your own piece.")
        if (!isPseudoLegal(piece, from, to)) return MoveAttempt(this, error = "That piece cannot move there.")

        val updated = board.toMutableMap().apply {
            remove(from)
            put(to, if (piece.type == PieceType.Pawn && to.rank in setOf(0, 7)) Piece(piece.side, PieceType.Queen) else piece)
        }
        val move = Move(from, to)
        return MoveAttempt(
            position = copy(board = updated, sideToMove = sideToMove.other(), moves = moves + move),
            move = move,
        )
    }

    private fun isPseudoLegal(piece: Piece, from: Square, to: Square): Boolean {
        val fileDelta = to.file - from.file
        val rankDelta = to.rank - from.rank
        return when (piece.type) {
            PieceType.King -> maxOf(kotlin.math.abs(fileDelta), kotlin.math.abs(rankDelta)) == 1
            PieceType.Queen -> (fileDelta == 0 || rankDelta == 0 || kotlin.math.abs(fileDelta) == kotlin.math.abs(rankDelta)) && pathIsClear(from, to)
            PieceType.Rook -> (fileDelta == 0 || rankDelta == 0) && pathIsClear(from, to)
            PieceType.Bishop -> kotlin.math.abs(fileDelta) == kotlin.math.abs(rankDelta) && pathIsClear(from, to)
            PieceType.Knight -> setOf(kotlin.math.abs(fileDelta), kotlin.math.abs(rankDelta)) == setOf(1, 2)
            PieceType.Pawn -> pawnMoveIsLegal(piece, from, to)
        }
    }

    private fun pawnMoveIsLegal(piece: Piece, from: Square, to: Square): Boolean {
        val direction = if (piece.side == Side.White) 1 else -1
        val startingRank = if (piece.side == Side.White) 1 else 6
        val fileDelta = to.file - from.file
        val rankDelta = to.rank - from.rank
        val target = board[to]
        return when {
            fileDelta == 0 && rankDelta == direction && target == null -> true
            fileDelta == 0 && rankDelta == 2 * direction && from.rank == startingRank && target == null && board[Square(from.file, from.rank + direction)] == null -> true
            kotlin.math.abs(fileDelta) == 1 && rankDelta == direction && target?.side == piece.side.other() -> true
            else -> false
        }
    }

    private fun pathIsClear(from: Square, to: Square): Boolean {
        val fileStep = (to.file - from.file).compareTo(0)
        val rankStep = (to.rank - from.rank).compareTo(0)
        var file = from.file + fileStep
        var rank = from.rank + rankStep
        while (file != to.file || rank != to.rank) {
            if (board[Square(file, rank)] != null) return false
            file += fileStep
            rank += rankStep
        }
        return true
    }

    companion object {
        fun initial(): ChessPosition {
            val pieces = mutableMapOf<Square, Piece>()
            val backRank = listOf(PieceType.Rook, PieceType.Knight, PieceType.Bishop, PieceType.Queen, PieceType.King, PieceType.Bishop, PieceType.Knight, PieceType.Rook)
            backRank.forEachIndexed { file, type ->
                pieces[Square(file, 0)] = Piece(Side.White, type)
                pieces[Square(file, 7)] = Piece(Side.Black, type)
                pieces[Square(file, 1)] = Piece(Side.White, PieceType.Pawn)
                pieces[Square(file, 6)] = Piece(Side.Black, PieceType.Pawn)
            }
            return ChessPosition(pieces)
        }
    }
}
