package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.deathsdoor.chillback.ui.components.action.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsableScaffold(
    modifier : Modifier = Modifier,
    label : String,
    onBack : () -> Unit,
    floatingActionButton: @Composable AnimatedVisibilityScope.() -> Unit,
    headerContent : @Composable (modifier : Modifier) -> Unit,
    content : @Composable (PaddingValues) -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    val isExpanded by remember(topAppBarState.collapsedFraction) {
        derivedStateOf { topAppBarState.collapsedFraction < 0.3f }
    }

    val enterAnimation = fadeIn() + scaleIn()
    val exitAnimation = fadeOut() + scaleOut()

    val floatingActionContent = @Composable { condition : Boolean, innerModifier : Modifier ->
        AnimatedVisibility(
            modifier = innerModifier,
            visible = condition,
            enter = enterAnimation,
            exit = exitAnimation,
            content = floatingActionButton
        )
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { floatingActionContent(!isExpanded, Modifier) },
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackButton(onClick = onBack) },
                title = {
                    if(!isExpanded) Text(modifier = Modifier, text = label)
                }
            )

            if(isExpanded) headerContent(Modifier.fillMaxHeight(0.35f))
        },
        content = content
    )
}