package unit

import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.GameRuleEngine
import service.GameService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class GameServiceTest {
    private val testName1 = Player(1, "test1")
    private val testName2 = Player(2, "test2")

    private fun createService(): GameService {
        return GameService(InMemoryGameRepository(), GameRuleEngine())
    }

    @Test
    fun `create game`() {
        val service = createService()
        val game = service.createGame("test-game", testName1, testName2)
        assertEquals("test-game", game.id)
        assertEquals("ACTIVE", game.status)
        assertEquals(2, game.players.size)
    }

    @Test
    fun `valid move in makeMove`() {
        val service = createService()
        service.createGame("test-game", testName1, testName2)
        val move = Move(testName1, 0, 0, 0, "R", "W")
        val result = service.makeMove("test-game", move)
        assertTrue(result)
        assertEquals(1, service.getGame("test-game")!!.moves.size)
    }
}
