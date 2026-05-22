package unit

import model.Player
import repository.DBHelper
import repository.SqlitePlayerRepository
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SqlitePlayerRepoTest {
    private val testDb = "test_pentago.db"
    private lateinit var dbHelper: DBHelper
    private lateinit var repo: SqlitePlayerRepository

    @BeforeTest
    fun setup() {
        File(testDb).delete()
        dbHelper = DBHelper(testDb)
        repo = SqlitePlayerRepository(dbHelper)
    }

    @AfterTest
    fun cleanup() {
        File(testDb).delete()
    }

    @Test
    fun `save and findAll works`() {
        val player1 = Player(1, "Alice")
        val player2 = Player(2, "Bob")
        repo.save(player1)
        repo.save(player2)

        val players = repo.findAll()
        assertEquals(2, players.size)
    }

    @Test
    fun `findById returns correct player`() {
        val player = Player(1, "Alice", wins = 5)
        repo.save(player)

        val found = repo.findById(1)
        assertNotNull(found)
        assertEquals("Alice", found.name)
        assertEquals(5, found.wins)
    }

    @Test
    fun `update modifies player`() {
        val player = Player(1, "Alice", wins = 0)
        repo.save(player)

        player.wins = 10
        player.rating = 1200
        repo.update(player)

        val updated = repo.findById(1)
        assertNotNull(updated)
        assertEquals(10, updated.wins)
        assertEquals(1200, updated.rating)
    }

    @Test
    fun `nextId increments correctly`() {
        assertEquals(1, repo.nextId())
        repo.save(Player(1, "Alice"))
        assertEquals(2, repo.nextId())
    }
}
