package com.deathsdoor.chillback.feature.mediaplayer.icons

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _ZoomInwards: ImageVector? = null

public val Icons.ZoomInwards: ImageVector
    get() {
        if (_ZoomInwards != null) {
            return _ZoomInwards!!
        }
        _ZoomInwards = ImageVector.Builder(
            name = "ZoomInwards",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group {
                group {
                    group {
                        path(
                            fill = SolidColor(Color(0xFFFFFFFF)),
                            fillAlpha = 0f,
                            stroke = null,
                            strokeAlpha = 0f,
                            strokeLineWidth = 1.0f,
                            strokeLineCap = StrokeCap.Butt,
                            strokeLineJoin = StrokeJoin.Miter,
                            strokeLineMiter = 1.0f,
                            pathFillType = PathFillType.NonZero
                        ) {
                            moveTo(0f, 0f)
                            horizontalLineTo(24f)
                            verticalLineTo(24f)
                            horizontalLineTo(0f)
                            verticalLineTo(0f)
                            close()
                        }
                        path(
                            fill = SolidColor(Color(0xFF231F20)),
                            fillAlpha = 1.0f,
                            stroke = null,
                            strokeAlpha = 1.0f,
                            strokeLineWidth = 1.0f,
                            strokeLineCap = StrokeCap.Butt,
                            strokeLineJoin = StrokeJoin.Miter,
                            strokeLineMiter = 1.0f,
                            pathFillType = PathFillType.NonZero
                        ) {
                            moveTo(19f, 9f)
                            horizontalLineTo(16.42f)
                            lineToRelative(3.29f, -3.29f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, -1.42f, -1.42f)
                            lineTo(15f, 7.57f)
                            verticalLineTo(5f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, -1f)
                            horizontalLineToRelative(0f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 1f)
                            lineToRelative(0f, 5f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 1f)
                            horizontalLineToRelative(5f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2f)
                            close()
                        }
                        path(
                            fill = SolidColor(Color(0xFF231F20)),
                            fillAlpha = 1.0f,
                            stroke = null,
                            strokeAlpha = 1.0f,
                            strokeLineWidth = 1.0f,
                            strokeLineCap = StrokeCap.Butt,
                            strokeLineJoin = StrokeJoin.Miter,
                            strokeLineMiter = 1.0f,
                            pathFillType = PathFillType.NonZero
                        ) {
                            moveTo(10f, 13f)
                            lineTo(5f, 13f)
                            horizontalLineTo(5f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2f)
                            horizontalLineTo(7.57f)
                            lineTo(4.29f, 18.29f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.42f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.42f, 0f)
                            lineTo(9f, 16.42f)
                            verticalLineTo(19f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 1f)
                            horizontalLineToRelative(0f)
                            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, -1f)
                            verticalLineTo(14f)
                            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 10f, 13f)
                            close()
                        }
                    }
                }
            }
        }.build()
        return _ZoomInwards!!
    }

