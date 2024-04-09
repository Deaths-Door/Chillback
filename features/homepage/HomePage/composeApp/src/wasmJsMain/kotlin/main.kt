import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.deathsdoor.chillback.homepage.ui.Homepage

@OptIn(ExperimentalComposeUiApi::class)
internal fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        MaterialTheme(colorScheme = darkColorScheme()) {
            Homepage()
        }
    }
}