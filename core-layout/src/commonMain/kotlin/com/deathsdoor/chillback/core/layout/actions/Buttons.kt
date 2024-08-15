package com.deathsdoor.chillback.core.layout.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import com.deathsdoor.chillback.core.resources.Res

@Composable
@NonRestartableComposable
fun BackButton(modifier: Modifier = Modifier,onClick : () -> Unit) = FloatingActionButton(
    modifier = modifier.padding(start = 12.dp,top = 12.dp),
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
        )
    }
)


@Composable
@NonRestartableComposable
fun DownButton(modifier: Modifier = Modifier, onClick : () -> Unit) = FloatingActionButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            modifier = Modifier.rotate(-90F),
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null
        )
    }
)

@Composable
@NonRestartableComposable
fun ForwardButton(modifier: Modifier = Modifier,onClick : () -> Unit) = FloatingActionButton(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    onClick = onClick,
    content = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null
        )
    }
)

@Composable
fun MoreInfoButton(
    modifier: Modifier = Modifier,
    content : @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { expanded = true },
            content = {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(Res.strings.more_options)
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = content
        )
    }
}