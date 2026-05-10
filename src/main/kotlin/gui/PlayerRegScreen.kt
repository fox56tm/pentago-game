package gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var newName by remember { mutableStateOf("") }
    var color1 by remember { mutableStateOf("W") }
    var firstColor by remember { mutableStateOf("W") }
    var showAdd by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column (modifier = Modifier.fillMaxSize().padding(20.dp)) {

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


}

