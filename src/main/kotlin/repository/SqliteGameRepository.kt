package repository

import model.Game
import java.time.LocalDateTime

class SqliteGameRepository(private val dbHelper: DBHelper) : GameRepository {

    init {
        dbHelper.initTables()
    }

    override fun save(game: Game) {
        dbHelper.getConnection().use { conn ->
            val winnerId = when {
                game.status == "DRAW" -> null
                game.status == "FINISHED" -> {
                    //winner id
                    if (game.currTurnColor == "W") game.players[0].id else game.players[1].id
                }
                else -> null
            }
            val stmt = conn.prepareStatement(
                "INSERT OR REPLACE INTO games (id, player1_id, player2_id, winner_id, status, created_at) VALUES (?, ?, ?, ?, ?, ?)"
            )
            stmt.setString(1, game.id)
            stmt.setInt(2, game.players[0].id)
            stmt.setInt(3, game.players[1].id)
            if (winnerId != null) {
                stmt.setInt(4, winnerId)
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER)
            }
            stmt.setString(5, game.status)
            stmt.setString(6, LocalDateTime.now().toString())
            stmt.executeUpdate()
        }
    }

    override fun findById(id: String): Game? {
        return null
    }

    fun getGameHistory(): List<GameRecord> {
        val history = mutableListOf<GameRecord>()
        dbHelper.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery(
                "SELECT * FROM games ORDER BY created_at DESC LIMIT 50"
            )
            while (rs.next()) {
                history.add(
                    GameRecord(
                        gameId = rs.getString("id"),
                        player1Id = rs.getInt("player1_id"),
                        player2Id = rs.getInt("player2_id"),
                        winnerId = rs.getInt("winner_id").takeIf { !rs.wasNull() },
                        status = rs.getString("status"),
                        createdAt = rs.getString("created_at")
                    )
                )
            }
        }
        return history
    }
}

data class GameRecord(
    val gameId: String,
    val player1Id: Int,
    val player2Id: Int,
    val winnerId: Int?,
    val status: String,
    val createdAt: String
)
