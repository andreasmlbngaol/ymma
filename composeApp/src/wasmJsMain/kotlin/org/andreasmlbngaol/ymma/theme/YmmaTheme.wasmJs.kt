package org.andreasmlbngaol.ymma.theme

import androidx.compose.runtime.Composable

@Composable
actual fun YmmaTheme(
    darkTheme: Boolean,
    content: @Composable (() -> Unit)
) {
    NonAndroidYmmaTheme { content() }
}