package repository

import java.io.File
import model.Player
class FilePlayerRepo(private val filePath: String = "players_stat.csv"): PlayerRepo {

    private fun readAll(): MutableList<Player> {
        val file = File(filePath)
        if(!file.exists()) return mutableListOf()
        return file.readLines().filter { it.isNotBlank() }
            .mapNotNull { line ->
                runCatching { val p = line.split(",")
                    Player(
                        id = p[0].toInt(),
                        name = p[1],
                        rating = p[2].toInt(),
                        wins = p[3].toInt(),
                        losses = p[4].toInt(),
                        draws = p[5].toInt()
                    )
                }.getOrNull()
            }.toMutableList()
    }
    private fun writeAll(players: List<Player>){
        File(filePath).writeText(players.joinToString("\n"){
            "${it.id} , ${it.name} , ${it.rating} , ${it.wins}, ${it.losses} , ${it.draws}"
            }
        )
    }

    override fun save(player: Player) {
        val players = readAll()
        players.add(player)
        writeAll(players)
    }

    override fun findAll(): List<Player> = readAll()

    override fun findById(id: Int): Player? {
        val players = readAll()
        return players.find { it.id == id }
    }

    override fun update(player: Player) {
        val players = readAll()
        val ind = players.indexOfFirst { it.id == player.id }
        if (ind >= 0) {
            players[ind] = player
            writeAll(players)
        }
    }

    override fun nextId(): Int {
        val players = readAll()
        return if(players.isEmpty()) 1 else players.maxOf { it.id } + 1
    }
}