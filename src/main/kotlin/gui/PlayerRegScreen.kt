package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Player
import repository.PlayerRepo

@Composable
fun PlayerRegistryScreen(
    playerRepo: PlayerRepo,
    onStartGame: (Player, Player, String, String) -> Unit
) {
    var players by remember { mutableStateOf(playerRepo.findAll()) }
    var selected by remember { mutableStateOf<List<Player>>(emptyList()) }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        RegistryHeader()
        PlayerList(
            players = players,
            selected = selected,
            onPlayerClick = { player ->
                val isSelected = selected.any { it.id == player.id }
                selected = if (isSelected) {
                    selected.filter { it.id != player.id }
                } else if (selected.size < 2) {
                    selected + player
                } else {
                    selected
                }
            },
            modifier = Modifier.weight(1f)
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        AddPlayerSection(
            players = players,
            onAddPlayer = { newName ->
                playerRepo.save(Player(playerRepo.nextId(), newName))
                players = playerRepo.findAll()
            }
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        GameSetupSection(
            selected = selected,
            onStartGame = onStartGame
        )
    }
}

@Composable
fun RegistryHeader() {
    Text("Player Registration", fontSize = 22.sp, modifier = Modifier.padding(bottom = 4.dp))
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 13.dp)) {
        Text("Name", Modifier.weight(2f), color = Color.Gray, fontSize = 13.sp)
        Text("Games", Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp)
        Text("W", Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp)
        Text("L", Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp)
        Text("D", Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp)
        Text("Rating", Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp)
    }
    Divider()
}

@Composable
fun PlayerList(
    players: List<Player>,
    selected: List<Player>,
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(players) { player ->
            val isSelected = selected.any { it.id == player.id }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable { onPlayerClick(player) }
                    .background(if (isSelected) Color.Blue else Color.Transparent)
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(player.name, Modifier.weight(2f))
                Text("${player.gamesPlayed}", Modifier.weight(1f))
                Text("${player.wins}", Modifier.weight(1f), color = Color.Green)
                Text("${player.losses}", Modifier.weight(1f), color = Color.Red)
                Text("${player.draws}", Modifier.weight(1f), color = Color.Gray)
                Text("${player.rating}", Modifier.weight(1f), color = Color.Gray)
            }
            Divider(color = Color.Gray)
        }
    }
}

@Composable
fun AddPlayerSection(
    players: List<Player>,
    onAddPlayer: (String) -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { showAdd = !showAdd }.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Add Player", fontSize = 16.sp)
        Icon(
            imageVector = if (showAdd) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (showAdd) "Close" else "Open"
        )
    }
    if (showAdd) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
            Button(
                onClick = {
                    val trimmedName = newName.trim()
                    when {
                        trimmedName.isBlank() -> error = "Enter a name"
                        players.any { trimmedName == it.name } -> error = "Name is already exists"
                        else -> { onAddPlayer(trimmedName); newName = ""; error = "" }
                    }
                }
            ) { Text("Add") }
        }
        if (error.isNotEmpty()) Text(error, color = Color.Red, fontSize = 12.sp)
    }
}

@Composable
fun GameSetupSection(
    selected: List<Player>,
    onStartGame: (Player, Player, String, String) -> Unit
) {
    var color1 by remember { mutableStateOf("W") }
    var firstColor by remember { mutableStateOf("W") }
    if (selected.size == 2) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${selected[0].name} plays:", fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = color1 == "W", onClick = { color1 = "W" })
                    Text("White", Modifier.padding(end = 12.dp))
                    RadioButton(selected = color1 == "B", onClick = { color1 = "B" })
                    Text("Black")
                }
            }
            Column {
                Text("First move:", fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = firstColor == "W", onClick = { firstColor = "W" })
                    Text("White", Modifier.padding(end = 12.dp))
                    RadioButton(selected = firstColor == "B", onClick = { firstColor = "B" })
                    Text("Black")
                }
            }
            Button(
                onClick = { onStartGame(selected[0], selected[1], color1, firstColor) },
                modifier = Modifier.width(140.dp)
            ) { Text("Start game") }
        }
    } else {
        Text(
            text = if (selected.isEmpty()) "Select 2 players for play" else "Select one more player",
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}
