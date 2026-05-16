package gui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import model.Player
import repository.DBHelper
import repository.InMemoryGameRepository
import repository.SqliteGameRepository
import repository.SqlitePlayerRepository
import service.DefaultRules
import service.GameService

@Composable
fun App() {
    val dbHelper = remember { DBHelper() }
    val playerRepo = remember { SqlitePlayerRepository(dbHelper) }
    val gameHistoryRepo = remember { SqliteGameRepository(dbHelper) }
    val activeGameRepo = remember { InMemoryGameRepository() }
    val gameService = remember { GameService(activeGameRepo, DefaultRules()) }

    var currentScreen by remember { mutableStateOf("registry") }
    var gameId by remember { mutableStateOf("") }
    var player1 by remember { mutableStateOf<Player?>(null) }
    var player2 by remember { mutableStateOf<Player?>(null) }
    var color1 by remember { mutableStateOf("W") }

    when (currentScreen) {
        "registry" -> PlayerRegistryScreen(
            playerRepo = playerRepo,
            onStartGame = { p1, p2, c1, firstColor ->
                player1 = p1
                player2 = p2
                color1 = c1
                gameId = "game-${System.currentTimeMillis()}"
                gameService.createGame(gameId, p1, p2)
                gameService.getGame(gameId)!!.currTurnColor = firstColor
                currentScreen = "game"
            }
        )
        "game" -> GameScreen(
            service = gameService,
            gameId = gameId,
            player1 = player1!!,
            player2 = player2!!,
            color1 = color1,
            playerRepo = playerRepo,
            onBack = {
                //save game
                val game = gameService.getGame(gameId)
                if (game != null && game.status != "ACTIVE") {
                    gameHistoryRepo.save(game)
                }
                currentScreen = "registry"
            }
        )
    }
}
