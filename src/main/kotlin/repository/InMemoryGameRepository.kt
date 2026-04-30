package repository
import model.Game

class InMemoryGameRepository : GameRepository {
    private val storage = mutableMapOf<String, Game>()

    override fun save(game: Game) {
        storage[game.id] = game
    }

    override fun findById(id: String): Game? {
        return storage[id]
    }

}
