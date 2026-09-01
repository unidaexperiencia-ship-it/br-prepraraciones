package com.example.ui.theme

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
    primary = GbPrimaryLight,
    onPrimary = Color.Black,
    primaryContainer = GbPrimaryDark,
    onPrimaryContainer = GbPrimaryContainer,
    secondary = GbLavenderContainer,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4F378B),
    onSecondaryContainer = GbLavenderContainer,
    tertiary = GbTertiaryContainer,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF004977),
    onTertiaryContainer = GbTertiaryContainer,
    background = GbBackgroundDark,
    onBackground = GbTextPrimaryDark,
    surface = GbSurfaceDark,
    onSurface = GbTextPrimaryDark,
    surfaceVariant = GbSurfaceVariantDark,
    onSurfaceVariant = GbTextSecondaryDark,
    outline = GbBorderMuted.copy(alpha = 0.3f),
    outlineVariant = Color(0xFF49454E)
)

private val LightColorScheme = lightColorScheme(
    primary = GbPrimary,
    onPrimary = Color.White,
    primaryContainer = GbPrimaryContainer,
    onPrimaryContainer = GbOnPrimaryContainer,
    secondary = GbLavenderAccent,
    onSecondary = Color.White,
    secondaryContainer = GbLavenderContainer,
    onSecondaryContainer = GbOnLavenderContainer,
    tertiary = GbTertiaryBlue,
    onTertiary = Color.White,
    tertiaryContainer = GbTertiaryContainer,
    onTertiaryContainer = GbOnTertiaryContainer,
    background = GbLightBg,
    onBackground = GbDarkText,
    surface = GbSurfaceLight,
    onSurface = GbDarkText,
    surfaceVariant = GbSurfaceVariant,
    onSurfaceVariant = GbSecondaryText,
    outline = GbBorderMuted,
    outlineVariant = GbBorderMuted.copy(alpha = 0.6f)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Geometric Balance brand identity
    content: @Composable () -> Unit,
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

