package com.deathsdoor.chillback.core.layout.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


const val PROJECT_HOME_PAGE = "https://github.com/Deaths-Door/Chillback"

fun String.asAnnotatedStringWith(
    substring : String,
    builder : AnnotatedString.Builder.(section : String) -> Unit
): AnnotatedString = buildAnnotatedString {
    val index = indexOf(substring)
    append(substring(startIndex = 0, endIndex = index))

    val endIndex = index + substring.length
    builder(substring(startIndex = index, endIndex = endIndex))

    append(substring(startIndex = endIndex))

}

fun String.asAnnotatedStringWithSpanStyle(
    substring : String,
    style :SpanStyle
): AnnotatedString = asAnnotatedStringWith(substring) { section ->
    withStyle(style = style) {
        append(section)
    }
}