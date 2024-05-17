import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.deathsdoor.chillback.core.layout.InitializeProviders
import com.deathsdoor.chillback.feature.welcome.WelcomeScreen
import com.deathsdoor.chillback.ui.ChillbackApplication
import com.deathsdoor.chillback.ui.ChillbackSplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() = InitializeProviders {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ChillbackSplashScreen {
                // TODO : use this for mobile
                val applicationNavController = rememberNavController()

                ChillbackApplication(applicationNavController = applicationNavController) {
                    // TODO : Add App Content Here
                }
            }
        }
    }
}