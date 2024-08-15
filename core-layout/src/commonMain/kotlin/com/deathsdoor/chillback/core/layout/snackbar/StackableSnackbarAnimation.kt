package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.deathsdoor.chillback.core.layout.snackbar.Constant.TWEEN_ANIMATION_DURATION

@Stable
enum class StackableSnackbarAnimation(
    val paddingAnimationSpec: AnimationSpec<Dp>,
    val scaleAnimationSpec: AnimationSpec<Float>,
    val enterAnimationSpec: FiniteAnimationSpec<IntOffset>,
    val exitAnimationSpec: FiniteAnimationSpec<IntOffset>,
) {
    Bounce(
        paddingAnimationSpec =
        spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
        ),
        scaleAnimationSpec =
        spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
        ),
        enterAnimationSpec =
        spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
        exitAnimationSpec =
        spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioHighBouncy,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
    ),
    Slide(
        paddingAnimationSpec =
        tween(
            TWEEN_ANIMATION_DURATION,
            0,
            FastOutSlowInEasing,
        ),
        scaleAnimationSpec =
        tween(
            TWEEN_ANIMATION_DURATION,
            0,
            FastOutSlowInEasing,
        ),
        enterAnimationSpec =
        tween(
            TWEEN_ANIMATION_DURATION,
            0,
            FastOutSlowInEasing,
        ),
        exitAnimationSpec =
        tween(
            TWEEN_ANIMATION_DURATION,
            0,
            FastOutSlowInEasing,
        ),
    ),
}