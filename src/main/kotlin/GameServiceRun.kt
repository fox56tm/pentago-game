package main

import model.Game
import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.DefaultRules
import service.GameService

class GameServiceRun {
    private val scanner = java.util.Scanner(System.`in`)

    fun firstColor(): String {
        println("Who goes first? (W/B):")
        var firstColor = scanner.nextLine().uppercase().trim()
        while (firstColor != "W" && firstColor != "B") {
            println("Invalid color! Enter W or B:")
            firstColor = scanner.nextLine().uppercase().trim()
        }
        return firstColor
    }

    fun chooseColor(player1: String): String {
        println("$player1 plays which color? (W/B):")
        var color1 = scanner.nextLine().uppercase().trim()
        while (color1 != "W" && color1 != "B") {
            println("Invalid color! Enter W or B:")
            color1 = scanner.nextLine().uppercase().trim()
        }
        return color1
    }

    fun run() {
        val service = GameService(InMemoryGameRepository(), DefaultRules())
        val (p1, p2, color1) = setupPlayers()

        val game = service.createGame("game-1", p1, p2).apply {
            currTurnColor = firstColor()
            println("\n=== Game started! First move: $currTurnColor ===")
            println("Actions format: x y quadrant rotation")
        }

        while (game.status == "ACTIVE") {
            val move = readMove(game, p1, p2, color1) ?: continue
            service.makeMove("game-1", move)
            println("Correct action")
            println("Now action: ${game.currTurnColor}")
        }

        println("\n=== Game was ended ===")
        println("Status: ${game.status}")
        println("Total actions count: ${game.moves.size}")
    }

    private fun setupPlayers(): Triple<Player, Player, String> {
        println("Player 1 name:")
        val name1 = scanner.nextLine().trim()
        println("Player 2 name:")
        val name2 = scanner.nextLine().trim()

        val c1 = chooseColor(name1)
        val c2 = if (c1 == "W") "B" else "W"
        println("$name1 = $c1, $name2 = $c2")
        return Triple(Player(1, name1), Player(2, name2), c1)
    }

    private fun readMove(game: Game, p1: Player, p2: Player, color1: String): Move? {
        val parts = scanner.nextLine().trim().split(" ")
        if (parts.size != 4) {
            println("Wrong format! Action format: x y quadrant rotation")
            return null
        }

        val x = parts[0].toIntOrNull()
        val y = parts[1].toIntOrNull()
        val q = parts[2].toIntOrNull()
        if (x == null || y == null || q == null) {
            println("Wrong format! x, y, quadrant must be numbers")
            return null
        }

        val player = if (game.currTurnColor == color1) p1 else p2
        return Move(player, x, y, q, parts[3].uppercase(), game.currTurnColor)
    }
}
