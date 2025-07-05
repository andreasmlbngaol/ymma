package org.andreasmlbngaol.ymma.theme

import androidx.compose.runtime.Composable

//@Composable
//actual fun YmmaTheme(
//    darkTheme: Boolean,
//    content: @Composable (() -> Unit)
//) {
//    val context = LocalContext.current
//
//    val colorScheme = when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            if (darkTheme) {
//                dynamicDarkColorScheme(context)
//            }
//            else dynamicLightColorScheme(context)
//        }
//        darkTheme -> darkColorScheme()
//        else -> lightColorScheme()
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        content = content
//    )
//}

@Composable
actual fun YmmaTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    NonAndroidYmmaTheme { content() }
}