package gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    movesCount: Int) {
    Text("%02d:%02d".format(seconds / 60, (seconds % 60)), fontSize = 22.sp)
    Text("${player1.name} = $color1 || ${player2.name} = $color2", fontSize = 12.sp, color = Color.Gray)
    Text("Moves: $movesCount", fontSize = 12.sp)
}

@Composable
fun GameOverSection(
    game: Game,
    color1: String,
) {

}