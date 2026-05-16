package model

data class Game (
    val id: String,
    val players: List<Player>,
    val moves: MutableList<Move> = mutableListOf(),
    var status: String = "NEW"
)
