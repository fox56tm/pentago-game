package repository

import model.Game

interface GameRepository {
    fun save(game: Game)

    fun findById(id: String): Game?
}
