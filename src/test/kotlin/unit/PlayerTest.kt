package unit

import model.Player
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerTest {

    @Test
    fun `returns sum of wins losses and draws`() {
        val player = Player(1, "Test", wins = 5, losses = 3, draws = 2)
        assertEquals(10, player.gamesPlayed)
    }

    @Test
    fun `returns zero when no games`() {
        val player = Player(1, "Test")
        assertEquals(0, player.gamesPlayed)
    }

    @Test
    fun `player created with default values`() {
        val player = Player(1, "Test")
        assertEquals(1000, player.rating)
        assertEquals(0, player.wins)
        assertEquals(0, player.losses)
        assertEquals(0, player.draws)
    }
}
