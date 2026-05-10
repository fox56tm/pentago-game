import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
fun main() = application {
    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth()
    ) {
        Text(text = "Hello,")
        Text(text = "allo")
    }
}
