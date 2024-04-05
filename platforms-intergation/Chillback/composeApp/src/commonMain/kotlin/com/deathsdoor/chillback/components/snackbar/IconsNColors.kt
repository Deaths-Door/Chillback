package com.deathsdoor.chillback.components.snackbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val SnackbarError = Color(0xFFF54F4E)
private val SnackbarLink = Color(0xFF246EE5)
internal val SnackbarInfo = Color(0xFF3150EC)
internal val SnackbarWarning = Color(0xFFFE9E01)
internal val SnackbarSuccess = Color(0xFF24BF5F)

internal val SnackbarErrorIcon: ImageVector
    get() {
        if (_icError != null) {
            return _icError!!
        }
        _icError =
            ImageVector.Builder(
                name = "SnackbarErrorIcon", defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f,
            ).apply {
                path(
                    fill = SolidColor(SnackbarError), stroke = SolidColor(SnackbarError),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd,
                ) {
                    moveTo(256.0f, 42.667f)
                    curveTo(373.61f, 42.667f, 469.333f, 138.39f, 469.333f, 256.0f)
                    curveTo(469.333f, 373.61f, 373.61f, 469.333f, 256.0f, 469.333f)
                    curveTo(138.39f, 469.333f, 42.667f, 373.61f, 42.667f, 256.0f)
                    curveTo(42.667f, 138.39f, 138.39f, 42.667f, 256.0f, 42.667f)
                    close()
                    moveTo(256.0f, 85.333f)
                    curveTo(161.541f, 85.333f, 85.333f, 161.541f, 85.333f, 256.0f)
                    curveTo(85.333f, 350.459f, 161.541f, 426.667f, 256.0f, 426.667f)
                    curveTo(350.459f, 426.667f, 426.667f, 350.459f, 426.667f, 256.0f)
                    curveTo(426.667f, 161.541f, 350.459f, 85.333f, 256.0f, 85.333f)
                    close()
                    moveTo(256.0f, 314.709f)
                    curveTo(271.238f, 314.709f, 282.667f, 325.973f, 282.667f, 341.333f)
                    curveTo(282.667f, 356.693f, 271.238f, 367.957f, 256.0f, 367.957f)
                    curveTo(240.416f, 367.957f, 229.333f, 356.693f, 229.333f, 340.992f)
                    curveTo(229.333f, 325.973f, 240.762f, 314.709f, 256.0f, 314.709f)
                    close()
                    moveTo(277.333f, 128.0f)
                    lineTo(277.333f, 277.333f)
                    lineTo(234.667f, 277.333f)
                    lineTo(234.667f, 128.0f)
                    lineTo(277.333f, 128.0f)
                    close()
                }
            }
                .build()
        return _icError!!
    }

internal val SnackbarInfoIcon: ImageVector
    get() {
        if (_icInfo != null) {
            return _icInfo!!
        }
        _icInfo =
            ImageVector.Builder(
                name = "IcInfo", defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f,
            ).apply {
                path(
                    fill = SolidColor(SnackbarInfo), stroke = SolidColor(SnackbarInfo),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd,
                ) {
                    moveTo(256.0f, 42.667f)
                    curveTo(138.18f, 42.667f, 42.667f, 138.178f, 42.667f, 256.0f)
                    curveTo(42.667f, 373.82f, 138.18f, 469.333f, 256.0f, 469.333f)
                    curveTo(373.822f, 469.333f, 469.333f, 373.82f, 469.333f, 256.0f)
                    curveTo(469.333f, 138.178f, 373.822f, 42.667f, 256.0f, 42.667f)
                    close()
                    moveTo(256.0f, 426.667f)
                    curveTo(161.895f, 426.667f, 85.333f, 350.105f, 85.333f, 256.0f)
                    curveTo(85.333f, 161.895f, 161.895f, 85.333f, 256.0f, 85.333f)
                    curveTo(350.107f, 85.333f, 426.667f, 161.895f, 426.667f, 256.0f)
                    curveTo(426.667f, 350.105f, 350.107f, 426.667f, 256.0f, 426.667f)
                    close()
                    moveTo(282.713f, 170.667f)
                    curveTo(282.713f, 186.134f, 271.452f, 197.333f, 256.217f, 197.333f)
                    curveTo(240.365f, 197.333f, 229.38f, 186.134f, 229.38f, 170.371f)
                    curveTo(229.38f, 155.22f, 240.663f, 144.0f, 256.217f, 144.0f)
                    curveTo(271.452f, 144.0f, 282.713f, 155.22f, 282.713f, 170.667f)
                    close()
                    moveTo(234.713f, 234.667f)
                    lineTo(277.38f, 234.667f)
                    lineTo(277.38f, 362.667f)
                    lineTo(234.713f, 362.667f)
                    lineTo(234.713f, 234.667f)
                    close()
                }
            }
                .build()
        return _icInfo!!
    }
