package com.deathsdoor.chillback.feature.mediaplayer.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import com.deathsdoor.astroplayer.ui.AstroPlayerState

internal fun LazyListScope.equalizerChip(
    identifier : String,
    state : AstroPlayerState,
    update : () -> Unit
) = item(key = identifier) {
    SuggestionChip(
        enabled = state.isEqualizerEnabled,
        onClick = update,
        label = { Text(identifier) }
    )
}