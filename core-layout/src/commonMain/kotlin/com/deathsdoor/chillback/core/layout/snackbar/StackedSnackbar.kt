package com.deathsdoor.chillback.core.layout.snackbar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun StackableSnackbar(
    state : StackableSnackbarState,
    maxStack: Int,
    animation: StackableSnackbarAnimation,
    onSnackbarRemoved: () -> Unit,
    firstItemVisibility: Boolean,
    modifier: Modifier = Modifier,
) {
    val snackbarData = state.currentSnackbarData
    val snackbarDataSize =snackbarData.size
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        snackbarData.forEachIndexed { index, data ->
            val multiplier = abs(index.toFloat() - snackbarDataSize.dec().toFloat())
            val scale = 1f.minus((multiplier).times(Constant.SCALE_DECREMENT))

            val padding = ((multiplier.times(Constant.PADDING_INCREMENT)).plus(Constant.PADDING_INCREMENT)).dp

            val scaleAnimation by animateFloatAsState(
                scale,
                animationSpec = animation.scaleAnimationSpec,
            )
            val initialPos by animateFloatAsState(
                0f,
                animationSpec = animation.scaleAnimationSpec,
            )
            val paddingAnimation by animateDpAsState(
                padding,
                animationSpec = animation.paddingAnimationSpec,
            )

            var offsetX by remember { mutableStateOf(-1f) }

            AnimatedVisibility(
                visible = if (snackbarDataSize.dec() == index) firstItemVisibility else true,
                enter =
                slideIn(
                    initialOffset =
                    { IntOffset(0, Constant.Y_TARGET_ENTER) },
                    animationSpec = animation.enterAnimationSpec,
                ),
                exit =
                if (offsetX == -1f) {
                    slideOut(
                        targetOffset =
                        { IntOffset(0, Constant.Y_TARGET_EXIT) },
                        animationSpec = animation.exitAnimationSpec,
                    )
                } else {
                    slideOutHorizontally(
                        targetOffsetX = { if (offsetX > 0) Constant.X_TARGET_EXIT_RIGHT else Constant.X_TARGET_EXIT_LEFT },
                        animationSpec =
                        tween(
                            easing = LinearEasing,
                        ),
                    )
                },
            ) {
                val draggableModifier =
                    if (snackbarData.lastIndex == index) {
                        Modifier
                            .offset { IntOffset(offsetX.roundToInt(), 0) }
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state =
                                rememberDraggableState { delta ->
                                    offsetX += delta
                                },
                                onDragStopped = {
                                    if (offsetX >= Constant.OFFSET_THRESHOLD_EXIT_RIGHT || offsetX <= Constant.OFFSET_THRESHOLD_EXIT_LEFT) {
                                        onSnackbarRemoved.invoke()
                                    } else {
                                        offsetX = initialPos
                                    }
                                },
                            )
                    } else {
                        Modifier
                    }
                val snackbarScale =
                    if (snackbarDataSize - index > maxStack) {
                        0f
                    } else {
                        scaleAnimation
                    }
                when (data) {
                    is StackableSnackbarData.Custom ->
                        CustomStackableSnackbarItem(
                            data = data,
                            scaleAnimation = snackbarScale,
                            paddingAnimation = paddingAnimation,
                            modifier = draggableModifier,
                            onActionClicked = {
                                onSnackbarRemoved.invoke()
                            },
                        )

                    is StackableSnackbarData.Normal ->
                        NormalStackableSnackbarItem(
                            data = data,
                            scaleAnimation = snackbarScale,
                            paddingAnimation = paddingAnimation,
                            modifier = draggableModifier,
                            onActionClicked = {
                                onSnackbarRemoved.invoke()
                                data.action?.invoke()
                            },
                        )
                }
            }
        }
    }
}

@Composable
private fun CustomStackableSnackbarItem(
    data: StackableSnackbarData.Custom,
    scaleAnimation: Float,
    paddingAnimation: Dp,
    onActionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CardSnackbarContainer(
        scaleAnimation = scaleAnimation,
        paddingAnimation = paddingAnimation,
        modifier = modifier,
        content = {
            Box(
                modifier =
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
            ) {
                data.content.invoke(onActionClicked)
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NormalStackableSnackbarItem(
    data: StackableSnackbarData.Normal,
    scaleAnimation: Float,
    paddingAnimation: Dp,
    onActionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) = CardSnackbarContainer(
    scaleAnimation = scaleAnimation,
    paddingAnimation = paddingAnimation,
    modifier = modifier,
    content = {
        Row(
            modifier =
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(data.type.color.copy(alpha = 0.1f))
                        .padding(8.dp),
                    content = {
                        Image(
                            imageVector = data.type.icon,
                            null,
                        )
                    }
                )

                Column {
                    Text(
                        modifier= Modifier.basicMarquee(),
                        text = data.title,
                        overflow = TextOverflow.Ellipsis,
                        style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.Black,
                    )

                    if (!data.description.isNullOrEmpty()) {
                        Text(
                            modifier= Modifier.basicMarquee(),
                            text = data.description.orEmpty(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                    }
                    if (!data.actionTitle.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            contentAlignment = Alignment.BottomEnd,
                            content = {
                                Text(
                                    data.actionTitle.orEmpty(),
                                    modifier =
                                    Modifier.clickable {
                                        onActionClicked.invoke()
                                    }.basicMarquee(),
                                    style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = data.type.color,
                                )
                            }
                        )
                    }
                }
            }
        )
    },
)

@Composable
private fun CardSnackbarContainer(
    scaleAnimation: Float,
    paddingAnimation: Dp,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) = Card(
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier
        .padding(bottom = paddingAnimation, start = 16.dp, end = 16.dp)
        .wrapContentHeight()
        .scale(scaleAnimation)
        .then(modifier),
    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
    content = content
)