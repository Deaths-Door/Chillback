package com.deathsdoor.chillback.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun AnnotatedString.tag(tag : String,offset: Int) = getStringAnnotations(tag = tag, start = offset, end = offset).firstOrNull()

fun AnnotatedString.Range<String>.openLink(uriHandler: UriHandler) = uriHandler.openUri(item)

@Composable
@ReadOnlyComposable
fun styledText(
    plain0 : String,
    colored0 : String,
    plain1 : String,
    colored1 : String
) = buildAnnotatedString {
    append(plain0)
    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
        append(colored0)
    }

    append(plain1)

    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
        append(colored1)
    }
}