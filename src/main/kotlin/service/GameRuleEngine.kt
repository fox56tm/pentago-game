package service

import model.Game
import model.Move

interface GameRuleEngine {
    fun validateMove(game: Game, move: Move): Boolean
    fun applyRotation(game: Game, quadrant: Int, direction: String)
    fun checkWinner(game: Game): String?
}
