package com.scatter97.chesscamerapgnmobile.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scatter97.chesscamerapgnmobile.domain.chess.ChessPosition
import com.scatter97.chesscamerapgnmobile.domain.chess.Piece
import com.scatter97.chesscamerapgnmobile.domain.chess.PieceType
import com.scatter97.chesscamerapgnmobile.domain.chess.Side
import com.scatter97.chesscamerapgnmobile.domain.chess.Square
import com.scatter97.chesscamerapgnmobile.ui.theme.Amber
import com.scatter97.chesscamerapgnmobile.ui.theme.PanelRaised

private val LightSquare = Color(0xFFE7D7B6)
private val DarkSquare = Color(0xFF8A684B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualBoardScreen(onBack: () -> Unit) {
    var position by remember { mutableStateOf(ChessPosition.initial()) }
    var selected by remember { mutableStateOf<Square?>(null) }
    var status by remember { mutableStateOf("Select a ${position.sideToMove.name.lowercase()} piece, then its destination.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Virtual Board") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("${position.sideToMove.name} to move", style = MaterialTheme.typography.titleMedium)
                    Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            ChessBoard(
                position = position,
                selected = selected,
                onSquarePressed = { square ->
                    val source = selected
                    if (source == null) {
                        if (position.board[square]?.side == position.sideToMove) {
                            selected = square
                            status = "${square} selected. Choose a destination."
                        } else {
                            status = "Choose one of ${position.sideToMove.name.lowercase()}’s pieces."
                        }
                    } else if (source == square) {
                        selected = null
                        status = "Selection cleared."
                    } else {
                        val attempt = position.attemptMove(source, square)
                        if (attempt.move != null) {
                            position = attempt.position
                            selected = null
                            status = "Move ${attempt.move} confirmed."
                        } else {
                            selected = null
                            status = attempt.error ?: "Move could not be confirmed."
                        }
                    }
                },
            )

            Text(
                text = "Manual confirmation prototype: standard piece movement, captures, blocked paths, and pawn promotion to queen are supported. Check, castling, and en passant are added with the full rules engine.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (position.moves.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(position.moves) { move ->
                        Text(
                            text = move.toString(),
                            modifier = Modifier
                                .background(PanelRaised, MaterialTheme.shapes.small)
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }

            Button(
                onClick = {
                    position = ChessPosition.initial()
                    selected = null
                    status = "Board reset. White to move."
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Reset board") }
        }
    }
}

@Composable
private fun ChessBoard(
    position: ChessPosition,
    selected: Square?,
    onSquarePressed: (Square) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
        for (rank in 7 downTo 0) {
            Row(modifier = Modifier.weight(1f)) {
                for (file in 0..7) {
                    val square = Square(file, rank)
                    val isLight = (file + rank) % 2 != 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(if (isLight) LightSquare else DarkSquare)
                            .then(if (selected == square) Modifier.border(3.dp, Amber) else Modifier)
                            .clickable { onSquarePressed(square) },
                        contentAlignment = Alignment.Center,
                    ) {
                        position.board[square]?.let { piece ->
                            Text(
                                text = piece.symbol(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (piece.side == Side.White) Color.White else Color(0xFF151515),
                                textAlign = TextAlign.Center,
                            )
                        }
                        if (file == 0) {
                            Text(
                                text = "${rank + 1}",
                                modifier = Modifier.align(Alignment.TopStart).padding(2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isLight) DarkSquare else LightSquare,
                            )
                        }
                        if (rank == 0) {
                            Text(
                                text = ('a'.code + file).toChar().toString(),
                                modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isLight) DarkSquare else LightSquare,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Piece.symbol(): String = when (side) {
    Side.White -> when (type) {
        PieceType.King -> "♔"; PieceType.Queen -> "♕"; PieceType.Rook -> "♖"
        PieceType.Bishop -> "♗"; PieceType.Knight -> "♘"; PieceType.Pawn -> "♙"
    }
    Side.Black -> when (type) {
        PieceType.King -> "♚"; PieceType.Queen -> "♛"; PieceType.Rook -> "♜"
        PieceType.Bishop -> "♝"; PieceType.Knight -> "♞"; PieceType.Pawn -> "♟"
    }
}
