package model

data class Game(
    val id: String,
    val players: List<Player>,
    val moves: MutableList<Move> = mutableListOf(),
    var status: String = "NEW",
    val board: MutableList<MutableList<Int>> = MutableList(6) { MutableList(6) { 0 } },
    var currTurnColor: String = "W",
    var winnerColor: String? = null
) {
    fun getCell(
        x: Int,
        y: Int
    ): Int {
        if (x !in 0..5 || y !in 0..5) return -1
        return board[y][x]
    }

    fun placeMove(move: Move) {
        board[move.y][move.x] = if (move.color == "W") 1 else 2
        moves.add(move)
    }

    fun giveCurrentColor(): String = currTurnColor

    fun nextMoveColor() {
        currTurnColor = if (currTurnColor == "W") "B" else "W"
    }
}
