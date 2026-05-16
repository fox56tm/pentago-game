package model

data class Move(
    val player: Player,
    val x: Int,
    val y: Int,
    val quadrant: Int,
    val rotation: String // "L" , "R"
)
