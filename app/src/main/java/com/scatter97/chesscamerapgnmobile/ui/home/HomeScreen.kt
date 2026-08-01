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
    val available: Boolean,
)

private val features = listOf(
    FeatureItem(
        title = "Record OTB Game",
        description = "Open the rear camera, align the board, and calibrate its 64 squares.",
        available = true,
    ),
    FeatureItem(
        title = "Virtual Board",
        description = "Play or correct a position manually while the camera detection engine is being built.",
        available = true,
    ),
    FeatureItem(
        title = "64-Square Detection V2",
        description = "The Android detection engine is the next major implementation phase.",
        available = false,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onOpenVirtualBoard: () -> Unit,
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
                    text = "Record over-the-board games with a calibrated camera, or use the virtual board while detection is in development.",
                    modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            items(features) { feature ->
                FeatureCard(
                    feature = feature,
                    onStartGame = onStartGame,
                    onOpenVirtualBoard = onOpenVirtualBoard,
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
            if (feature.available) {
                Button(
                    onClick = when (feature.title) {
                        "Record OTB Game" -> onStartGame
                        else -> onOpenVirtualBoard
                    },
                ) {
                    Text(if (feature.title == "Record OTB Game") "Open camera" else "Open board")
                }
            } else {
                Text(
                    text = "Planned",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
