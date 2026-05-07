package unit

import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.DefaultRules
import service.GameService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class GameServiceTest {
    private val testPlayer1 = Player(1, "test1")
    private val testPlayer2 = Player(2, "test2")

    private fun createService(): GameService {
        return GameService(InMemoryGameRepository(), DefaultRules())
    }

    @Test
    fun `create game`() {
        val service = createService()
        val game = service.createGame("test-game", testPlayer1, testPlayer2)
        assertEquals("test-game", game.id)
        assertEquals("ACTIVE", game.status)
        assertEquals(2, game.players.size)
    }

    @Test
    fun `valid move in makeMove`() {
        val service = createService()
        service.createGame("test-game", testPlayer1, testPlayer2)
        val move = Move(testPlayer1, 0, 0, 0, "R", "W")
        val result = service.makeMove("test-game", move)
        assertTrue(result)
        assertEquals(1, service.getGame("test-game")!!.moves.size)
    }

    @Test
    fun `game status change on FINISHED`() {
        val service = createService()
        service.createGame("test-game", testPlayer1, testPlayer2)
        service.makeMove("test-game", Move(testPlayer1, 0, 0, 3, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 0, 5, 3, "R", "B"))
        service.makeMove("test-game", Move(testPlayer1, 1, 0, 3, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 1, 5, 3, "R", "B"))
        service.makeMove("test-game", Move(testPlayer1, 2, 0, 3, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 2, 5, 3, "R", "B"))
        service.makeMove("test-game", Move(testPlayer1, 3, 0, 3, "R", "W"))
        service.makeMove("test-game", Move(testPlayer2, 3, 5, 3, "R", "B"))
        service.makeMove("test-game", Move(testPlayer1, 4, 0, 3, "R", "W"))
        val game = service.getGame("test-game")!!
        assertEquals("FINISHED", game.status)
    }
}
