package org.andreasmlbngaol.ymma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "YMMA",
    ) {
        App()
    }
}