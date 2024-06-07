import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.deathsdoor.chillback.data.initializeFirebase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

fun main() {
    initializeFirebase()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chillback",
        ) {
            App()
        }
    }
}