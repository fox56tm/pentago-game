package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
fun PlayerRegScreen (playerRepo: PlayerRepo, onStartGame: (Player, Player, String, String) -> Unit) {

    var players by remember { mutableStateOf(playerRepo.findAll()) }
    var selected by remember { mutableStateOf<List<Player>>(emptyList()) }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            }
}
@Composable
fun RegistryHeader(){

    Text("Player Registration", fontSize = 22.sp, modifier = Modifier.padding(bottom = 4.dp))
    Row (modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
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
    ){
        LazyColumn(modifier= modifier) {
            items(players){ player ->
                val isSelected = selected.any{it.id == player.id}
                Row(modifier = modifier.fillMaxWidth()
                    .clickable{onPlayerClick(player)}
                    .background(if (isSelected) Color.Blue else Color.Transparent)
                    .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Text(player.name, Modifier.weight(2f))
                    Text("${player.gamesPlayed}", Modifier.weight(1f), color = Color.Green)
                    Text("${player.wins}", Modifier.weight(1f), color = Color.Red)
                    Text("${player.losses}", Modifier.weight(1f), color = Color.Gray)
                    Text("${player.rating}", Modifier.weight(1f))
                }
                Divider(color = Color.Gray)
            }
        }
}

@Composable
fun AddPlayerSection(
    players: List<Player>,
    onAddPlayer: (String) -> Unit
){
    var showAdd by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth()
        .clickable{}
}



