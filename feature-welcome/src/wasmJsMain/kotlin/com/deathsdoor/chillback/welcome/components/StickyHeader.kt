package com.deathsdoor.chillback.welcome.components

import Chillback.`feature-welcome`.myiconpack.SupportUs
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.coming_soon
import chillback.`feature-welcome`.generated.resources.support_us
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import com.deathsdoor.chillback.core.layout.extensions.PROJECT_HOME_PAGE
import com.deathsdoor.chillback.welcome.components.icons.Github
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun StickyHeader(modifier: Modifier) = Row(modifier = modifier.padding(vertical = 12.dp),verticalAlignment = Alignment.CenterVertically) {
    val icon =  Modifier.size(32.dp)

    Icon(
        modifier = icon.padding(end = 12.dp),
        // TODO : Replace with application logo
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null
    )

    Text(
        text = "Chillback",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.weight(1f))


    val windowSize = LocalWindowSize.current
    val uriHandler = LocalUriHandler.current

    // TODO : Add theme + Language Selector
    when(windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
        true -> Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart),
            content = {
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = !expanded }) {
                    val angle by animateFloatAsState(targetValue = if(expanded) 360F else 0F)

                    Icon(
                        modifier = Modifier.rotate(angle),
                        imageVector = if(expanded) Icons.Default.ArrowDropDown else Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    content = {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(Res.string.support_us)) },
                            onClick = {
                                // TODO : Show snackbar that its coming soon
                                      },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.SupportUs,
                                    contentDescription = null
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Github") },
                            onClick = { uriHandler.openUri(PROJECT_HOME_PAGE) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Github,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
        )
        false -> {
            // This is necessary so that the badge does not overlap with the Github Icon
            val badgeAvoidOverlapModifier = Modifier.padding(end = 32.dp)
            BadgedBox(
                modifier = badgeAvoidOverlapModifier,
                badge = {
                    Badge(modifier = badgeAvoidOverlapModifier) {
                        Text(text = stringResource(Res.string.coming_soon))
                    }
                },
                content = {
                    Text(text = stringResource(Res.string.support_us))
                }
            )

            VerticalDivider()

            IconButton(
                onClick = { uriHandler.openUri(PROJECT_HOME_PAGE) },
                content = {
                    Icon(
                        modifier = icon,
                        imageVector = Icons.Github,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

