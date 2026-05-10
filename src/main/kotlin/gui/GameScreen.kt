package gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.Game
import model.Move
import model.Player
import repository.PlayerRepo
import service.GameService
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameScreen(
    service: GameService,
    gameId: String,
    player1: Player,
    player2: Player,
    color1: String,
    playerRepo: PlayerRepo,
    onBack: () -> Unit
){
    val game = service.getGame(gameId) ?: return
    val color2 = if(color1 == "W") "B" else "W"
    var refresh by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    var statsUpdated by remember { mutableStateOf(false) }
    val board = if (refresh >= 0) game.board else game.board
    val isActive = game.status == "ACTIVE"

    LaunchedEffect(isActive){
        while(isActive){ delay(1000L.milliseconds); seconds++ }

    }
    LaunchedEffect(game.status){
        if(!statsUpdated && !isActive){
            statsUpdated = true
            updateStats(game, player1, player2, color1, playerRepo)
        }
    }
    Row(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Board(board)
        GameSidePanel(
            game = game,
            player1 = player1,
            player2 = player2,
            color1 = color1,
            color2 = color2,
            seconds = seconds,
            isActive = isActive,
            onMove = { x, y, q, r ->
                val player = if (game.currTurnColor == color1) player1 else player2
                val success = service.makeMove(gameId, Move(player, x, y, q, r, game.currTurnColor))
                if (success) refresh++
                success
            },
            onBack = onBack
        )
    }

}

@Composable
fun GameSidePanel(
    game: Game,
    player1: Player,
    player2: Player,
    color1: String,
    color2: String,
    seconds: Int,
    isActive: Boolean,
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

@Composable
fun Board(board: MutableList<MutableList<Int>>){


}
