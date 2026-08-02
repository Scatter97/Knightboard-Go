package com.scatter97.chesscamerapgnmobile.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scatter97.chesscamerapgnmobile.domain.game.ChessSession
import com.scatter97.chesscamerapgnmobile.domain.game.MoveOutcome
import com.scatter97.chesscamerapgnmobile.domain.game.StockfishBot
import io.github.alluhemanth.chess.core.piece.PieceColor
import io.github.alluhemanth.chess.core.piece.PieceType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotGameScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var session by remember { mutableStateOf(ChessSession()) }
    var revision by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var pendingPromotion by remember { mutableStateOf<Pair<String, String>?>(null) }
    var botThinking by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("You are White. Stockfish plays Black.") }

    fun requestStockfishMove() {
        if (session.isOver()) {
            status = session.resultLabel()
            return
        }
        botThinking = true
        status = "Stockfish is thinking…"
        scope.launch {
            runCatching { StockfishBot.chooseMove(session.fen()) }
                .onSuccess { move ->
                    if (session.applyUci(move)) {
                        revision++
                        status = if (session.isOver()) session.resultLabel() else "Stockfish played $move. Your move."
                    } else status = "Stockfish returned an invalid move. Try a new game."
                }
                .onFailure { error -> status = "Stockfish could not start: ${error.message ?: "unknown error"}" }
            botThinking = false
        }
    }

    fun submitPlayerMove(from: String, to: String, promotion: PieceType? = null) {
        when (val outcome = session.attemptMove(from, to, promotion)) {
            is MoveOutcome.Applied -> {
                revision++
                selected = null
                if (session.isOver()) status = session.resultLabel() else requestStockfishMove()
            }
            is MoveOutcome.PromotionRequired -> pendingPromotion = from to to
            is MoveOutcome.Rejected -> {
                selected = null
                status = outcome.message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bot Game") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            revision
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(if (botThinking) "Stockfish • thinking" else "You • White", style = MaterialTheme.typography.titleMedium)
                    Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            KnightboardChessBoard(
                session = session,
                selected = selected,
                enabled = !botThinking && !session.isOver() && session.sideToMove() == PieceColor.WHITE,
                onSquarePressed = { square ->
                    val source = selected
                    if (source == null) {
                        if (session.pieceAt(square)?.isWhite == true) {
                            selected = square
                            status = "$square selected. Choose a highlighted destination."
                        } else status = "You are playing White."
                    } else if (source == square) {
                        selected = null
                        status = "Selection cleared."
                    } else submitPlayerMove(source, square)
                },
            )
            Text(
                "Stockfish Lite runs on your phone. The first bot version uses depth 8 so it remains responsive on mobile.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            MoveRow(session.moves())
            Button(
                onClick = {
                    session = ChessSession()
                    revision++
                    selected = null
                    botThinking = false
                    status = "New game. You are White."
                },
                enabled = !botThinking,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("New game") }
        }
    }
    pendingPromotion?.let { (from, to) ->
        PromotionPicker { piece ->
            pendingPromotion = null
            submitPlayerMove(from, to, piece)
        }
    }
}

@Composable
private fun PromotionPicker(onChoice: (PieceType) -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Choose promotion") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT).forEach { piece ->
                    TextButton(onClick = { onChoice(piece) }, modifier = Modifier.fillMaxWidth()) {
                        Text(piece.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }
        },
        confirmButton = {},
    )
}
