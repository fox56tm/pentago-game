package repository

import model.Player

interface PlayerRepo{
    fun save(player: Player)
    fun findAll(): List<Player>
    fun findById(id: Int): Player?
    fun update(player: Player)
    fun nextId(): Int
}
