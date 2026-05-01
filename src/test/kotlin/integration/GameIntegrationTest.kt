package integration

import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.GameRuleEngine
import service.GameService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameIntegrationTest {
    private val testPlayer1 = Player(1, "test1")
    private val testPlayer2 = Player(2, "test2")

    private fun createService(): GameService {
        return GameService(InMemoryGameRepository(), GameRuleEngine())
    }

    @Test
    fun `moves in game`() {
        val service = createService()
        service.createGame("test-game", testPlayer1, testPlayer2)
        service.makeMove("test-game", Move(testPlayer1, 0, 0, 0, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 3, 0, 1, "L", "B"))
        val game = service.getGame("test-game")
        assertNotNull(game)
        assertEquals(2, game.moves.size)
        assertEquals(1, game.board[0][2])
    }

    @Test
    fun `invalid moves do not change game status`() {
        val service = createService()
        service.createGame("test-game", testPlayer1, testPlayer2)
        service.makeMove("test-game", Move(testPlayer1, 0, 0, 0, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 2, 0, 0, "L", "B"))
        val game = service.getGame("test-game")!!
        assertEquals(1, game.moves.size)
        assertEquals("ACTIVE", game.status)
    }

    @Test
    fun `correct board rotate in game`() {
        val service = createService()
        service.createGame("test-game", testPlayer1, testPlayer2)
        service.makeMove("test-game", Move(testPlayer1, 0, 0, 0, "R", "W"))
        val game = service.getGame("test-game")!!
        assertEquals(1, game.board[0][2])
    }
}
