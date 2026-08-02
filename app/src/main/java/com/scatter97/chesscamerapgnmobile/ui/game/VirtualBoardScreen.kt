package com.scatter97.chesscamerapgnmobile.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scatter97.chesscamerapgnmobile.domain.game.ChessSession
import com.scatter97.chesscamerapgnmobile.domain.game.MoveOutcome
import io.github.alluhemanth.chess.core.piece.PieceColor
import io.github.alluhemanth.chess.core.piece.PieceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualBoardScreen(onBack: () -> Unit) {
    var session by remember { mutableStateOf(ChessSession()) }
    var revision by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var pendingPromotion by remember { mutableStateOf<Pair<String, String>?>(null) }
    var status by remember { mutableStateOf("Select a white piece, then its destination.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Play on Virtual Board") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            revision
            Text("${session.sideToMove().name.lowercase().replaceFirstChar { it.uppercase() }} to move", style = MaterialTheme.typography.titleLarge)
            Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
            MoveRow(session.moves())
            KnightboardChessBoard(
                session = session,
                selected = selected,
                enabled = !session.isOver(),
                lastMove = session.moves().lastOrNull(),
                onSquarePressed = { square ->
                    val source = selected
                    if (source == null) {
                        if (session.pieceAt(square)?.isWhite == (session.sideToMove() == PieceColor.WHITE)) {
                            selected = square
                            status = "$square selected. Choose a highlighted destination."
                        } else status = "Choose a ${session.sideToMove().name.lowercase()} piece."
                    } else if (source == square) {
                        selected = null
                        status = "Selection cleared."
                    } else {
                        when (val outcome = session.attemptMove(source, square)) {
                            is MoveOutcome.Applied -> {
                                revision++
                                selected = null
                                status = if (session.isOver()) session.resultLabel() else "Move ${outcome.uci} confirmed."
                            }
                            is MoveOutcome.PromotionRequired -> pendingPromotion = source to square
                            is MoveOutcome.Rejected -> {
                                selected = null
                                status = outcome.message
                            }
                        }
                    }
                },
            )
            Text(
                "Full rules engine: legal moves, check, checkmate, stalemate, castling, en passant, promotions, repetition, 50-move draws, FEN, and PGN.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = {
                    session = ChessSession()
                    revision++
                    selected = null
                    status = "Board reset. White to move."
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Reset board") }
        }
    }
    pendingPromotion?.let { (from, to) ->
        PromotionPicker(onChoice = { piece ->
            when (val outcome = session.attemptMove(from, to, piece)) {
                is MoveOutcome.Applied -> {
                    revision++
                    status = "Promotion ${outcome.uci} confirmed."
                }
                is MoveOutcome.Rejected -> status = outcome.message
                is MoveOutcome.PromotionRequired -> Unit
            }
            selected = null
            pendingPromotion = null
        })
    }
}

@Composable
fun MoveRow(moves: List<String>) {
    if (moves.isNotEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(moves) { move -> Text(move, style = MaterialTheme.typography.labelLarge) }
        }
    }
}
