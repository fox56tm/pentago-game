package repository

import model.Player
import java.io.File

class FilePlayerRepo(private val filePath: String = "players.csv") : PlayerRepo {

    private fun readAll(): MutableList<Player> {
        val file = File(filePath)
        if (!file.exists()) return mutableListOf()
        return file.readLines()
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                runCatching {
                    val parts = line.split(",")
                    Player(
                        id = parts[0].toInt(),
                        name = parts[1],
                        rating = parts[2].toInt(),
                        wins = parts[3].toInt(),
                        losses = parts[4].toInt(),
                        draws = parts[5].toInt()
                    )
                }.getOrNull()
            }.toMutableList()
    }

    private fun writeAll(players: List<Player>) {
        File(filePath).writeText(
            players.joinToString("\n") {
                "${it.id},${it.name},${it.rating},${it.wins},${it.losses},${it.draws}"
            }
        )
    }

    override fun save(player: Player) {
        val players = readAll()
        players.add(player)
        writeAll(players)
    }

    override fun findAll(): List<Player> = readAll()

    override fun findById(id: Int): Player? = readAll().find { it.id == id }

    override fun update(player: Player) {
        val players = readAll()
        val index = players.indexOfFirst { it.id == player.id }
        if (index >= 0) {
            players[index] = player
            writeAll(players)
        }
    }

    override fun nextId(): Int {
        val players = readAll()
        return if (players.isEmpty()) 1 else players.maxOf { it.id } + 1
    }
}
