package com.deathsdoor.chillback.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.footer_made_by
import com.deathsdoor.chillback.core.layout.extensions.asAnnotatedStringWithSpanStyle
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun Footer(modifier : Modifier = Modifier) {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.year
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.footer_made_by).asAnnotatedStringWithSpanStyle(
                substring = "Aarav Shah",
                style = SpanStyle(color = colorScheme.primary)
            ),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Copyright @${currentYear} Aarav Shah",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}