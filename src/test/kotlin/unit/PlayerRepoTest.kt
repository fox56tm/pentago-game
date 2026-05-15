package unit

import model.Player
import repository.FilePlayerRepo
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PlayerRepositoryTest {
    private val testFile = "test_players.csv"
    private lateinit var repo: FilePlayerRepo

    @BeforeTest
    fun setup() {
        File(testFile).delete()
        repo = FilePlayerRepo(testFile)
    }

    @AfterTest
    fun cleanup() {
        File(testFile).delete()
    }

    @Test
    fun `save and findAll returns saved players`() {
        val player1 = Player(1, "Alice")
        val player2 = Player(2, "Bob")
        repo.save(player1)
        repo.save(player2)

        val players = repo.findAll()
        assertEquals(2, players.size)
        assertEquals("Alice", players[0].name)
        assertEquals("Bob", players[1].name)
    }

    @Test
    fun `findById returns correct player`() {
        val player = Player(1, "Alice")
        repo.save(player)

        val found = repo.findById(1)
        assertNotNull(found)
        assertEquals("Alice", found.name)
    }

    @Test
    fun `findById returns null for non-existent id`() {
        val found = repo.findById(999)
        assertNull(found)
    }

    @Test
    fun `update modifies existing player`() {
        val player = Player(1, "Alice", wins = 0)
        repo.save(player)

        player.wins = 5
        player.rating = 1100
        repo.update(player)

        val updated = repo.findById(1)
        assertNotNull(updated)
        assertEquals(5, updated.wins)
        assertEquals(1100, updated.rating)
    }

    @Test
    fun `nextId returns correct next id`() {
        assertEquals(1, repo.nextId())
        repo.save(Player(1, "Alice"))
        assertEquals(2, repo.nextId())
        repo.save(Player(2, "Bob"))
        assertEquals(3, repo.nextId())
    }

    @Test
    fun `gamesPlayed calculated correctly`() {
        val player = Player(1, "Alice", wins = 3, losses = 2, draws = 1)
        assertEquals(6, player.gamesPlayed)
    }
}
