package unit

import model.Game
import model.Move
import model.Player
import service.GameRuleEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameRuleEngineTest {

    private val ruleEngine = GameRuleEngine()
    private val testName1 = Player(1, "test1")
    private val testName2 = Player(2, "test2")

    private fun createActiveGame(): Game {
        return Game(id = "test-game", players = listOf(testName1, testName2), status = "ACTIVE")
    }

    @Test
    fun `correct move`() {
        val game = createActiveGame()
        val move = Move(testName1, 0, 0, 0, "R", "W")
        assertTrue(ruleEngine.validateMove(game, move))
    }

    @Test
    fun `invalid actions in validateMove`() {
        val game = createActiveGame()
        game.board[0][0] = 1
        game.status = "FINISHED"
        assertFalse(ruleEngine.validateMove(game, Move(testName1, 1, 0, 0, "R", "W")))
        game.status = "ACTIVE"
        assertFalse(ruleEngine.validateMove(game, Move(testName1, 0, 0, 0, "R", "W")))
        assertFalse(ruleEngine.validateMove(game, Move(testName1, 9, 9, 0, "R", "W")))
        assertFalse(ruleEngine.validateMove(game, Move(testName1, 1, 0, 5, "R", "W")))
        assertFalse(ruleEngine.validateMove(game, Move(testName1, 1, 0, 0, "X", "W")))
    }

    @Test
    fun `correct rotate in applyRotation`() {
        val game = createActiveGame()
        game.board[0][0] = 1; game.board[0][1] = 2; game.board[0][2] = 3
        game.board[1][0] = 4; game.board[1][1] = 5; game.board[1][2] = 6
        game.board[2][0] = 7; game.board[2][1] = 8; game.board[2][2] = 9

        ruleEngine.applyRotation(game, 0, "R")
        assertEquals(7, game.board[0][0])
        assertEquals(1, game.board[0][2])
        ruleEngine.applyRotation(game, 0, "L")
        assertEquals(1, game.board[0][0])
        assertEquals(3, game.board[0][2])
    }

    @Test
    fun `all win conditions in checkWinner`() {
        var game = createActiveGame()
        for (c in 0..4) game.board[0][c] = 1
        assertEquals("W", ruleEngine.checkWinner(game))

        game = createActiveGame()
        for (r in 0..4) game.board[r][0] = 2
        assertEquals("B", ruleEngine.checkWinner(game))

        game = createActiveGame()
        for (i in 0..4) game.board[i][i] = 1
        assertEquals("W", ruleEngine.checkWinner(game))

        game = createActiveGame()
        for (c in 0..4) game.board[0][c] = 1
        for (c in 0..4) game.board[1][c] = 2
        assertEquals("DRAW", ruleEngine.checkWinner(game))

        game = createActiveGame()
        assertEquals(null, ruleEngine.checkWinner(game))
    }
}
