package repository

import java.sql.Connection
import java.sql.DriverManager

class DBHelper(private val dbPath: String = "pentago.db") {

    fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:$dbPath")
    }

    fun initTables() {
        getConnection().use { conn ->
            conn.createStatement().execute(
                """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL UNIQUE,
                    rating INTEGER DEFAULT 1000,
                    wins INTEGER DEFAULT 0,
                    losses INTEGER DEFAULT 0,
                    draws INTEGER DEFAULT 0
                )
                """.trimIndent()
            )

            conn.createStatement().execute(
                """
                CREATE TABLE IF NOT EXISTS games (
                    id TEXT PRIMARY KEY,
                    player1_id INTEGER,
                    player2_id INTEGER,
                    winner_id INTEGER,
                    status TEXT,
                    created_at TEXT,
                    FOREIGN KEY (player1_id) REFERENCES players(id),
                    FOREIGN KEY (player2_id) REFERENCES players(id),
                    FOREIGN KEY (winner_id) REFERENCES players(id)
                )
                """.trimIndent()
            )
        }
    }

    fun clearAllTables() {
        getConnection().use { conn ->
            conn.createStatement().execute("DELETE FROM games")
            conn.createStatement().execute("DELETE FROM players")
        }
    }
}
