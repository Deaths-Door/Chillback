package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer
import com.deathsdoor.astroplayer.ui.AstroPlayerState

@Composable
internal actual fun RingtoneSelectorAlertDialog(
    applicationPlayer: AstroPlayer,
    isDialogOpen: MutableState<Boolean>,
    mediaItem: AstroMediaItem,
    label : String
) = Unit

@Composable
internal actual fun RingtoneSelectorDropDownItem(
    applicationPlayer: AstroPlayer,
    mediaItem: AstroMediaItem
) = Unit

@Composable
internal actual fun RingtoneSelectorThumbItem(
    applicationPlayer: AstroPlayer,
    mediaItem: AstroMediaItem
) = Unit