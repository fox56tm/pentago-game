package service

import model.Game
import model.Move
import repository.GameRepository

class GameService (
    private val repository: GameRepository,
    private val ruleEngine: GameRuleEngine
){
    fun makeMove(gameId: String, move: Move){
        val game = repository.findById(gameId) ?: throw IllegalArgumentException("Game with id $gameId not found\n")
        if (ruleEngine.validateMove(game, move)){

            //реализую позже логику записи шара

            game.placeMove(move)
            repository.save(game)
        }else {
            throw IllegalArgumentException("Uncorrect action!\n")
        }
    }
}
