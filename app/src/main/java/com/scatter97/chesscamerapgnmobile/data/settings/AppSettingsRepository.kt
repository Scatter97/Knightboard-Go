package com.scatter97.chesscamerapgnmobile.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "chess_camera_settings")

enum class DetectionMode {
    NORMAL,
    FAST,
    BULLET,
}

data class AppSettings(
    val local64V2Enabled: Boolean = true,
    val showDiagnostics: Boolean = true,
    val detectionMode: DetectionMode = DetectionMode.NORMAL,
)

class AppSettingsRepository(private val context: Context) {
    private object Keys {
        val Local64V2 = booleanPreferencesKey("local64_v2_enabled")
        val Diagnostics = booleanPreferencesKey("show_diagnostics")
        val DetectionMode = stringPreferencesKey("detection_mode")
    }

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { preferences ->
        AppSettings(
            local64V2Enabled = preferences[Keys.Local64V2] ?: true,
            showDiagnostics = preferences[Keys.Diagnostics] ?: true,
            detectionMode = preferences[Keys.DetectionMode]
                ?.let { stored -> DetectionMode.entries.firstOrNull { it.name == stored } }
                ?: DetectionMode.NORMAL,
        )
    }

    suspend fun setLocal64V2Enabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.Local64V2] = enabled }
    }

    suspend fun setShowDiagnostics(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.Diagnostics] = enabled }
    }

    suspend fun setDetectionMode(mode: DetectionMode) {
        context.settingsDataStore.edit { it[Keys.DetectionMode] = mode.name }
    }
}
