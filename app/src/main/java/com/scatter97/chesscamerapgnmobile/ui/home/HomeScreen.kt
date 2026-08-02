package com.scatter97.chesscamerapgnmobile.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class FeatureItem(
    val title: String,
    val description: String,
    val action: FeatureAction? = null,
)

private enum class FeatureAction { BotGame, VirtualBoard }

private val features = listOf(
    FeatureItem(
        title = "Record OTB Game",
        description = "Temporarily disabled while the mobile camera move-detection system is rebuilt.",
    ),
    FeatureItem(
        title = "Virtual Board",
        description = "Play a complete local game with full rule validation.",
        action = FeatureAction.VirtualBoard,
    ),
    FeatureItem(
        title = "Bot Game",
        description = "Play as White against on-device Stockfish.",
        action = FeatureAction.BotGame,
    ),
    FeatureItem(
        title = "64-Square Detection V2",
        description = "The Android detection engine is the next major implementation phase.",
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onOpenVirtualBoard: () -> Unit,
    onOpenBotGame: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Knightboard Go") })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text(
                    text = "Your chessboard, connected.",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = "Play against Stockfish or use the virtual board while camera move detection is being rebuilt.",
                    modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            items(features) { feature ->
                FeatureCard(
                    feature = feature,
                    onStartGame = onStartGame,
                    onOpenVirtualBoard = onOpenVirtualBoard,
                    onOpenBotGame = onOpenBotGame,
                )
            }

            item {
                Button(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                ) {
                    Text("Settings")
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    feature: FeatureItem,
    onStartGame: () -> Unit,
    onOpenVirtualBoard: () -> Unit,
    onOpenBotGame: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(feature.title, style = MaterialTheme.typography.titleLarge)
            Text(
                feature.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (feature.action != null) {
                Button(
                    onClick = when (feature.action) {
                        FeatureAction.BotGame -> onOpenBotGame
                        FeatureAction.VirtualBoard -> onOpenVirtualBoard
                        null -> onStartGame
                    },
                ) {
                    Text(if (feature.action == FeatureAction.BotGame) "Play Stockfish" else "Open board")
                }
            } else {
                Text(
                    text = "Temporarily disabled",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
