package gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Game
import model.Move
import model.Player
import repository.PlayerRepo
import service.GameService

@Composable
fun GameScreen(){

}

@Composable
fun GameSidePanel(
    game: Game,
    player1: Player,
    player2: Player,
    color1: String,
    color2: String,
    seconds: Int,
    isAlive: Boolean,
    onMove: (Int, Int, Int, String) -> Boolean,
    onBack: () -> Unit
){
    Column(modifier = Modifier.padding(24.dp).width(240.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        GameInfoSection(player1, player2, color1,color2,seconds, game.moves.size)
    }
}

@Composable
fun GameInfoSection(
    player1: Player,
    player2: Player,
    color1: String,
    color2: String,
    seconds: Int,
    movesCount: Int
) {
    Text("%02d:%02d".format(seconds / 60, (seconds % 60)), fontSize = 22.sp)
    Text("${player1.name} = $color1 || ${player2.name} = $color2", fontSize = 12.sp, color = Color.Gray)
    Text("Moves: $movesCount", fontSize = 12.sp)
}

@Composable
fun GameOverSection(
    game: Game,
    color1: String,
    player1: Player,
    player2: Player,
    onBack: () -> Unit
) {
    val result = when {
        game.status == "DRAW" -> "DRAW"
        game.currTurnColor == color1 -> "${player1.name} Wins!!!"
        else -> "${player2.name} Wins!!!"
    }
    Text(result,fontSize = 22.sp)
    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
        Text("Back to registry")
    }
}

fun updateStats(game: Game, player1: Player, player2: Player, color1: String, repo:PlayerRepo){
    val p1 = repo.findById(player1.id) ?: player1
    val p2 = repo.findById(player2.id) ?: player2
    when{
        game.status == "DRAW" -> {p1.draws++; p2.draws++}
        game.currTurnColor == color1 -> {p1.wins++; p1.rating+=100; p2.losses++; p2.rating-=100}
        else -> { p2.wins++; p2.rating += 100; p1.losses++; p1.rating -= 100 }
    }
    repo.update(p1)
    repo.update(p2)
}