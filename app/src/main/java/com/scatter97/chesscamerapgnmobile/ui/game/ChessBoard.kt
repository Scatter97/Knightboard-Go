package com.scatter97.chesscamerapgnmobile.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scatter97.chesscamerapgnmobile.domain.game.BoardPiece
import com.scatter97.chesscamerapgnmobile.domain.game.ChessSession
import com.scatter97.chesscamerapgnmobile.ui.theme.Amber

private val LightSquare = Color(0xFFE7D7B6)
private val DarkSquare = Color(0xFF8A684B)
private val LegalMove = Color(0xB85DE69A)

@Composable
fun KnightboardChessBoard(
    session: ChessSession,
    selected: String?,
    enabled: Boolean,
    onSquarePressed: (String) -> Unit,
) {
    val legalDestinations = selected?.let(session::legalDestinations).orEmpty()
    Column(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
        for (rank in 8 downTo 1) {
            Row(modifier = Modifier.weight(1f)) {
                for (file in 'a'..'h') {
                    val square = "$file$rank"
                    val isLight = ((file.code - 'a'.code) + rank) % 2 == 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(if (isLight) LightSquare else DarkSquare)
                            .then(
                                when {
                                    selected == square -> Modifier.border(3.dp, Amber)
                                    square in legalDestinations -> Modifier.border(3.dp, LegalMove)
                                    else -> Modifier
                                },
                            )
                            .then(if (enabled) Modifier.clickable { onSquarePressed(square) } else Modifier),
                        contentAlignment = Alignment.Center,
                    ) {
                        session.pieceAt(square)?.let { piece ->
                            Text(
                                text = piece.symbol(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (piece.isWhite) Color.White else Color(0xFF151515),
                                textAlign = TextAlign.Center,
                            )
                        }
                        if (file == 'a') {
                            Text(
                                text = rank.toString(),
                                modifier = Modifier.align(Alignment.TopStart).padding(2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isLight) DarkSquare else LightSquare,
                            )
                        }
                        if (rank == 1) {
                            Text(
                                text = file.toString(),
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

private fun BoardPiece.symbol(): String = when (type.name) {
    "KING" -> if (isWhite) "♔" else "♚"
    "QUEEN" -> if (isWhite) "♕" else "♛"
    "ROOK" -> if (isWhite) "♖" else "♜"
    "BISHOP" -> if (isWhite) "♗" else "♝"
    "KNIGHT" -> if (isWhite) "♘" else "♞"
    else -> if (isWhite) "♙" else "♟"
}
