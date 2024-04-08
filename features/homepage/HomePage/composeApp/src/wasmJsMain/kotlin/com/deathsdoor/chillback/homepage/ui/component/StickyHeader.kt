package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.homepage.ui.component.icons.Github
import com.deathsdoor.chillback.homepage.utils.PROJECT_HOME_PAGE

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

    // TODO : Add theme + Language Selector


    // TODO : Fix the badge overlap the the github icon
    // This is necessary so that the badge does not overlap with the Github Icon
    val badgeAvoidOverlapModifier = Modifier.padding(end = 32.dp)
    BadgedBox(
        modifier = badgeAvoidOverlapModifier,
        badge = {
            Badge(modifier = badgeAvoidOverlapModifier) {
                Text(text = "Coming Soon")
            }
        },
        content = {
            Text("Support Us")
        }
    )

    VerticalDivider()

    val uriHandler = LocalUriHandler.current

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