import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.deathsdoor.chillback.core.layout.InitializeProviders

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        InitializeProviders {
            MaterialTheme(colorScheme = darkColorScheme()) {
                com.deathsdoor.chillback.welcome.Homepage()
            }
        }
    }
}