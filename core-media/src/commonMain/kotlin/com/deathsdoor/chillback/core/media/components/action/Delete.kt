package com.deathsdoor.chillback.core.media.components.action

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.deathsdoor.chillback.core.layout.Thumbnail
import com.deathsdoor.chillback.core.layout.ThumbnailTitle
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.core.media.extensions.optionsItemSpacing
import com.deathsdoor.chillback.core.media.resources.Res

@Composable
fun DeleteThumbItem(label : String,name : String,onDelete : () -> Unit) {
    val isOpen = remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable { isOpen.value = true }
            .optionsItemSpacing(),
        title = { ThumbnailTitle(text = label) },
        artwork = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    )

    if(!isOpen.value) return

    DeleteConfirmationAlertDialog(label = label, name = name, onDelete = onDelete, isOpen = isOpen)
}


@Composable
fun DeleteDropDownItem(
    label : String,
    name : () -> String,
    enabled : Boolean,
    onDelete : () -> Unit
) {
    val isOpen = remember { mutableStateOf(false) }

    DropdownMenuItem(
        text = { Text(text = label) },
        onClick = { isOpen.value = true },
        enabled = enabled,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    )

    if(!isOpen.value) return

    DeleteConfirmationAlertDialog(label = label, name = name(), onDelete = onDelete, isOpen = isOpen)
}

@Composable
private fun DeleteConfirmationAlertDialog(
    label : String,
    name : String,
    onDelete : () -> Unit,
    isOpen : MutableState<Boolean>
) = AlertDialog(
    onDismissRequest = { isOpen.value = false },
    title = { Text("$label ?") },
    text = {
        val primary = MaterialTheme.colorScheme.error
        val bold = SpanStyle(fontWeight = FontWeight.Bold)
        
        Text(
            text = Res.strings.delete_confirmation.styleWith(
                onTranslatableTextAppend = { _, phrase ->
                    withStyle(style = bold + SpanStyle(color = primary)) {
                        append(phrase)
                    }
                },
                onAppend = {
                    when {
                        it.contains('!') -> {
                            withStyle(style = bold) {
                                append(it)
                            }
                        }
                        else -> {
                            append(it)
                        }
                    }
                },
                phrases = arrayOf("${label.lowercase()} $name")
            )
        )
    },
    confirmButton = {
        TextButton(
            onClick = { onDelete() },
            content = {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
    },
    dismissButton = {
        Button(
            onClick = { isOpen.value = false },
            content = { Text(text = "Cancel") }
        )
    }
)