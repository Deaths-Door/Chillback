package com.deathsdoor.chillback.data.extensions

import com.deathsdoor.chillback.components.snackbar.StackableSnackbarState
import com.deathsdoor.chillback.data.database.Track
import com.deathsdoor.chillback.data.music.AstroMediaItem
import com.deathsdoor.chillback.data.repositories.MusicRepository


suspend fun Collection<Track>.asMediaItemsOrReport(
    musicRepository : MusicRepository,
    snackbarState: StackableSnackbarState,
) : List<AstroMediaItem> {
    val sizeBefore = this.size
    val output = mapNotNull {
        it.asAstroMediaItem(musicRepository)
    }

    if(sizeBefore != output.size) snackbarState.showErrorSnackbar(title = "Failed to read Metadata for ${sizeBefore - output.size} mediaitems",)

    return output
}
