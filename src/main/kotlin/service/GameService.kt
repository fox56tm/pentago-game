package service

import model.*
import repository.GameRepository

class GameService (
    private val repository: GameRepository,
    private val ruleEngine: GameRuleEngine
){
    fun createGame(gameId: String, p1: Player, p2: Player): Game {
        val game = Game(id = gameId, players = listOf(p1, p2), status = "ACTIVE")
        repository.save(game)
        return game
    }
    fun makeMove(gameId: String, move: Move): Boolean{
        val game = repository.findById(gameId) ?: throw IllegalArgumentException("Game with id $gameId not found\n")
        if (ruleEngine.validateMove(game, move)) {
            game.placeMove(move)
            repository.save(game)
            return true
        } else {
            return false
        }
    }

}
