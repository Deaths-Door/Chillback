package com.deathsdoor.chillback.ui.components.action

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewModelScope
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DeleteThumbItem(label : String,name : String,onDelete : (coroutineScope: CoroutineScope) -> Unit) {
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
    onDelete : (coroutineScope: CoroutineScope) -> Unit
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
    onDelete : (coroutineScope: CoroutineScope) -> Unit,
    isOpen : MutableState<Boolean>
) = AlertDialog(
    onDismissRequest = { isOpen.value = false },
    title = { Text("$label ?") },
    text = {
        Text(
            text = buildAnnotatedString {
                val bold = SpanStyle(fontWeight = FontWeight.Bold)
                withStyle(style = bold) {
                    append("Are you sure ")
                }

                append("that you want to ")

                withStyle(style = bold + SpanStyle(color = MaterialTheme.colorScheme.error)) {
                    append("${label.lowercase()} $name ?")
                }

                withStyle(style = bold) {
                    append("\nThis action is irreversible!")
                }
            }
        )
    },
    confirmButton = {
        // As this should be updated even if the screen is removed
        val coroutineScope = LocalAppState.current.viewModelScope

        TextButton(
            onClick = { onDelete(coroutineScope) },
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


@Deprecated("",level = DeprecationLevel.ERROR)
@Composable
fun DeleteTrackCollectionThumbItem(trackCollection: TrackCollection){
    var isOpen by remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable { isOpen = true }
            .optionsItemSpacing(),
        title = "Delete Collection",
        artwork = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    )

    if(!isOpen) return

    AlertDialog(
        onDismissRequest = { isOpen = false },
        title = { Text("Delete Collection?") },
        text = {
            Text(
                text = buildAnnotatedString {
                    val bold = SpanStyle(fontWeight = FontWeight.Bold)
                    withStyle(style = bold) {
                        append("Are you sure ")
                    }

                    append("that you want to ")

                    withStyle(style = bold + SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        append("delete ${trackCollection.name}?")
                    }

                    withStyle(style = bold) {
                        append("\nThis action is irreversible!")
                    }
                }
            )
        },
        confirmButton = {
            // As this should be updated even if the screen is removed
            val coroutineScope = LocalAppState.current.viewModelScope
            val userRepository = LocalAppState.current.userRepository

            TextButton(
                onClick = {
                    coroutineScope.launch {
                        userRepository.deleteTrackCollection(trackCollection)
                    }
                },
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
                onClick = { isOpen = false },
                content = { Text(text = "Cancel") }
            )
        }
    )
}
