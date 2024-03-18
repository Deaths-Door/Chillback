package com.deathsdoor.chillback.ui.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.ui.components.layout.Thumbnail

@Composable
@NonRestartableComposable
fun ShareThumbItem(onClick : () -> Unit = { /*TODO : Share current mediaItem*/ }) {
    val label = "Share"
    Thumbnail(
        modifier = Modifier.clickable(onClickLabel = label) {
            onClick()
        }.optionsItemSpacing(),
        title = label,
        artwork = {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Song"
            )
        }
    )
}
@Composable
fun Share(modifier: Modifier = Modifier,onClick : () -> Unit = { /*TODO : Share current mediaItem*/ }) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Song"
            )
        }
    )
}