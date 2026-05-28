package integration

import model.Player
import repository.DBHelper
import repository.SqliteGameRepository
import repository.SqlitePlayerRepository
import service.DefaultRules
import service.GameService
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SqliteGameHistoryTest {
    private val testDb = "test_history.db"
    private lateinit var dbHelper: DBHelper
    private lateinit var playerRepo: SqlitePlayerRepository
    private lateinit var gameRepo: SqliteGameRepository

    @BeforeTest
    fun setup() {
        File(testDb).delete()
        dbHelper = DBHelper(testDb)
        playerRepo = SqlitePlayerRepository(dbHelper)
        gameRepo = SqliteGameRepository(dbHelper)
    }

    @AfterTest
    fun cleanup() {
        File(testDb).delete()
    }

    @Test
    fun `game saved to history`() {
        val p1 = Player(1, "Alice")
        val p2 = Player(2, "Bob")
        playerRepo.save(p1)
        playerRepo.save(p2)

        val service = GameService(gameRepo, DefaultRules())
        val gameId = "test-1"
        service.createGame(gameId, p1, p2)
        val game = service.getGame(gameId)!!
        game.status = "FINISHED"
        gameRepo.save(game)

        val history = gameRepo.getGameHistory()
        assertEquals(1, history.size)
        assertEquals(gameId, history[0].gameId)
    }
}
