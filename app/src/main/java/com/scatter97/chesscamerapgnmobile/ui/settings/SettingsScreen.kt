package com.scatter97.chesscamerapgnmobile.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.scatter97.chesscamerapgnmobile.data.settings.AppSettings
import com.scatter97.chesscamerapgnmobile.data.settings.AppSettingsRepository
import com.scatter97.chesscamerapgnmobile.data.settings.DetectionMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current.applicationContext
    val repository = remember(context) { AppSettingsRepository(context) }
    val settings by repository.settings.collectAsState(initial = AppSettings())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Text("Detection foundation", style = MaterialTheme.typography.headlineSmall)

            SettingSwitch(
                title = "64-Square Detection V2",
                description = "Keep the planned 16-zone local detector enabled by default.",
                checked = settings.local64V2Enabled,
                onCheckedChange = { enabled ->
                    scope.launch { repository.setLocal64V2Enabled(enabled) }
                },
            )

            SettingSwitch(
                title = "Camera diagnostics",
                description = "Show zone and performance information once analysis is implemented.",
                checked = settings.showDiagnostics,
                onCheckedChange = { enabled ->
                    scope.launch { repository.setShowDiagnostics(enabled) }
                },
            )

            Text("Detection mode", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DetectionMode.entries.forEach { mode ->
                    Button(
                        onClick = { scope.launch { repository.setDetectionMode(mode) } },
                        enabled = settings.detectionMode != mode,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Text(
                text = "This build does not yet analyze moves. These settings establish the persistent configuration used by the upcoming vision engine.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
