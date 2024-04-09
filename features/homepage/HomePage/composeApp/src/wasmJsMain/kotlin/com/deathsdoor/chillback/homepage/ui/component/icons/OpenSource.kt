package com.deathsdoor.chillback.homepage.ui.component.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons

public val Icons.OpenSource: ImageVector
    get() {
        if (`_lock-open-source-svgrepo-com` != null) {
            return `_lock-open-source-svgrepo-com`!!
        }
        `_lock-open-source-svgrepo-com` = Builder(name = "Open Source",
                defaultWidth = 800.0.dp, defaultHeight = 800.0.dp, viewportWidth = 48.0f,
                viewportHeight = 48.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(32.0f, 21.9f)
                horizontalLineToRelative(0.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(40.0f, 18.0f)
                horizontalLineTo(16.0f)
                verticalLineTo(13.0f)
                arcToRelative(7.0f, 7.0f, 0.0f, false, true, 7.0f, -7.0f)
                horizontalLineToRelative(2.0f)
                arcToRelative(7.1f, 7.1f, 0.0f, false, true, 5.0f, 2.1f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.2f, 0.5f)
                horizontalLineToRelative(0.1f)
                arcToRelative(1.9f, 1.9f, 0.0f, false, false, 0.6f, -3.1f)
                arcTo(10.9f, 10.9f, 0.0f, false, false, 25.0f, 2.0f)
                horizontalLineTo(23.0f)
                arcTo(11.0f, 11.0f, 0.0f, false, false, 12.0f, 13.0f)
                verticalLineToRelative(5.0f)
                horizontalLineTo(8.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, -2.0f, 2.0f)
                verticalLineTo(44.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.0f, 2.0f)
                horizontalLineTo(40.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.0f, -2.0f)
                verticalLineTo(20.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, false, 40.0f, 18.0f)
                close()
                moveTo(38.0f, 42.0f)
                horizontalLineTo(10.0f)
                verticalLineTo(22.0f)
                horizontalLineTo(38.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.0f, 40.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, -1.3f, -3.5f)
                lineTo(19.0f, 32.0f)
                lineToRelative(-5.3f, -4.5f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 2.6f, -3.0f)
                lineToRelative(7.0f, 6.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 0.0f, 3.0f)
                lineToRelative(-7.0f, 6.0f)
                arcTo(1.9f, 1.9f, 0.0f, false, true, 15.0f, 40.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(33.0f, 38.0f)
                horizontalLineTo(27.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 0.0f, -4.0f)
                horizontalLineToRelative(6.0f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, true, 0.0f, 4.0f)
                close()
            }
        }
        .build()
        return `_lock-open-source-svgrepo-com`!!
    }

private var `_lock-open-source-svgrepo-com`: ImageVector? = null
