package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import com.deathsdoor.chillback.homepage.utils.asAnnotatedStringWith
import com.deathsdoor.chillback.homepage.utils.asAnnotatedStringWithSpanStyle
import homepage.composeapp.generated.resources.Res
import homepage.composeapp.generated.resources.and
import homepage.composeapp.generated.resources.download
import homepage.composeapp.generated.resources.download_sentence1
import homepage.composeapp.generated.resources.download_sentence2
import homepage.composeapp.generated.resources.download_sentence2_phrase1
import homepage.composeapp.generated.resources.download_sentence2_phrase2
import homepage.composeapp.generated.resources.download_sentence3
import homepage.composeapp.generated.resources.or
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DownloadApplication() = Box(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.download),
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.displayMedium,
        )

        val annotatedString  = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(Res.string.download_sentence1))
            }

            val and = stringResource(Res.string.and)
            val or = stringResource(Res.string.or)

            val allPlatforms = "Linux, Windows , Android $and iOS"
            val phrase1 = stringResource(Res.string.download_sentence2_phrase1,allPlatforms)
                .asAnnotatedStringWithSpanStyle(
                    substring = allPlatforms,
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                )

            val mobileDevices = "Android $or iOS"
            val phrase2 = stringResource(Res.string.download_sentence2_phrase2,mobileDevices)
                .asAnnotatedStringWithSpanStyle(
                    substring = mobileDevices,
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                )

            append(
                phrase1,
                phrase2,
                stringResource(Res.string.download_sentence2)
            )

            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                val readme = "README."
                stringResource(Res.string.download_sentence3,readme).asAnnotatedStringWith(readme) { section ->
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                        // TODO : Link
                        pushStringAnnotation(tag = README_TAG, annotation = "Link")
                        append(readme)
                        pop()
                    }
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