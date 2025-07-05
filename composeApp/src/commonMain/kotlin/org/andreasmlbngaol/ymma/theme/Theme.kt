package org.andreasmlbngaol.ymma.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
expect fun YmmaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)

@Composable
fun NonAndroidYmmaTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme())
        darkScheme
    else lightScheme
    MaterialTheme(
        typography = appTypography(),
        colorScheme = colorScheme,
        content = content
    )
}