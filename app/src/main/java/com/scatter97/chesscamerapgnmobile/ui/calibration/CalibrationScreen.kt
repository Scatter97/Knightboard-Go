package com.scatter97.chesscamerapgnmobile.ui.calibration

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.scatter97.chesscamerapgnmobile.domain.calibration.BoardCalibrationState
import com.scatter97.chesscamerapgnmobile.ui.camera.CameraPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalibrationScreen(
    onBack: () -> Unit,
    onCalibrationSaved: () -> Unit,
) {
    val context = LocalContext.current
    val hasCameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED
    var state by remember { mutableStateOf(BoardCalibrationState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Board calibration") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black),
        ) {
            if (hasCameraPermission) {
                CameraPreview(modifier = Modifier.fillMaxSize())
                CalibrationOverlay(
                    state = state,
                    onPointAdded = { state = state.add(it) },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Text(
                    text = "Camera permission is missing. Return to camera setup first.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    color = Color.White,
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.78f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = if (state.isComplete) {
                        "Grid ready. Check that every line follows the board."
                    } else {
                        "Tap corners in order: top-left, top-right, bottom-right, bottom-left (${state.points.size}/4)."
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(
                        onClick = { state = state.undo() },
                        enabled = state.points.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Undo")
                    }
                    OutlinedButton(
                        onClick = { state = state.reset() },
                        enabled = state.points.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Reset")
                    }
                    Button(
                        onClick = onCalibrationSaved,
                        enabled = state.isComplete,
                        modifier = Modifier.weight(1.25f),
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
