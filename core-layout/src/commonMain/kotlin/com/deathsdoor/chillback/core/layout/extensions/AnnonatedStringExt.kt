package com.deathsdoor.chillback.core.layout.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

private const val PLACEHOLDER = "|-|"

@Composable
fun StringResource.styleWith(
    spanStyle: SpanStyle,
    vararg phrases : StringResource
) = styleWith(
    onAppend =  { _ ,phrase ->
        withStyle(style = spanStyle) {
            append(phrase)
        }
    },
    phrases = phrases
)

@Composable
fun StringResource.styleWith(
    onAppend : AnnotatedString.Builder.(index : Int,phrase : String) -> Unit,
    vararg phrases : StringResource
): AnnotatedString = styleWith(
    onTranslatableTextAppend = onAppend,
    phrases = Array(phrases.size){
        stringResource(phrases[it])
    }
)
/**
 * Styles a translatable phrase with a specific style and combines it with other translatable phrases.
 *
 * This function takes a base translatable phrase (`this@styleWith`), a desired styling information
 * (`spanStyle`), and an optional variable number of additional translatable phrases (`phrases`).
 * It builds an annotated string where the base phrase gets styled with the provided style,
 * and other phrases are inserted at appropriate positions based on placeholders within the base phrase.
 *
 * @param phrases An optional variable number of additional translatable phrases to combine with the base phrase.
 * @return An annotated string with the styled base phrase and combined translatable phrases.
 */
@Composable
fun StringResource.styleWith(
    onTranslatableTextAppend : AnnotatedString.Builder.(index : Int, phrase : String) -> Unit,
    onAppend: AnnotatedString.Builder.(substring: String) -> Unit = { append(it) },
    vararg phrases : String
) = buildAnnotatedString {
    val arguments = Array(size = phrases.size) { PLACEHOLDER }
    val translatedString = stringResource(resource = this@styleWith,args = arguments)
    var startIndex = 0

    phrases.forEachIndexed { index , phrase ->
        val phraseStartIndex = translatedString.indexOf(string = PLACEHOLDER,startIndex = startIndex)

        // First append the content before the styled phrase
        onAppend(translatedString.substring(startIndex = startIndex, endIndex = phraseStartIndex))

        // Now append the styled phrase
        onTranslatableTextAppend(index,phrase)

        // Finally update the start index
        startIndex = phraseStartIndex + PLACEHOLDER.length
    }

    // Append any remainder of the string
    if(startIndex != translatedString.length)
        append(translatedString.substring(startIndex = startIndex))
}

@Composable
fun StringResource.styleWith(): AnnotatedString = AnnotatedString(stringResource(this@styleWith))

@Composable
fun StringResource.styleWith(spanStyle: SpanStyle): AnnotatedString = buildAnnotatedString {
    withStyle(spanStyle){
        append(stringResource(this@styleWith))
    }
}