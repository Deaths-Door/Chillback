package com.deathsdoor.chillback.feature.mediaplayer.icons

/* This ImageVector was generated using Composables – https://www.composables.com */
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _ZoomPan: ImageVector? = null

public val Icons.ZoomPan: ImageVector
		get() {
			if (_ZoomPan != null) {
				return _ZoomPan!!
			}
_ZoomPan = ImageVector.Builder(
                name = "ZoomPan",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f
            ).apply {
				path(
    				fill = SolidColor(Color(0xFF000000)),
    				fillAlpha = 1.0f,
    				stroke = null,
    				strokeAlpha = 1.0f,
    				strokeLineWidth = 1.0f,
    				strokeLineCap = StrokeCap.Butt,
    				strokeLineJoin = StrokeJoin.Miter,
    				strokeLineMiter = 1.0f,
    				pathFillType = PathFillType.NonZero
				) {
					moveTo(120f, 840f)
					lineTo(120f, 600f)
					lineTo(200f, 600f)
					lineTo(200f, 704f)
					lineTo(324f, 580f)
					lineTo(380f, 636f)
					lineTo(256f, 760f)
					lineTo(360f, 760f)
					lineTo(360f, 840f)
					lineTo(120f, 840f)
					close()
					moveTo(636f, 380f)
					lineTo(580f, 324f)
					lineTo(704f, 200f)
					lineTo(600f, 200f)
					lineTo(600f, 120f)
					lineTo(840f, 120f)
					lineTo(840f, 360f)
					lineTo(760f, 360f)
					lineTo(760f, 256f)
					lineTo(636f, 380f)
					close()
}
}.build()
return _ZoomPan!!
		}

