package system

import model.Move
import model.Player
import repository.InMemoryGameRepository
import service.DefaultRules
import service.GameService
import kotlin.test.Test
import kotlin.test.assertEquals

class FullGameTest {
    private val testPlayer1 = Player(1, "test1")
    private val testPlayer2 = Player(2, "test2")

    private fun createService(): GameService {
        return GameService(InMemoryGameRepository(), DefaultRules())
    }

    @Test
    fun `white wins`() {
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
        assertEquals(9, game.moves.size)
    }

    @Test
    fun `moves after finish will reject`() {
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
        val result = service.makeMove("test-game", Move(testPlayer2, 5, 5, 3, "R", "B"))
        val game = service.getGame("test-game")!!
        assertEquals(false, result)
        assertEquals(9, game.moves.size)
    }
}
