package model

data class Player(
    val id: Int,
    val name: String,
    var rating: Int = 1000,
    var wins: Int = 0,
    var losses: Int = 0,
    var draws: Int = 0
) {
    val gamesPlayed: Int get() = wins + losses + draws
}
