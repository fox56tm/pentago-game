package service

import model.Game
import model.Move
import model.Player
import repository.GameRepository

class
GameService(
    private val repository: GameRepository,
    private val ruleEngine: DefaultRules
) {
    fun createGame(
        gameId: String,
        p1: Player,
        p2: Player
    ): Game {
        val game = Game(id = gameId, players = listOf(p1, p2), status = "ACTIVE")
        repository.save(game)
        return game
    }

    fun makeMove(
        gameId: String,
        move: Move
    ): Boolean {
        val game = repository.findById(gameId) ?: throw IllegalArgumentException("Game with id $gameId not found")
        if (!ruleEngine.validateMove(game, move)) return false
        game.placeMove(move)
        ruleEngine.applyRotation(game, move.quadrant, move.rotation)
        val winner = ruleEngine.checkWinner(game)
        if (winner != null) {
            game.status = if (winner == "DRAW") "DRAW" else "FINISHED"
            game.winnerColor = if (winner == "DRAW") null else winner
        }
        repository.save(game)
        return true
    }

    fun getGame(gameId: String): Game? {
        return repository.findById(gameId)
    }
}
