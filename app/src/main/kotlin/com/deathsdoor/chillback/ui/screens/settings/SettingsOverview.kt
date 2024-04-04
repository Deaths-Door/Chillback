package com.deathsdoor.chillback.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.layout.BackgroundImage
import com.deathsdoor.chillback.ui.components.layout.CollapsableScaffold
import com.deathsdoor.chillback.ui.components.layout.circular
import com.deathsdoor.chillback.ui.navigation.OtherCategory
import com.deathsdoor.chillback.ui.navigation.WidgetThemeCategory
import com.deathsdoor.chillback.ui.providers.LocalAppState

@Composable
fun SettingsOverviewPortraitMobile() {
    val navController = LocalAppState.current.navController

    CollapsableScaffold(
        label = stringResource(R.string.overview),
        onBack = { navController.popBackStack() },
        floatingActionButton = null,
        headerContent = { modifier ->
            // TODO : Add profile card
        },
        content = {
            // TODO : Add profile card UserProfileCard(modifier = Modifier.padding(vertical = 16.dp),navController = navController)
            Row {
                WidgetThemeCategory { route, icon, label, description ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .padding(end = 12.dp),
                        onClick = { navController.navigate(route) },
                        content = {
                            BackgroundImage(
                                // TODO : Create background image dafur
                                painter = painterResource(R.drawable.add_to_queue),
                                content = {
                                    Text(
                                        modifier = Modifier.padding(start = 8.dp,top = 16.dp),
                                        text = stringResource(id = label),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            )
                        }
                    )
                }
            }

            OtherCategory { route, icon, label, description ->
                SettingsMenuLink(
                    icon = {
                        Icon(
                            modifier = Modifier.circular(),
                            painter = rememberVectorPainter(icon),
                            contentDescription = null,
                        )
                    },
                    title = {
                        val style = MaterialTheme.typography.titleMedium

                        Text(
                            text = stringResource(id = label),
                            fontSize = style.fontSize * 1.2,
                            fontWeight = FontWeight.Bold,
                            style = style
                        )
                    },
                    subtitle = {
                        Text(
                            text = stringResource(id = description),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = { navController.navigate(route) },
                )
            }
        }
    )
}