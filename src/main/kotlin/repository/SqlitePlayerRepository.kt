package repository

import model.Player

class SqlitePlayerRepository(private val dbHelper: DBHelper) : PlayerRepo {

    init {
        dbHelper.initTables()
    }

    override fun save(player: Player) {
        dbHelper.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO players (id, name, rating, wins, losses, draws) VALUES (?, ?, ?, ?, ?, ?)"
            )
            stmt.setInt(1, player.id)
            stmt.setString(2, player.name)
            stmt.setInt(3, player.rating)
            stmt.setInt(4, player.wins)
            stmt.setInt(5, player.losses)
            stmt.setInt(6, player.draws)
            stmt.executeUpdate()
        }
    }

    override fun findAll(): List<Player> {
        val players = mutableListOf<Player>()
        dbHelper.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT * FROM players")
            while (rs.next()) {
                players.add(
                    Player(
                        id = rs.getInt("id"),
                        name = rs.getString("name"),
                        rating = rs.getInt("rating"),
                        wins = rs.getInt("wins"),
                        losses = rs.getInt("losses"),
                        draws = rs.getInt("draws")
                    )
                )
            }
        }
        return players
    }

    override fun findById(id: Int): Player? {
        dbHelper.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM players WHERE id = ?")
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return Player(
                    id = rs.getInt("id"),
                    name = rs.getString("name"),
                    rating = rs.getInt("rating"),
                    wins = rs.getInt("wins"),
                    losses = rs.getInt("losses"),
                    draws = rs.getInt("draws")
                )
            }
        }
        return null
    }

    override fun update(player: Player) {
        dbHelper.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "UPDATE players SET name = ?, rating = ?, wins = ?, losses = ?, draws = ? WHERE id = ?"
            )
            stmt.setString(1, player.name)
            stmt.setInt(2, player.rating)
            stmt.setInt(3, player.wins)
            stmt.setInt(4, player.losses)
            stmt.setInt(5, player.draws)
            stmt.setInt(6, player.id)
            stmt.executeUpdate()
        }
    }

    override fun nextId(): Int {
        dbHelper.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT MAX(id) as max_id FROM players")
            if (rs.next()) {
                val maxId = rs.getInt("max_id")
                return if (rs.wasNull()) 1 else maxId + 1
            }
        }
        return 1
    }
}
