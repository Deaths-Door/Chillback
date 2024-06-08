package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.deathsdoor.astroplayer.core.AstroMediaItem
import com.deathsdoor.astroplayer.core.AstroPlayer

//! Android Only for now
@Composable
internal expect fun RingtoneSelectorThumbItem(applicationPlayer: AstroPlayer,mediaItem: AstroMediaItem)
@Composable
internal expect fun RingtoneSelectorDropDownItem(applicationPlayer: AstroPlayer,mediaItem: AstroMediaItem)
@Composable
internal expect fun RingtoneSelectorAlertDialog(applicationPlayer: AstroPlayer, isDialogOpen : MutableState<Boolean>, mediaItem: AstroMediaItem, label : String)