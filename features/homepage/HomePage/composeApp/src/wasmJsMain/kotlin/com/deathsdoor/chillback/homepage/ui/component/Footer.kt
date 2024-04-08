package com.deathsdoor.chillback.homepage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun Footer(modifier : Modifier = Modifier) {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.year
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = buildAnnotatedString {
                append("A labour of love built by ")

                withStyle(SpanStyle(color = colorScheme.primary)) {
                    append("Aarav Shah")
                }
            },
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Copyright @${currentYear} Aarav Shah",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}