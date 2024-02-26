package com.deathsdoor.chillback.ui.components.layout

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.components.track.Artwork

/**
 * This is copied and modified from this [source](https://github.com/matsumo0922/Kanade/blob/master/core/ui/src/main/java/caios/android/kanade/core/ui/music/MultiArtwork.kt#L28)
 **/
@Composable
fun MultipleRotatedArtworks(
    modifier : Modifier = Modifier,
    artworks : List<Uri>,
    space: Dp = 8.dp,
    content : @Composable BoxScope.() -> Unit
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .clipToBounds(),
    content = {
        @Suppress("NAME_SHADOWING")
        val artworks = artworks.fillIfRequired()

        // TODO : Optimize it so that only items visible are 'rendered' by images
        LazyVerticalGrid(
            modifier = Modifier
                .matchParentSize()
                .extraSize(0.3f, 0.3f)
                .rotate(30f),
            columns = GridCells.Fixed(3),
            userScrollEnabled = false,
            content = {
                val shape = RoundedCornerShape(space * 1.5f)

                val cardModifier = Modifier
                    .padding(space / 2)
                    .aspectRatio(1f)
                val contentModifier = Modifier.fillMaxSize(

                )
                items(artworks) {
                    Card(
                        modifier = cardModifier,
                        shape = shape,
                        content = { Artwork(modifier = contentModifier, uri = it) }
                    )
                }
            }
        )

        content()
    }
)

private fun Modifier.extraSize(widthPercent: Float, heightPercent: Float) = layout { measurable, constraints ->
    val width = constraints.maxWidth * widthPercent
    val height = constraints.maxHeight * heightPercent

    val placeable = measurable.measure(
        constraints.copy(
            maxWidth = (constraints.maxWidth + width * 2).toInt(),
            maxHeight = (constraints.maxHeight + height * 2).toInt(),
        ),
    )

    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

@Composable
@ReadOnlyComposable
private fun drawableToUri(@DrawableRes drawable : Int) = Uri.parse("android.resource://${LocalContext.current.applicationContext.packageName}/$drawable")

private const val MAX_LENGTH = 9

@Composable
@ReadOnlyComposable
private fun List<Uri>.fillIfRequired(): List<Uri> {
    val minLength = minOf(size, MAX_LENGTH)

    if(minLength == MAX_LENGTH) return this

    return buildList {
        addAll(subList(0,minLength).toList())
        val uri = drawableToUri(R.drawable.ic_launcher_background)
        repeat(MAX_LENGTH - minLength) { add(uri) }
    }
}