package com.scatter97.chesscamerapgnmobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scatter97.chesscamerapgnmobile.ui.calibration.CalibrationScreen
import com.scatter97.chesscamerapgnmobile.ui.camera.CameraScreen
import com.scatter97.chesscamerapgnmobile.ui.home.HomeScreen
import com.scatter97.chesscamerapgnmobile.ui.settings.SettingsScreen
import com.scatter97.chesscamerapgnmobile.ui.game.VirtualBoardScreen
import com.scatter97.chesscamerapgnmobile.ui.game.BotGameScreen

private object Route {
    const val Home = "home"
    const val Camera = "camera"
    const val Calibration = "calibration"
    const val Settings = "settings"
    const val VirtualBoard = "virtual-board"
    const val BotGame = "bot-game"
}

@Composable
fun KnightboardGoApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable(Route.Home) {
            HomeScreen(
                onStartGame = { navController.navigate(Route.Camera) },
                onOpenVirtualBoard = { navController.navigate(Route.VirtualBoard) },
                onOpenBotGame = { navController.navigate(Route.BotGame) },
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
        composable(Route.VirtualBoard) {
            VirtualBoardScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.BotGame) {
            BotGameScreen(onBack = { navController.popBackStack() })
        }
    }
}
