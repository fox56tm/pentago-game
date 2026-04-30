package service

import model.Game
import model.Move
import model.Player

class GameRuleEngine {

    fun validateMove(game: Game, move: Move): Boolean {
        if (game.status != "ACTIVE"){
            println("game was ended!\n")
            return false
        }
        if (move.x !in 0..5 || move.y !in 0..5) {
            println("Out of game board range!\n")
            return false
        }
        if(game.getCell(move.x, move.y) != 0) {
            println("This place is not empty!\n")
            return false
        }

        return true
    }
}