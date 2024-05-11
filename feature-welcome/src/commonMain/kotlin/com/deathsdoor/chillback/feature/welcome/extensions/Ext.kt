package com.deathsdoor.chillback.feature.welcome.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import com.deathsdoor.chillback.feature.welcome.states.EmailState
import com.deathsdoor.chillback.feature.welcome.states.PasswordState

internal fun Boolean.toInt() = if (this) 1 else 0

internal fun String.asGithubFileForLegal() = "https://github.com/Deaths-Door/Chillback/tree/main/legal/$this"

internal fun AnnotatedString.tag(tag : String,offset: Int) = getStringAnnotations(tag = tag, start = offset, end = offset).firstOrNull()
internal fun AnnotatedString.Range<String>.openLink(uriHandler: UriHandler) = uriHandler.openUri(item)

internal fun AnnotatedString.Builder.clickableLink(
    tag : String,
    source : String,
    text : AnnotatedString.Builder.() -> Unit
) {
    pushStringAnnotation(tag = tag, annotation = source)
    text()
    pop()
}

@Composable
internal fun rememberEmailState() = remember { EmailState() }

@Composable
internal fun rememberPasswordState() = remember { PasswordState() }
