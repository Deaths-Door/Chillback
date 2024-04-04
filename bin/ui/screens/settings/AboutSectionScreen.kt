\package com.deathsdoor.chillback.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.ui.components.layout.BackgroundLayout
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundImage
import com.deathsdoor.chillback.ui.components.settings.SettingsCollapsingToolbar
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.launch

@Composable
fun AboutSectionScreen(navController: NavController) = SettingsCollapsingToolbar(
    navController = navController,
    text = SettingScreenRoutes.About.title,
    body = {
        val data = data()
        LazyColumn {
            item { DeveloperAccount() }

            items(data) {
                val uriHandler = LocalUriHandler.current

                val (title,_temp) = it
                val (description,uri) = _temp

                SettingsMenuLink(
                    title = { Text(text = title) },
                    subtitle = { description?.let { d -> Text(text = d) } },
                    onClick = {
                        uri.let {
                            // if string open link else execute the function
                            s -> if(s is String) uriHandler.openUri("$REPO_URL/$s") else (s as () -> Unit)()
                        }
                    }
                )
            }

            item {
                SettingsMenuLink(
                    title = { Text(text = "Version")},
                    subtitle = { Text(text = "") },
                    onClick = { }
                )
            }
        }
    }
)

private const val USER_NAME = "Deaths-Door"
private const val GITHUB_USER_NAME_URL = "https://github.com/$USER_NAME"
private const val REPO_URL = "$GITHUB_USER_NAME_URL/Chillback"
private fun String.fileFromRepo() = "tree/main/$this"
private const val ISSUES ="issues"

@Composable
private fun DeveloperAccount() = BackgroundLayout(
    background = {
        Box(
            modifier = Modifier.padding(bottom = 64.dp).height(120.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
        )
    },
    content = {
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                CircularBackgroundImage(
                    modifier = Modifier.size(128.dp),
                    model = "$GITHUB_USER_NAME_URL.png",
                    contentDescription = null,
                    background = MaterialTheme.colorScheme.primary
                )

                Column {
                    Text(
                        text = "Aarav Shah",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    val uriHandler = LocalUriHandler.current

                    ClickableText(
                        text = AnnotatedString(USER_NAME),
                        style = MaterialTheme.typography.bodyMedium,
                        onClick = { uriHandler.openUri(GITHUB_USER_NAME_URL) }
                    )
                }
            }
        )
    }
)

@Composable
private fun data(): List<Pair<String, Pair<String?, Any?>>> {
    val context = LocalContext.current
    val packageName = context.packageName
    val version = context.packageManager.getPackageInfo(packageName,0).versionName
    val errorSnackbar = LocalErrorSnackbarState.current

    val coroutineScope = rememberCoroutineScope()

    return listOf(
        "Github Repository" to ("Provides a link to the GitHub repository." to ""),
        "Term and conditions" to ("Provides links to the terms of service" to "TERM_CONDITIONS.md".fileFromRepo()),
        "Privacy Policy" to ("Provides links to the privacy policy" to "PRIVACY_POLICY.md".fileFromRepo()),
        "Rate the app" to (null to {
            val manager = ReviewManagerFactory.create(context)
            val flow = manager.requestReviewFlow()

            flow.addOnCompleteListener { request ->
                if(!request.isSuccessful) {
                    coroutineScope.launch {
                        errorSnackbar.showSnackbar("Rating App Failed")
                    }
                    return@addOnCompleteListener
                }

                val reviewInfo: ReviewInfo = request.result
                manager.launchReviewFlow(context as Activity, reviewInfo)
            }
        }),
        "Feedback" to ("Provides links to feedback forms" to ISSUES),
        "Report bug" to ("Provides links to bug reporting pages." to ISSUES),
        "Share App" to (null to {
            ShareCompat.IntentBuilder(context)
                .setType("text/plain")
                .setChooserTitle("Check out my app")
                .setText("Please check out my app on Google Play!\n Link: http://play.google.com/store/apps/details?id=$packageName")
                .startChooser()
        }),
        "Open source license" to ("Provides a link to the open source license for the app" to "LICENSE".fileFromRepo()),
        "Version" to (version to null)
    )
}
