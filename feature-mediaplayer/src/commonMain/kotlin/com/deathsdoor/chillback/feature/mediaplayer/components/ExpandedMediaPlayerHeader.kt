package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.core.layout.actions.DownButton
import com.deathsdoor.chillback.feature.mediaplayer.resources.Res
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpandedMediaPlayerHeader(
    onDismiss : () -> Unit,
    onClick : (Int) -> Unit,
) = TopAppBar(
    navigationIcon = { DownButton(onClick = onDismiss) },
    title = {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val content = @Composable { text : StringResource -> Text(text = stringResource(text)) }

            TextButton(
                onClick = { onClick(0) },
                content = { content(Res.strings.playing) }
            )

            TextButton(
                onClick = { onClick(1) },
                content = { content(Res.strings.lyrics) }
            )
        }
    },
    actions = { /*TODO : Show more info about song and this view + show playback queue option */ }
)