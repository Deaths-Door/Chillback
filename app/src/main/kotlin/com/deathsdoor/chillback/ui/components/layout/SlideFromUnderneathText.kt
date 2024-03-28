package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi

/// Copyed from [source](https://github.com/alexmercerind/moving-letters-android/blob/main/moving-letters/src/main/java/com/alexmercerind/movingletters/AnimatedTextState.kt)
@Composable
fun SlideFromUnderneathText(
    modifier: Modifier = Modifier,
    currentState: AnimatedTextState = rememberAnimatedTextState(),
    text: String,
    style: TextStyle? = null,
    dampingRatio: Float = DampingRatioMediumBouncy,
    stiffness: Float = StiffnessMediumLow,
    intermediateDuration: Duration = 80.milliseconds,
    animateOnMount: Boolean = true
) {
    val animationSpec: FiniteAnimationSpec<IntOffset> = spring(dampingRatio, stiffness)

    val currentStyle = style ?: LocalTextStyle.current
    if (!currentState.attached) {
        currentState.attached = true

        currentState.visibility = text.map { mutableStateOf(false) }
        currentState.lineHeight = text.map { 0.0F }.toMutableList()
        currentState.transformOrigin = text.map { TransformOrigin.Center }.toMutableList()

        currentState.animationDuration = 2000.milliseconds
        currentState.intermediateDuration = intermediateDuration
    }

    LaunchedEffect("JumpAnimatedText", Dispatchers.IO) {
        if (animateOnMount) {
            currentState.start()
        }
    }

    Box(modifier = modifier.clipToBounds()) {
        Text(
            text = text,
            style = currentStyle.copy(color = Color.Transparent),
            onTextLayout = {
                for (offset in text.indices) {
                    val x = it.multiParagraph.getBoundingBox(offset).width / 2 + it.multiParagraph.getHorizontalPosition(offset, true)
                    var y = 0.0F
                    val line = it.multiParagraph.getLineForOffset(offset)
                    for (i in 0..line) {
                        y += if (i == line) it.multiParagraph.getLineHeight(i) / 2 else it.multiParagraph.getLineHeight(i)
                    }
                    currentState.transformOrigin[offset] = TransformOrigin(
                        x / it.multiParagraph.width,
                        y / it.multiParagraph.height
                    )
                    currentState.lineHeight[offset] = it.multiParagraph.getLineHeight(line)
                }
                currentState.layout.complete(Any())
            }
        )
        if (currentState.current >= 0) {
            Text(
                text = buildAnnotatedString {
                    addStyle(currentStyle.toSpanStyle(), 0, currentState.current)
                    addStyle(currentStyle.copy(color = Color.Transparent).toSpanStyle(), currentState.current + 1, text.length)
                    append(text)
                },
                style = currentStyle,
            )
        }
        for (i in text.indices) {
            AnimatedVisibility(
                visible = currentState.visibility[i].value,
                enter = slideInVertically(
                    animationSpec = animationSpec,
                    initialOffsetY =  {
                        (0.5F * currentState.lineHeight[i]).toInt()
                    }
                ),
                exit = fadeOut(animationSpec = tween(0))
            ) {
                Text(
                    text = buildAnnotatedString {
                        addStyle(currentStyle.toSpanStyle().copy(color = Color.Transparent), 0, i)
                        addStyle(currentStyle.toSpanStyle().copy(color = Color.Transparent), i + 1, text.length)
                        append(text)
                    },
                    style = currentStyle,
                )
            }
        }
    }
}

/**
 * A state object that can be hoisted to control and observe the animated text state.
 * In most cases, this state will be created via [rememberAnimatedTextState].
 */
@OptIn(DelicateCoroutinesApi::class)
class AnimatedTextState {
    /** Whether animation is currently paused. */
    val paused: StateFlow<Boolean> get() = _paused

    /** Whether animation is currently stopped. */
    val stopped: StateFlow<Boolean> get() = _stopped

    /** Whether animation is currently playing (not paused). */
    val playing get() = paused.map { !it }

    /** Whether animation is currently ongoing (not stopped). */
    val ongoing get() = stopped.map { !it }

    /** Whether this instance is attached to an animated text. */
    internal var attached = false

    /** Whether initial text layout has been done. */
    internal val layout = CompletableDeferred<Any>()

    /** Current visible character index. */
    internal var current by mutableStateOf(-1)

    /** Current animation coroutine job. */
    private var job: Job? = null

    /** Current animation coroutine jobs. */
    private var jobs: MutableSet<Job> = mutableSetOf()

    private var _paused = MutableStateFlow(true)
    private var _stopped = MutableStateFlow(true)

    private var _visibility: List<MutableState<Boolean>>? = null
    private var _lineHeight: MutableList<Float>? = null
    private var _transformOrigin: MutableList<TransformOrigin>? = null

    private var _animationDuration: Duration? = null
    private var _intermediateDuration: Duration? = null

    internal var visibility: List<MutableState<Boolean>>
        get() = _visibility!!
        set(value) {
            _visibility = value
        }
    internal var lineHeight: MutableList<Float>
        get() = _lineHeight!!
        set(value) {
            _lineHeight = value
        }
    internal var transformOrigin: MutableList<TransformOrigin>
        get() = _transformOrigin!!
        set(value) {
            _transformOrigin = value
        }
    internal var animationDuration: Duration
        get() = _animationDuration!!
        set(value) {
            _animationDuration = value
        }
    internal var intermediateDuration: Duration
        get() = _intermediateDuration!!
        set(value) {
            _intermediateDuration = value
        }

    fun start() {
        stop()
        resume()
        _stopped.update { false }
    }

    private fun stop() {
        pause()
        current = -1
        for (i in visibility.indices) {
            visibility[i].value = false
        }
        _stopped.update { true }
    }

    private fun resume() {
        if (paused.value) {
            job = GlobalScope.launch(Dispatchers.IO) {
                layout.await()
                for (i in current.coerceIn(0, 1 shl 16) until visibility.size) {
                    if (coroutineContext.isActive) {
                        delay(intermediateDuration)
                        visibility[i].value = true
                        jobs.add(
                            async {
                                delay(animationDuration)
                                current = i
                                delay(100)
                                visibility[i].value = false

                                if (current == visibility.size - 1) {
                                    _stopped.update { true }
                                }
                            }
                        )
                    }
                }
            }
            _paused.update { false }
        }
    }

    private fun pause() {
        if (!paused.value) {
            job?.cancel()
            job = null
            jobs.forEach { it.cancel() }
            jobs.clear()
            _paused.update { true }
        }
    }
}


/**
 * Creates a [AnimatedTextState] that is remembered across compositions.
 */
@Composable
fun rememberAnimatedTextState() = remember { AnimatedTextState() }