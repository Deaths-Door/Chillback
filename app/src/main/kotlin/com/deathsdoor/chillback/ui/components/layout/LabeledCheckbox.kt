package com.deathsdoor.chillback.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun LabeledCheckBox(
    checked : Boolean,
    label : String,
    onClick: (() -> Unit)? = null
) {

    val modifier : Modifier = if(onClick != null) Modifier.fillMaxWidth().clickable(
        role = Role.Checkbox,
        onClick = onClick
    ) else Modifier.fillMaxWidth()

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = label)
        }
    )
}