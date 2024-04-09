package com.deathsdoor.chillback.homepage.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

internal val emailAddressRegex by lazy {
    Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}

internal const val PROJECT_HOME_PAGE = "https://github.com/Deaths-Door/Chillback"

internal fun String.asAnnotatedStringWith(
    substring : String,
    builder : AnnotatedString.Builder.(section : String) -> Unit
): AnnotatedString = buildAnnotatedString {
    val index = indexOf(substring)
    append(substring(startIndex = 0, endIndex = index))

    val endIndex = index + substring.length
    builder(substring(startIndex = index, endIndex = endIndex))

    append(substring(startIndex = endIndex))

}

internal fun String.asAnnotatedStringWithSpanStyle(
    substring : String,
    style :SpanStyle
): AnnotatedString = asAnnotatedStringWith(substring) { section ->
    withStyle(style = style) {
        append(section)
    }
}