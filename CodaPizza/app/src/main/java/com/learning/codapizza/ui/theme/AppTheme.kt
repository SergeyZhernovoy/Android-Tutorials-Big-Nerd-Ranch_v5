package com.learning.codapizza.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) = MaterialTheme(
    typography = Typography,
    colorScheme = lightColorScheme(
        primary = Color(0xFFB72A33),
        onPrimary = Color(0xFFA6262E),
        secondary = Color(0xFF03C4DD),
        onSecondary = Color(0xFF03B2C9)
    )
) {
    content()
}