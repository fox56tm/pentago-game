package model

data class Game (
    val id: String,
    val players: List<Player>,
    val moves: MutableList<Move> = mutableListOf(),
    var status: String = "NEW",
    val board: MutableList<MutableList<Int>> = MutableList(6) { MutableList(6) { 0 } }
){
    fun getCell(x: Int, y: Int): Int{

        if(x !in 0..5 || y !in 0..5) return 0
        return board[y][x]
    }
    fun placeMove(move: Move) {
        board[move.y][move.x] = move.player.id
        moves.add(move)
    }

}