internal val SnackbarSuccessIcon: ImageVector
    get() {
        if (_icSuccess != null) {
            return _icSuccess!!
        }
        _icSuccess =
            ImageVector.Builder(
                name = "IcSuccess", defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f,
            ).apply {
                path(
                    fill = SolidColor(SnackbarSuccess), stroke = SolidColor(SnackbarSuccess),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd,
                ) {
                    moveTo(256.0f, 42.667f)
                    curveTo(138.18f, 42.667f, 42.667f, 138.18f, 42.667f, 256.0f)
                    curveTo(42.667f, 373.82f, 138.18f, 469.333f, 256.0f, 469.333f)
                    curveTo(373.82f, 469.333f, 469.333f, 373.82f, 469.333f, 256.0f)
                    curveTo(469.333f, 138.18f, 373.82f, 42.667f, 256.0f, 42.667f)
                    close()
                    moveTo(256.0f, 426.667f)
                    curveTo(161.895f, 426.667f, 85.333f, 350.105f, 85.333f, 256.0f)
                    curveTo(85.333f, 161.895f, 161.895f, 85.333f, 256.0f, 85.333f)
                    curveTo(350.105f, 85.333f, 426.667f, 161.895f, 426.667f, 256.0f)
                    curveTo(426.667f, 350.105f, 350.106f, 426.667f, 256.0f, 426.667f)
                    close()
                    moveTo(336.336f, 179.781f)
                    lineTo(366.503f, 209.948f)
                    lineTo(234.667f, 342.336f)
                    lineTo(155.583f, 263.252f)
                    lineTo(185.75f, 233.086f)
                    lineTo(234.667f, 282.003f)
                    lineTo(336.336f, 179.781f)
                    close()
                }
            }
                .build()
        return _icSuccess!!
    }
public val SnackbarWarningIcon: ImageVector
    get() {
        if (_icWarning != null) {
            return _icWarning!!
        }
        _icWarning =
            ImageVector.Builder(
                name = "IcWarning", defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f,
            ).apply {
                path(
                    fill = SolidColor(SnackbarWarning), stroke = SolidColor(SnackbarWarning),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = EvenOdd,
                ) {
                    moveTo(278.313f, 48.296f)
                    curveTo(284.928f, 52.075f, 290.41f, 57.557f, 294.189f, 64.172f)
                    lineTo(476.667f, 383.508f)
                    curveTo(488.358f, 403.967f, 481.25f, 430.03f, 460.791f, 441.722f)
                    curveTo(454.344f, 445.405f, 447.047f, 447.343f, 439.622f, 447.343f)
                    lineTo(74.667f, 447.343f)
                    curveTo(51.103f, 447.343f, 32.0f, 428.241f, 32.0f, 404.677f)
                    curveTo(32.0f, 397.251f, 33.938f, 389.955f, 37.622f, 383.508f)
                    lineTo(220.099f, 64.172f)
                    curveTo(231.79f, 43.713f, 257.854f, 36.605f, 278.313f, 48.296f)
                    close()
                    moveTo(257.144f, 85.341f)
                    lineTo(74.667f, 404.677f)
                    lineTo(439.622f, 404.677f)
                    lineTo(257.144f, 85.341f)
                    close()
                    moveTo(256.0f, 314.667f)
                    curveTo(271.238f, 314.667f, 282.667f, 325.931f, 282.667f, 341.291f)
                    curveTo(282.667f, 356.651f, 271.238f, 367.915f, 256.0f, 367.915f)
                    curveTo(240.416f, 367.915f, 229.333f, 356.651f, 229.333f, 340.949f)
                    curveTo(229.333f, 325.931f, 240.762f, 314.667f, 256.0f, 314.667f)
                    close()
                    moveTo(277.333f, 149.333f)
                    lineTo(277.333f, 277.333f)
                    lineTo(234.667f, 277.333f)
                    lineTo(234.667f, 149.333f)
                    lineTo(277.333f, 149.333f)
                    close()
                }
            }
                .build()
        return _icWarning!!
    }

private var _icWarning: ImageVector? = null
private var _icSuccess: ImageVector? = null
private var _icInfo: ImageVector? = null
private var _icError: ImageVector? = null