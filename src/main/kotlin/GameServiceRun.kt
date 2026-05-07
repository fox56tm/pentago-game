package main

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
        val repository = InMemoryGameRepository()
        val ruleEngine = DefaultRules()
        val service = GameService(repository, ruleEngine)

        println("Player 1 name:")
        val name1 = scanner.nextLine().trim()
        println("Player 2 name:")
        val name2 = scanner.nextLine().trim()

        val p1 = Player(1, name1)
        val p2 = Player(2, name2)
        val color1 = chooseColor(name1)
        val color2 = if (color1 == "W") "B" else "W"
        println("$name1 = $color1, $name2 = $color2")

        service.createGame("game-1", p1, p2)
        val game = service.getGame("game-1")!!
        game.currTurnColor = firstColor()
        game.currTurnColor = if (game.currTurnColor == "B") "B" else "W"
        println("\n=== Game started! First move: ${game.currTurnColor} ===")
        println("Actions format: x y quadrant rotation")

        while (game.status == "ACTIVE") {
            val input = scanner.nextLine().trim().split(" ")

            if (input.size != 4) {
                println("Wrong format! Action format: x y quadrant rotation")
                continue
            }

            val x = input[0].toIntOrNull()
            val y = input[1].toIntOrNull()
            val quadrant = input[2].toIntOrNull()
            val rotation = input[3].uppercase()
            val currentPlayer = if (game.currTurnColor == color1) p1 else p2
            if (x == null || y == null || quadrant == null) {
                println("Wrong format! x, y, quadrant must be a numbers")
                continue
            }
            val move =
                Move(
                    player = currentPlayer,
                    x = x,
                    y = y,
                    quadrant = quadrant,
                    rotation = rotation,
                    color = game.currTurnColor
                )
            val success = service.makeMove("game-1", move)
            if (success) println("Correct action")
            println("Now action: ${game.currTurnColor}")
        }
        println("\n=== Game was ended ===")
        println("Status: ${game.status}")
        println("Total actions count: ${game.moves.size}")
    }
}
