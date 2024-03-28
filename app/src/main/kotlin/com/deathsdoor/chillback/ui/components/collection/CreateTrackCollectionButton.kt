package com.deathsdoor.chillback.ui.components.collection

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import kotlinx.coroutines.launch

// TODO : ADD M3U/M3U8 SUPPORT HERE -> IMPORT M3U -> GO TO ADD SONGS TO PLAYLIST SCREEN
@Composable
fun CreateTrackCollectionButton(iconSize : Dp = 32.dp) {
    var isOpen by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isOpen = true },
        content = {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = Icons.Default.Add,
                contentDescription = "Create New Playlist"
            )
        }
    )

    if(!isOpen) return

    val userRepository = LocalAppState.current.userRepository

    var name by remember { mutableStateOf("") }
    val isError by remember(name) { mutableStateOf(name.isEmpty()) }

    val coroutineScope = rememberCoroutineScope()
    val errorSnackbar = LocalErrorSnackbarState.current

    var imageSource by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberImagePickerLauncher { imageSource = it }

    // TODO : CHANGE THIS -> REMOVE IMAGE PICKER -> GO TO ADD SONGS TO PLAYLIST SCREEN
    AlertDialog(
        onDismissRequest = { isOpen = false },
        confirmButton = {
            Button(
                onClick = {
                    if(isError) {
                        coroutineScope.launch {
                            errorSnackbar.showSnackbar("Can't create playlist with no name")
                        }
                        return@Button
                    }

                    isOpen = false

                    val collection = TrackCollection(name = name, imageSource = imageSource)
                    val withTracks = TrackCollectionWithTracks(collection = collection)
                    userRepository.createTrackCollection(withTracks)
                },
                content = { Text("Create") }
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Enter Name") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.playback_queue),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } ,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    isError = isError,
                    singleLine = true
                )

                imageSource?.let {
                    Card(modifier = Modifier.fillMaxWidth().padding(16.dp).heightIn(max = 200.dp)) {
                        AsyncImage(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            model = imageSource,
                            contentDescription = "Cover Image",
                        )
                    }
                }

                TextButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { imagePickerLauncher.launchSingleImageSelector() },
                    content = { Text("Select Cover Image") }
                )
            }
        }
    )
}

@Composable
private fun rememberImagePickerLauncher(onReceived : (Uri) -> Unit): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if(it == null) return@rememberLauncherForActivityResult
        context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        onReceived(it)
    }
}

private fun ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>.launchSingleImageSelector() = launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))