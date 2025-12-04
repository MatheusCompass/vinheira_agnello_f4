package com.example.vinheira_agnello_f4.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MarromLight,
    secondary = DouradoLight,
    tertiary = CinzaSecondary,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2B2520),
    onPrimary = BrancoWhite,
    onSecondary = BrancoWhite,
    onBackground = BegeBackground,
    onSurface = BegeBackground
)

private val LightColorScheme = lightColorScheme(
    primary = MarromPrimary,
    onPrimary = BrancoWhite,
    primaryContainer = BegeBackground,
    onPrimaryContainer = MarromPrimary,
    secondary = DouradoAccent,
    onSecondary = BrancoWhite,
    secondaryContainer = HeaderBackground,
    onSecondaryContainer = DouradoAccent,
    tertiary = CinzaSecondary,
    onTertiary = BrancoWhite,
    tertiaryContainer = HeaderBackground,
    onTertiaryContainer = CinzaSecondary,
    background = BegeBackground,
    onBackground = MarromPrimary,
    surface = BrancoWhite,
    onSurface = MarromPrimary,
    surfaceVariant = HeaderBackground,
    onSurfaceVariant = CinzaSecondary,
    outline = CinzaBorder,
    outlineVariant = CinzaBorder
)

@Composable
fun Vinheira_agnello_f4Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}