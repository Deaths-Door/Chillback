package com.deathsdoor.chillback.ui.extensions

import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.applyIf(condition : Boolean,transform : @DisallowComposableCalls Modifier.() -> Modifier)
= if(condition) transform() else this

fun Modifier.fadedRightEdge(edgeWidth : Dp = 12.dp) = drawWithContent {
    drawContent()
    drawFadedEdge(edgeWidth = edgeWidth,leftEdge = false)
}

fun Modifier.fadedEdges(edgeWidth : Dp = 12.dp) = drawWithContent {
    drawContent()
    drawFadedEdge(edgeWidth = edgeWidth,leftEdge = true)
    drawFadedEdge(edgeWidth = edgeWidth,leftEdge = false)
}

fun ContentDrawScope.drawFadedEdge(edgeWidth : Dp,leftEdge: Boolean) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent,Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}