package com.scatter97.chesscamerapgnmobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scatter97.chesscamerapgnmobile.ui.calibration.CalibrationScreen
import com.scatter97.chesscamerapgnmobile.ui.camera.CameraScreen
import com.scatter97.chesscamerapgnmobile.ui.home.HomeScreen
import com.scatter97.chesscamerapgnmobile.ui.settings.SettingsScreen

private object Route {
    const val Home = "home"
    const val Camera = "camera"
    const val Calibration = "calibration"
    const val Settings = "settings"
}

@Composable
fun ChessCameraApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable(Route.Home) {
            HomeScreen(
                onStartGame = { navController.navigate(Route.Camera) },
                onOpenSettings = { navController.navigate(Route.Settings) },
            )
        }
        composable(Route.Camera) {
            CameraScreen(
                onBack = { navController.popBackStack() },
                onBeginCalibration = { navController.navigate(Route.Calibration) },
            )
        }
        composable(Route.Calibration) {
            CalibrationScreen(
                onBack = { navController.popBackStack() },
                onCalibrationSaved = {
                    navController.popBackStack(Route.Home, inclusive = false)
                },
            )
        }
        composable(Route.Settings) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
