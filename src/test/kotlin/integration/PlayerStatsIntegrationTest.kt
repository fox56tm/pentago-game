package integration

import model.Player
import repository.FilePlayerRepo
import repository.InMemoryGameRepository
import service.DefaultRules
import service.GameService
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerStatsIntegrationTest {
    private val testFile = "test_stats_players.csv"
    private lateinit var playerRepo: FilePlayerRepo
    private lateinit var gameService: GameService

    @BeforeTest
    fun setup() {
        File(testFile).delete()
        playerRepo = FilePlayerRepo(testFile)
        gameService = GameService(InMemoryGameRepository(), DefaultRules())
    }

    @AfterTest
    fun cleanup() {
        File(testFile).delete()
    }

    @Test
    fun `player stats update after game completion`() {
        val player1 = Player(1, "Alice")
        val player2 = Player(2, "Bob")
        playerRepo.save(player1)
        playerRepo.save(player2)

        player1.wins++
        player1.rating += 20
        player2.losses++
        player2.rating -= 20

        playerRepo.update(player1)
        playerRepo.update(player2)

        val updated1 = playerRepo.findById(1)!!
        val updated2 = playerRepo.findById(2)!!

        assertEquals(1, updated1.wins)
        assertEquals(1020, updated1.rating)
        assertEquals(1, updated2.losses)
        assertEquals(980, updated2.rating)
    }

    @Test
    fun `player stats update after draw`() {
        val player1 = Player(1, "Alice")
        val player2 = Player(2, "Bob")
        playerRepo.save(player1)
        playerRepo.save(player2)

        player1.draws++
        player2.draws++

        playerRepo.update(player1)
        playerRepo.update(player2)

        val updated1 = playerRepo.findById(1)!!
        val updated2 = playerRepo.findById(2)!!

        assertEquals(1, updated1.draws)
        assertEquals(1, updated2.draws)
        assertEquals(1000, updated1.rating)
        assertEquals(1000, updated2.rating)
    }

    @Test
    fun `multiple games update stats correctly`() {
        val player = Player(1, "Alice")
        playerRepo.save(player)

        player.wins++
        player.rating += 20
        playerRepo.update(player)

        player.losses++
        player.rating -= 20
        playerRepo.update(player)

        player.draws++
        playerRepo.update(player)

        val final = playerRepo.findById(1)!!
        assertEquals(1, final.wins)
        assertEquals(1, final.losses)
        assertEquals(1, final.draws)
        assertEquals(3, final.gamesPlayed)
        assertEquals(1000, final.rating)
    }
}
