package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.AdaptiveLayout

@Composable
internal fun GenericWelcome(
    modifier : Modifier = Modifier,
    title : String,
    description : AnnotatedString,
    imageVector: ImageVector,
    outerGradient : Painter,
    innerGradient : Painter,
    content : (@Composable BoxScope.() -> Unit)? = null,
) {
    AdaptiveLayout(
        onMobileLandscape = {
            Row(modifier) {
                GradientCircles(
                    imageVector = imageVector,
                    outerGradient = outerGradient,
                    innerGradient = innerGradient,
                )

                Box(modifier = Modifier.fillMaxSize().padding(start = 24.dp)) {
                    TitleText(text = title)
                    TitleDescription(text = description)

                    content?.invoke(this)
                }
            }
        },
        onMobilePortrait = {
            Column(modifier) {
                GradientCircles(
                    modifier = Modifier.weight(0.5f),
                    imageVector = imageVector,
                    outerGradient = outerGradient,
                    innerGradient = innerGradient,
                    content = content
                )

                Column {
                    TitleText(title)
                    TitleDescription(description)
                }

            }
        },
        onDesktop = {
            Box(modifier) {
                val maxHeightFraction = 0.6f
                GradientCircles(
                    modifier = Modifier.fillMaxHeight(maxHeightFraction),
                    imageVector = imageVector,
                    outerGradient = outerGradient,
                    innerGradient = null,
                    content = content
                )

                Column {
                    Spacer(modifier = Modifier.fillMaxHeight(maxHeightFraction - 0.35f))

                    TitleText(title)

                    Spacer(modifier = Modifier.height(64.dp))

                    TitleDescription(description)
                }
            }
        }
    )
}


private val textStartPadding = 24.dp
private val textEndPadding = 12.dp
@Composable
@NonRestartableComposable
private fun TitleText(text : String) = Text(
    modifier = Modifier.padding(
        start = textStartPadding,
        end = textEndPadding,
        top = 24.dp,
        bottom = 32.dp
    ),
    text = text,style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)

@Composable
@NonRestartableComposable
private fun TitleDescription(text: AnnotatedString)
        = Text(modifier = Modifier.padding(start = textStartPadding,end = textEndPadding),text = text,style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)



@Composable
private fun GradientCircles(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    innerGradient : Painter?,
    outerGradient : Painter,
    content : (@Composable BoxScope.() -> Unit)? = null
) {
    Box(modifier) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = outerGradient,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )

        innerGradient?.let {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Image(
                    painter = innerGradient,
                    contentDescription = null
                )

                Image(
                    modifier = Modifier.matchParentSize(),
                    imageVector = imageVector,
                    contentDescription = null
                )
            }
        }

        content?.invoke(this)
    }
}