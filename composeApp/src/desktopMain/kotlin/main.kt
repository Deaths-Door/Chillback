import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.deathsdoor.chillback.data.initializeFirebase
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import uk.co.caprica.vlcj.binding.lib.LibVlc
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil
import java.io.File


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