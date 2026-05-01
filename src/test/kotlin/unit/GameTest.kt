package unit

import model.Game
import model.Move
import model.Player
import kotlin.test.Test
import kotlin.test.assertEquals

class GameTest {

    private val testName1 = Player(1, "test1")
    private val testName2 = Player(2, "test2")

    private fun createGame(): Game {
        return Game(id = "test-game", players = listOf(testName1, testName2), status = "ACTIVE")
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
    fun `change color correctly in getCurrentColor`() {
        val game = createGame()
        assertEquals("W", game.getCurrentColor())
        game.placeMove(Move(testName1, 0, 0, 0, "R", "W"))
        assertEquals("B", game.getCurrentColor())
    }
}
