package main

import main.service.DefaultRules
import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.GameService

class GameServiceRun {

    fun run() {
        val repository = InMemoryGameRepository()
        val ruleEngine = DefaultRules()
        val service = GameService(repository, ruleEngine)
        val scanner = java.util.Scanner(System.`in`)

        println("Player 1 name(white):")
        val name1 = scanner.nextLine()
        println("Player 2 name(black):")
        val name2 = scanner.nextLine()

        val p1 = Player(1, name1)
        val p2 = Player(2, name2)
        service.createGame("game-1", p1, p2)
        println("\n=== Game was started ===")
        println("Actions format: x y quadrant rotation")
        val game = service.getGame("game-1")!!

        while (game.status == "ACTIVE") {
            val currentPlayer = if (game.getCurrentColor() == "W") p1 else p2
            println("Now do action color: ${game.getCurrentColor()}")

            val input = scanner.nextLine().trim().split(" ")

            if (input.size != 4) {
                println("Uncorrect format! Action format: x y quadrant rotation")
                continue
            }

            val x = input[0].toIntOrNull()
            val y = input[1].toIntOrNull()
            val quadrant = input[2].toIntOrNull()
            val rotation = input[3].uppercase()

            if (x == null || y == null || quadrant == null) {
                println("Uncorrect format! x, y, quadrant must be a numbers")
                continue
            }

            val move =
                Move(
                    player = currentPlayer,
                    x = x,
                    y = y,
                    quadrant = quadrant,
                    rotation = rotation,
                    color = game.getCurrentColor()
                )
            val success = service.makeMove("game-1", move)
            if (success) println("Correct action")
        }
        println("\n=== Game was ended ===")
        println("Status: ${game.status}")
        println("Total actions count: ${game.moves.size}")
    }
}
