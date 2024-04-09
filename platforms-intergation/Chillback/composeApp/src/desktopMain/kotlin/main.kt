import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.deathsdoor.chillback.resources.Resources
import com.deathsdoor.chillback.ui.providers.App
import com.deathsdoor.chillback.ui.shortcut.ShortCutMenu
import dev.icerock.moko.resources.compose.stringResource
import java.awt.Dimension
import java.awt.Toolkit

fun main() = application {
    val mainWindowState = rememberWindowState(size = preferredWindowSize())
    var isMainWindowClosed by remember { mutableStateOf(true) }

    val applicationName = stringResource(Resources.strings.app_name)

    // TODO : ADD app icon
    Window(
        state = mainWindowState,
        icon = object : Painter() {
            override val intrinsicSize = Size(256f, 256f)

            override fun DrawScope.onDraw() {
                drawOval(Color(0xFFFFA500))
            }
        },
        onCloseRequest = { isMainWindowClosed = false },
        visible = isMainWindowClosed,
        title = applicationName,
        content = {
            App()
        }

    )
    if(isMainWindowClosed) return@application

    Tray(
        tooltip = applicationName,
        onAction = { isMainWindowClosed = true },
        icon = object : Painter() {
            override val intrinsicSize = Size(256f, 256f)

            override fun DrawScope.onDraw() {
                drawOval(Color(0xFFFFA500))
            }
        },
        menu = {
            // TODO : Tooltips for desktop + keyboard bindings
           ShortCutMenu(::exitApplication)
        },
    )
}

private const val desired = 800
private fun preferredWindowSize(): DpSize {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
    val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
    val width: Int = if (desired < preferredWidth) desired else preferredWidth
    val height: Int = if (desired < preferredHeight) desired else preferredHeight
    return DpSize(width.dp, height.dp)
}