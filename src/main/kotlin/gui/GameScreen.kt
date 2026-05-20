package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
) {
    val game = service.getGame(gameId) ?: return
    val color2 = if (color1 == "W") "B" else "W"
    var refresh by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    var statsUpdated by remember { mutableStateOf(false) }
    val board = if (refresh >= 0) game.board else game.board
    val isActive = game.status == "ACTIVE"

    LaunchedEffect(isActive) {
        while (isActive) { delay(1000L.milliseconds); seconds++ }
    }
    LaunchedEffect(game.status) {
        if (!statsUpdated && !isActive) {
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
) {
    Column(
        modifier = Modifier.padding(24.dp).width(240.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameInfoSection(player1, player2, color1, color2, seconds, game.moves.size)
        Divider()
        if (isActive) {
            MoveInput(
                currentPlayerName = if (game.currTurnColor == color1) player1.name else player2.name,
                currentColor = game.currTurnColor,
                onMove = onMove
            )
        } else {
            GameOverSection(game, color1, player1, player2, onBack)
        }
        Divider()
        MoveHistory(game.moves)
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
    Text("${player1.name} = $color1 vs ${player2.name} = $color2", fontSize = 23.sp, color = Color.Gray)
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
    Text(result, fontSize = 22.sp)
    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
        Text("Back to registry")
    }
}

fun updateStats(game: Game, player1: Player, player2: Player, color1: String, repo: PlayerRepo) {
    val p1 = repo.findById(player1.id) ?: player1
    val p2 = repo.findById(player2.id) ?: player2
    when {
        game.status == "DRAW" -> { p1.draws++; p2.draws++ }
        game.currTurnColor == color1 -> { p1.wins++; p1.rating += 100; p2.losses++; p2.rating -= 100 }
        else -> { p2.wins++; p2.rating += 100; p1.losses++; p1.rating -= 100 }
    }
    repo.update(p1)
    repo.update(p2)
}

@Composable
fun MoveInput(
    currentPlayerName: String,
    currentColor: String,
    onMove: (Int, Int, Int, String) -> Boolean
) {
    var x by remember { mutableStateOf("") }
    var y by remember { mutableStateOf("") }
    var quadrant by remember { mutableStateOf("") }
    var rotation by remember { mutableStateOf("R") }
    var message by remember { mutableStateOf("") }
    Text("Turn: $currentPlayerName ($currentColor)", fontSize = 15.sp)
    OutlinedTextField(
        x,
        { x = it },
        label = { Text("X (0-5)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        y,
        { y = it },
        label = { Text("Y (0-5)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        quadrant,
        { quadrant = it },
        label = { Text("Quadrant (0-3)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Rotation: ")
        RadioButton(rotation == "R", { rotation = "R" }); Text("R")
        RadioButton(rotation == "L", { rotation = "L" }); Text("L")
    }
    Button(
        onClick = {
            val xi = x.toIntOrNull()
            val yi = y.toIntOrNull()
            val qi = quadrant.toIntOrNull()
            if (xi == null || yi == null || qi == null) {
                message = "Numbers only"
            } else {
                val success = onMove(xi, yi, qi, rotation)
                message = if (success) "Accepted!" else "Invalid move!"
                if (success) { x = ""; y = ""; quadrant = "" }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Make move") }
    if (message.isNotEmpty()) Text(message, fontSize = 25.sp)
}

@Composable
fun Board(board: MutableList<MutableList<Int>>) {
    Column {
        for (r in 0..5) {
            Row {
                for (c in 0..5) {
                    val cell = board[r][c]
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .padding(4.dp)
                            .border(2.dp, Color.Gray)
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cell != 0) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (cell == 1) Color.White else Color.Black,
                                        CircleShape
                                    )
                                    .border(1.dp, Color.Gray, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoveHistory(moves: List<Move>) {
    var show by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().clickable { show = !show },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("History (${moves.size})", fontSize = 13.sp)
        Icon(
            imageVector = if (show) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (show) "Close" else "Open"
        )
    }
    if (show) {
        LazyColumn(modifier = Modifier.height(120.dp)) {
            items(moves.reversed()) { move ->
                val num = moves.size - moves.reversed().indexOf(move)
                Text(
                    "#$num ${move.color}: (${move.x},${move.y}) Q${move.quadrant}${move.rotation}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
