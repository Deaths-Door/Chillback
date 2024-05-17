package com.deathsdoor.chillback.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.AdaptiveMobileLayout
import com.deathsdoor.chillback.core.layout.LocalWindowSize

@Composable
internal fun GenericWelcome(
    modifier : Modifier,
    title : String,
    description : AnnotatedString,
    currentPosition : Int,
    imageVector: ImageVector,
    outerGradient : Painter,
    innerGradient : Painter,
    content : (@Composable BoxScope.() -> Unit)? = null,
) = AdaptiveMobileLayout(
    onPortrait = {
        Row(modifier) {
            GradientCircles(
                imageVector = imageVector,
                outerGradient = outerGradient,
                innerGradient = innerGradient,
            )

            Box(modifier = Modifier.fillMaxSize().padding(start = 24.dp)) {
                TitleText(
                    modifier = Modifier.padding(bottom = 32.dp),
                    text = title,
                )

                TitleDescription(text = description)

                content?.invoke(this)
            }
        }
    },
    onLandscape = {
        Column(modifier) {
            GradientCircles(
                imageVector = imageVector,
                outerGradient = outerGradient,
                innerGradient = innerGradient,
                content = content
            )

            val textStartPadding = 24.dp
            val textEndPadding = 12.dp

            TitleText(
                modifier = Modifier.padding(
                    start = textStartPadding,
                    end = textEndPadding,
                    top = 24.dp,
                    bottom = 32.dp
                ),
                text = title,
            )

            TitleDescription(
                modifier = Modifier.padding(start = textStartPadding,end = textEndPadding),
                text = description
            )

            Spacer(modifier = Modifier.weight(1f))

            val circleModifier = Modifier.clip(shape = CircleShape)
                .size(16.dp)

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    (0..2).forEach {
                        val circleColor = if(currentPosition == it) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.inverseSurface
                        Box(modifier = circleModifier.background(shape = CircleShape,color = circleColor))
                    }
                }
            )
        }
    }
)

@Composable
@NonRestartableComposable
private inline fun TitleText(modifier: Modifier,text : String)
    = Text(modifier = modifier,text = text,style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)

@Composable
@NonRestartableComposable
private inline fun TitleDescription(modifier: Modifier = Modifier,text: AnnotatedString)
        = Text(modifier = modifier,text = text,style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)


@Composable
private fun GradientCircles(
    imageVector: ImageVector,
    innerGradient : Painter,
    outerGradient : Painter,
    content : (@Composable BoxScope.() -> Unit)? = null
) {
    Box {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = outerGradient,
            contentDescription = null
        )

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

        content?.invoke(this)
    }
}