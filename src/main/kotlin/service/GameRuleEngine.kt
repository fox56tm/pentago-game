package service

import model.Game
import model.Move
import model.Player

class GameRuleEngine {

    fun validateMove(game: Game, move: Move): Boolean {
        if (game.status != "ACTIVE") {
            println("Game was ended with status: ${game.status}")
            return false
        }

        if (game.players.none { it.id == move.player.id }) {
            println(" Invalid player name: ${move.player.name}")
            return false
        }

        if (move.color != game.getCurrentColor()) {
            println("Now do action: ${game.getCurrentColor()}, not: ${move.color}")
            return false
        }

        if (move.x !in 0..5 || move.y !in 0..5) {
            println("Invalid board places: (${move.x}, ${move.y})")
            return false
        }

        if (game.getCell(move.x, move.y) != 0) {
            println("Place (${move.x}, ${move.y}) is not empty")
            return false
        }

        if (move.quadrant !in 0..3) {
            println("Quadrant must be 0-3, not: ${move.quadrant}")
            return false
        }

        if (move.rotation !in listOf("L", "R")) {
            println("Rotation only 'L' или 'R', not: '${move.rotation}'")
            return false
        }

        return true
    }
}
