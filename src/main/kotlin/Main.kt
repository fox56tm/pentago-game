package main

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pentago",
        state = rememberWindowState(width = 900.dp, height = 700.dp)
    ) {
        App()
    }
}
