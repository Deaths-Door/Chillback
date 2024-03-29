package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Deprecated("Do not use this")
@Composable
fun AnimatedDurationMarker(
    mediaController : Player,
    coroutineScope: CoroutineScope,
    showDurationMarker : Boolean = true,
    onPlayChanged : ((Boolean) -> Unit)? = null,
    content : (@Composable () -> Unit)? = null
) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    content =  {
        val exitFade = fadeOut(animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing))
        val enterFade = fadeIn(animationSpec = tween(durationMillis = 300))
        val exitAnimationSpec : FiniteAnimationSpec<IntSize> = tween(
            durationMillis = 700,
            easing = FastOutLinearInEasing,
        )
        val enterAnimationSpec : FiniteAnimationSpec<IntSize> = tween(
            durationMillis = 700,
            easing = FastOutLinearInEasing,
        )

        AnimatedVisibility(
            visible = showDurationMarker,
            exit = exitFade + shrinkHorizontally(shrinkTowards = Alignment.Start, animationSpec = exitAnimationSpec),
            enter = enterFade + expandHorizontally(expandFrom = Alignment.Start, animationSpec = enterAnimationSpec),
            content = {
                val currentPosition by mediaController.currentMediaItemPositionAsFlow(coroutineScope).collectAsState()

                Text(
                    text = currentPosition,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        )

        AnimatedContent(
            targetState = showDurationMarker,
            transitionSpec = {
                val enterTween = tween<Float>(durationMillis = 300)
                val enterAnimation = fadeIn(animationSpec = enterTween) +
                    scaleIn(initialScale = 0.8f, animationSpec = enterTween) +
                    expandVertically(expandFrom = Alignment.Top, animationSpec = tween(durationMillis = 300))

                val exitTween = tween<Float>(durationMillis = 150)
                val exitAnimation = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(durationMillis = 150)) +
                    scaleOut(animationSpec = exitTween) + fadeOut(animationSpec = exitTween)
                enterAnimation.togetherWith(exitAnimation)
            },
            content =  {
                if(it) {
                    val isPlaying by rememberMediaControllerIsPlaying(mediaController = mediaController,onPlayChanged = onPlayChanged)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                        content = {
                            AnimatedAmplitudes(
                                modifier = Modifier.matchParentSize(),
                                barWidth = 3.dp,
                                gapWidth = 2.dp,
                                isAnimating = isPlaying,
                            )

                            PlayPauseButton(mediaController = mediaController)
                        }
                    )
                    return@AnimatedContent
                }

                content!!()
            }
        )

        AnimatedVisibility(
            visible = showDurationMarker,
            exit = exitFade + shrinkHorizontally(shrinkTowards = Alignment.End, animationSpec = exitAnimationSpec),
            enter = enterFade + expandHorizontally(expandFrom = Alignment.End, animationSpec = enterAnimationSpec),
            content = {
                val duration by rememberMediaItemDuration(mediaController)

                Text(
                    text = duration.formatAsTime(),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        )
    }
)

const val MaxLinesCount = 100
@Composable
fun AnimatedAmplitudes(
    modifier: Modifier = Modifier,
    barWidth: Dp = 2.dp,
    gapWidth: Dp = 2.dp,
    barColor: Color = Color.White,
    isAnimating: Boolean = false,
) {
    val infiniteAnimation = rememberInfiniteTransition()
    val animations = mutableListOf<State<Float>>()
    val random = remember { Random(100) }

    repeat(15) {
        val durationMillis = random.nextInt(500, 2000)
        animations += infiniteAnimation.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis),
                repeatMode = RepeatMode.Reverse,
            )
        )
    }

    @Composable
    fun Dp.toPxf() = with(LocalDensity.current) { this@toPxf.toPx() }
    val barWidthFloat by rememberUpdatedState(newValue = barWidth.toPxf())
    val gapWidthFloat by rememberUpdatedState(newValue = gapWidth.toPxf())

    val initialMultipliers = remember {
        mutableListOf<Float>().apply {
            repeat(MaxLinesCount) { this += random.nextFloat() }
        }
    }

    val heightDivider by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 6f,
        animationSpec = tween(1000, easing = LinearEasing)
    )

    Canvas(modifier = modifier) {
        val canvasHeight = size.height
        val canvasWidth = size.width
        val canvasCenterY = canvasHeight / 2f

        val count = (canvasWidth / (barWidthFloat + gapWidthFloat)).toInt().coerceAtMost(MaxLinesCount)
        val animatedVolumeWidth = count * (barWidthFloat + gapWidthFloat)
        var startOffset = (canvasWidth - animatedVolumeWidth) / 2

        val barMinHeight = 0f
        val barMaxHeight = canvasHeight / 2f / heightDivider

        repeat(count) { index ->
            val currentSize = animations[index % animations.size].value
            var barHeightPercent = initialMultipliers[index] + currentSize
            if (barHeightPercent > 1.0f) {
                val diff = barHeightPercent - 1.0f
                barHeightPercent = 1.0f - diff
            }

            val barHeight =(1 - barHeightPercent) * barMinHeight + barHeightPercent * barMaxHeight

            drawLine(
                color = barColor,
                start = Offset(startOffset, canvasCenterY - barHeight / 2),
                end = Offset(startOffset, canvasCenterY + barHeight / 2),
                strokeWidth = barWidthFloat,
                cap = StrokeCap.Round,
            )
            startOffset += barWidthFloat + gapWidthFloat
        }
    }
}

// TODO : Use https://exyte.com/blog/android-dribbble-replicating-part-1
@Deprecated("Replace this")
@Composable
fun DurationMarkers(
    mediaController : Player,
    coroutineScope: CoroutineScope,
) = Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
    val currentPosition by mediaController.currentMediaItemPositionAsFlow(coroutineScope).collectAsState()

    Text(text = currentPosition)

    val duration by rememberMediaItemDuration(mediaController)

    Text(text = duration.formatAsTime())
}

fun Player.currentMediaItemPositionAsFlow(scope: CoroutineScope): StateFlow<String> = flow {
    while (true) {
        if(this@currentMediaItemPositionAsFlow.isPlaying) emit(this@currentMediaItemPositionAsFlow.currentPosition.formatAsTime())
        delay(1000) // Update the media metadata every second
    }
}.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000L),
    initialValue = formatIntoTime(0,0,0),
)

fun Long.formatAsTime() = milliseconds.toComponents { hours, minutes, seconds, _ -> formatIntoTime(hours,minutes,seconds) }
private fun formatIntoTime(hours : Long, minutes : Int, seconds : Int): String = String.format(
    "%02d:%02d:%02d",
    hours,
    minutes,
    seconds,
)