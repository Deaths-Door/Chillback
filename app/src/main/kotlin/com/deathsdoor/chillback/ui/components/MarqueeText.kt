package com.deathsdoor.chillback.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current
) = Text(
        text = text,
        color =  color,
        fontSize = fontSize,
        fontStyle =  fontStyle,
        fontWeight = fontWeight,
        fontFamily =  fontFamily,
        letterSpacing = letterSpacing,
        textDecoration =  textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap =  softWrap,
        maxLines =  maxLines,
        minLines = minLines,
        style =  style,
        modifier = modifier
            .widthIn(max = edgeWidth * 4)
            // Rendering to an offscreen buffer is required to get the faded edges' alpha to be
            // applied only to the text, and not whatever is drawn below this composable (e.g. the
            // window).
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                drawFadedEdge(leftEdge = true)
                drawFadedEdge(leftEdge = false)
            }
            .basicMarquee(
                // Animate forever.
                iterations = Int.MAX_VALUE,
            )
            .padding(start = edgeWidth)
    )


private val edgeWidth = 32.dp
fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}