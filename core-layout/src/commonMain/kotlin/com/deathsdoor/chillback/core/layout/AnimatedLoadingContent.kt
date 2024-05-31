package com.deathsdoor.chillback.core.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.spr.jetpack_loading.components.indicators.BallPulseRiseIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SpellCheckingInspection")
@Composable
@NonRestartableComposable
fun AnimatedUndismissibleLoadingContent(label : String) = BasicAlertDialog(
    onDismissRequest = {  },
    properties = DialogProperties(usePlatformDefaultWidth = false),
    content = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BallPulseRiseIndicator(color = MaterialTheme.colorScheme.inverseSurface)
            Text(text = label)
        }
    }
)