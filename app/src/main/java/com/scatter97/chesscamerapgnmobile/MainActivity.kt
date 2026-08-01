package com.scatter97.chesscamerapgnmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.scatter97.chesscamerapgnmobile.ui.ChessCameraApp
import com.scatter97.chesscamerapgnmobile.ui.theme.ChessCameraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessCameraTheme {
                ChessCameraApp()
            }
        }
    }
}
