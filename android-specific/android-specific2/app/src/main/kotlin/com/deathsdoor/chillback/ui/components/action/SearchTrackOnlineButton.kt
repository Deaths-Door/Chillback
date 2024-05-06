package com.deathsdoor.chillback.ui.components.action

import StackedSnackbarDuration
import android.provider.MediaStore
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.backend.YoutubeException
import com.deathsdoor.chillback.backend.downloadYoutubeVideoAudio
import com.deathsdoor.chillback.backend.searchYoutubeVideo
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.services.CacheWorkManager
import com.deathsdoor.chillback.ui.components.layout.Thumbnail
import com.deathsdoor.chillback.ui.components.layout.ThumbnailTitle
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.spr.jetpack_loading.components.indicators.BallPulseRiseIndicator
import kotlinx.coroutines.launch
import java.nio.file.Paths

@Composable
fun SearchTrackOnlineThumbItem(details: TrackDetails){
    val label = stringResource(R.string.find_song_elsewhere)
    val isOpen = remember { mutableStateOf(false) }

    Thumbnail(
        modifier = Modifier
            .clickable(onClickLabel = label) { isOpen.value = true }
            .optionsItemSpacing(),
        title = { ThumbnailTitle(text = label) },
        artwork = {
            Icon(
                imageVector= Icons.Default.Search,
                contentDescription = null
            )
        }
    )

    if(isOpen.value) SearchTrackOnlineAlertDialog(isOpen = isOpen, details = details)
}

@Composable
fun SearchTrackOnlineDropDownItem(details: TrackDetails?){
    val label = stringResource(R.string.find_song_elsewhere)
    val isOpen = remember { mutableStateOf(false) }

    DropdownMenuItem(
        text = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector= Icons.Default.Search,
                contentDescription = null
            )
        },
        onClick = { isOpen.value = true }
    )

    if(isOpen.value) SearchTrackOnlineAlertDialog(isOpen = isOpen, details = details!!)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTrackOnlineAlertDialog(
    isOpen: MutableState<Boolean>,
    details: TrackDetails
) {
    var isLoading by remember { mutableStateOf(false) }

    var enteredLink by remember { mutableStateOf<String?>(null) }
    var isEnteredLinkError by remember { mutableStateOf(false) }

    var searchForLyricVideo by remember { mutableStateOf(true) }
    var filterMode by remember { mutableStateOf<Boolean?>(null) }
    var scheme by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { isOpen.value = false },
        title = { Text("Find Track On Youtube") },
        text = {
            AnimatedContent(targetState = isLoading) { value ->
                if(value) Row(verticalAlignment = Alignment.CenterVertically) {
                    BallPulseRiseIndicator()
                    Text(text = "Loading..")
                }
                else Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        modifier = Modifier.applyIf(enteredLink == null) {
                             clickable(onClickLabel = "Enter Link") {
                                 enteredLink = ""
                             }
                        },
                        enabled = enteredLink != null,
                        value = enteredLink ?: "",
                        onValueChange = {
                            enteredLink = it
                            isEnteredLinkError = Patterns.WEB_URL.matcher(enteredLink!!).matches()
                        },
                        isError = !enteredLink.isNullOrEmpty() && isEnteredLinkError,
                        singleLine = true,
                        placeholder = { Text("https://www.youtube.com/watch?v=t0Q2otsqC4I") },
                        label = { Text("Enter Youtube Link") },
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = searchForLyricVideo,
                            onCheckedChange = { searchForLyricVideo = it })

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(text = "Search for Lyric Videos")
                    }

                    var isFilterExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = isFilterExpanded,
                        onExpandedChange = { isFilterExpanded = !isFilterExpanded },
                        content = {
                            var filterValue by remember { mutableStateOf("No Filter") }

                            TextField(
                                value = filterValue,
                                onValueChange = { filterValue = it },
                                label = { Text("Filter Mode") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isFilterExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = isFilterExpanded,
                                onDismissRequest = { isFilterExpanded = false },
                                content = {
                                    DropdownMenuItem(
                                        text = {  Text(text = "No Filter") },
                                        onClick = {
                                            filterMode = null
                                            isFilterExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {  Text(text = "Moderate") },
                                        onClick = {
                                            filterMode = true
                                            isFilterExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {  Text(text = "Strict") },
                                        onClick = {
                                            filterMode = false
                                            isFilterExpanded = false
                                        }
                                    )
                                }
                            )
                        }
                    )

                    TextField(
                        value = scheme ?: "",
                        onValueChange = { scheme = it },
                        label = { Text("Enter Proxy Scheme") }
                    )

                    TextField(
                        value = username ?: "",
                        enabled = scheme != null,
                        onValueChange = { username = it },
                        label = { Text("Enter Username for Proxy Authentication") }
                    )

                    TextField(
                        value = password ?: "",
                        enabled = scheme != null,
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = { password = it },
                        label = { Text("Enter Proxy Schema") }
                    )
                }
            }
        },
        confirmButton = {
            val snackbar = LocalSnackbarState.current
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val directoryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocumentTree(),
                onResult = { outputDirectory ->
                    if(outputDirectory == null) return@rememberLauncherForActivityResult

                    isLoading = true

                    val outputFile = Paths.get(
                        outputDirectory.toString(),
                        "${details.name} by ${details.artists} ${if(searchForLyricVideo) "lyrics" else ""}"
                    ).toString()

                    suspend fun download(video : String) {
                        downloadYoutubeVideoAudio(
                            videoUrl = video,
                            outputFile = outputFile,
                            scheme = scheme,
                            username = username,
                            password = password,
                        )

                        snackbar.showSuccessSnackbar(
                            title = "Download Successful",
                            duration = StackedSnackbarDuration.Short
                        )
                    }

                    fun onClick() {
                        coroutineScope.launch {
                            try {
                                when {
                                    enteredLink.isNullOrEmpty() -> {
                                        val videoID = searchYoutubeVideo(
                                            lyrics = searchForLyricVideo,
                                            trackName = details.name,
                                            trackArtist = details.artists ?: "",
                                            filterModerate = filterMode,
                                        )

                                        snackbar.showInfoSnackbar(
                                            title = "Beginning to download song ",
                                            duration = StackedSnackbarDuration.Short
                                        )

                                        download(videoID)
                                    }

                                    else -> download(enteredLink!!)
                                }

                                isLoading = false
                                isOpen.value = false

                                CacheWorkManager.start(context)
                            } catch (exception: YoutubeException) {
                                snackbar.showErrorSnackbar(
                                    title = "Error Occurred",
                                    duration = StackedSnackbarDuration.Long,
                                    description = exception.toString(),
                                    actionTitle = "Retry",
                                    action = { onClick() },
                                )
                            }
                        }
                    }

                    onClick()
                }
            )

            Button(
                content = { Text(text = if(enteredLink == null) "Search and download" else "Download") },
                onClick = { directoryLauncher.launch(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) }
            )
        }
    )
}