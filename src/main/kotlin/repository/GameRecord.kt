package main.repository

data class GameRecord(
    val gameId: String,
    val player1Id: Int,
    val player2Id: Int,
    val winnerId: Int?,
    val status: String,
    val createdAt: String
)
