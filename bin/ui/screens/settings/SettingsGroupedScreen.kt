package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.ui.components.layout.CircularBackgroundIcon
import com.deathsdoor.chillback.ui.components.profile.UserProfileCard
import com.deathsdoor.chillback.ui.components.settings.SettingsCollapsingToolbar

@Composable
fun SettingsGroupedScreen(navController: NavController) = SettingsCollapsingToolbar(
    navController = navController,
    text = SettingScreenRoutes.SettingGrouped.title,
    body = {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            content = {
                UserProfileCard(modifier = Modifier.padding(vertical = 16.dp),navController = navController)

                ThemeWidgetSection(navController = navController)

                // Removes SettingsGrouped , Theme / Widget selector
                SettingScreenRoutes.values().drop(3).forEach {
                    SettingsMenuLink(
                        icon = {
                            CircularBackgroundIcon(
                                painter = rememberVectorPainter(Icons.Default.Edit),
                                contentDescription = null,
                            )
                        },
                        title = {
                            with(MaterialTheme.typography.titleMedium) {
                                Text(
                                    text = it.title,
                                    fontSize = fontSize * 1.2,
                                    fontWeight = FontWeight.Bold,
                                    style = this
                                )
                            }
                        },
                        subtitle = {
                           Text(
                               text = it.description,
                               style = MaterialTheme.typography.labelLarge
                           )
                        },
                        onClick = { navController.navigate(it.route) },
                    )
                }
            }
        )
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeWidgetSection(navController: NavController) = Row {
    listOf(
        "Themes" to SettingScreenRoutes.ThemeSelector,
        "Widgets" to SettingScreenRoutes.WidgetSelector
    ).forEach { (text, selector) ->
        Card(
            modifier = Modifier.weight(1f).height(80.dp).padding(end = 12.dp),
            onClick = { navController.navigate(selector.route) },
            content = {
                BackgroundImage(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    content = {
                        Text(
                            modifier = Modifier.padding(start = 8.dp,top = 16.dp),
                            text = text,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        )
    }
}