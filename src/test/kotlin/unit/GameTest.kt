package unit

import model.Game
import model.Move
import model.Player
import kotlin.test.Test
import kotlin.test.assertEquals

class GameTest {
    private val testPlayer1 = Player(1, "test1")
    private val testPlayer2 = Player(2, "test2")

    private fun createGame(): Game {
        return Game(id = "test-game", players = listOf(testPlayer1, testPlayer2), status = "ACTIVE")
    }

    @Test
    fun `returns correct value in getCell`() {
        val game = createGame()
        game.board[0][0] = 1
        assertEquals(1, game.getCell(0, 0))
        assertEquals(0, game.getCell(1, 1))
    }

    @Test
    fun `out of board range in getCell`() {
        val game = createGame()
        assertEquals(-1, game.getCell(9, 9))
    }

    @Test
    fun `change and give color correctly`() {
        val game = createGame()
        assertEquals("W", game.giveCurrentColor())
        game.placeMove(Move(testPlayer1, 0, 0, 0, "R", "W"))
        game.nextMoveColor()
        assertEquals("B", game.giveCurrentColor())
    }
}
