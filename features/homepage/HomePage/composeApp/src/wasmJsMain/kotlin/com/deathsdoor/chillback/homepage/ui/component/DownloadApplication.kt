package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun DownloadApplication() = Box(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Download",
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.displayMedium,
        )

        val annotatedString  = buildAnnotatedString {
            // Title with bold and larger font
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Chillback is available for a wide range of devices! ")
            }

            // Text with normal style
            append("You can download pre-built versions for ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Linux, Windows , Android and iOS")
            }
            append(", or grab the app from the app store on your ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Android or iOS")
            }
            append(" device.")

            // Text with normal style
            append(" There's even a web version you can use in your browser. ")

            // Text with italic style for adventurous users
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("If you're feeling adventurous, you can build the app from source or customize it to your liking " +
                        "by following the instructions in the project ")

                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                    // TODO : Link
                    pushStringAnnotation(tag = README_TAG, annotation = "Link")
                    append("README.")
                    pop()
                }
            }
        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            modifier = Modifier.padding(top = 16.dp,bottom = 24.dp),
            text = annotatedString,
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.inverseSurface),
            onClick = { offset ->
                annotatedString
                    .getStringAnnotations(tag = README_TAG,start = offset,end = offset)
                    .firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
            },
        )

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp),) {
            listOf("Windows","Linux" ,"Android","iOS").forEach {
                OutlinedButton(
                    content = { Text(it) },
                    onClick = { /*TODO : Download */ }
                )
            }
        }
    }
}

private val README_TAG = "readme"