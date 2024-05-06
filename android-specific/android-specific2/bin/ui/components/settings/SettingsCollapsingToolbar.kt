package com.deathsdoor.chillback.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.deathsdoor.chillback.data.navigation.SettingScreenRoutes
import com.deathsdoor.chillback.ui.components.layout.BackButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCollapsingToolbar(
    navController : NavController,
    text : String,
    body : @Composable CollapsingToolbarScaffoldScope.() -> Unit
) =  CollapsingToolbarScaffold(
    modifier = Modifier.padding(16.dp),
    state = rememberCollapsingToolbarScaffoldState(),
    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
    body = body,
    toolbar = {
        TopAppBar(
            navigationIcon = { BackButton { navController.popBackStack() } },
            title = {
                Text(
                    modifier = Modifier.background(Color.Red),
                    text = text,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        )
    }
)