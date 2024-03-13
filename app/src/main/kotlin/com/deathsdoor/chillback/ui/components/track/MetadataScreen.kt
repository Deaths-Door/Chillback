package com.deathsdoor.chillback.ui.components.track

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deathsdoor.chillback.data.media.MediaMetadataExtractor
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackDetails
import com.deathsdoor.chillback.data.models.TrackMetadata
import com.deathsdoor.chillback.data.models.TrackMetadataInputType
import com.deathsdoor.chillback.ui.components.layout.CollapsableScaffold
import com.deathsdoor.chillback.ui.components.layout.LabeledCheckBox
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.state.MetadataEditScreenState
import kotlinx.coroutines.launch
import org.jaudiotagger.tag.FieldKey

@Composable
fun TrackMetadataScreen(track : Track) {
    val appState = LocalAppState.current
    val errorSnackbarState = LocalErrorSnackbarState.current
    // TODO : Remove his for some reason its nullable
    // Since this screen is only navigable , when we have the details
    val trackDetails = appState.musicRepository.trackDetailsOrNull(track) ?: TrackDetails("NAME",null,"ARTISTs","GENRE","ALBUM","ALBUM ARTISTS")

    val coroutineScope = rememberCoroutineScope()
    var inEditMode  by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }

    val editModeState = viewModel<MetadataEditScreenState>()

    val onSave = {
        // Since this should be updated even if screen is closed
        appState.viewModelScope.launch {
            val audioFile = MediaMetadataExtractor.audioFileFrom(track.sourcePath)
            val tag = audioFile.tagOrCreateDefault
            editModeState.updateMetadata(
                snackBarState = errorSnackbarState,
                coroutineScope = coroutineScope,
                tag = tag
            )
            audioFile.commit()

            if(!editModeState.anyFieldChanged) return@launch

            editModeState.copyIntoTrackDetails(trackDetails = trackDetails)?.let {
                appState.musicRepository.updateDetailsCache(track = track, details = it)
            }
        }

        Unit
    }

    val imageVector : ImageVector
    val contentDescription : String
    val onModeIconClick : () -> Unit
    if(inEditMode) {
        imageVector = Icons.Default.Done
        contentDescription = "Save Metadata"
        onModeIconClick = onSave
    }
    else {
        imageVector = Icons.Default.Edit
        contentDescription = "Edit Metadata"
        onModeIconClick = { inEditMode = true }
    }

    CollapsableScaffold(
        label = "Track Metadata",
        onBack = {
            // Has Changed any of the fields
            if(editModeState.anyFieldChanged) isDialogOpen = true
            else appState.navController.popBackStack()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = if(!inEditMode) {{ inEditMode = false }} else onSave,
                content = {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = contentDescription
                    )
                }
            )
        },
        headerContent = { modifier  ->
            Box(modifier = modifier.fillMaxWidth()) {
                Artwork(
                    uri = trackDetails.artwork,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )

                val sixteen = 16.dp

                Row(
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = sixteen,bottom = sixteen,end = sixteen).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Column(Modifier.weight(2f)) {
                            when(inEditMode) {
                                true -> {
                                    TrackMetadataInputType.TextWithNoRecommendation.InputField(
                                        editModeState = editModeState,
                                        trackMetadata = editModeState.metadataFields.find { it.value.fieldKey == FieldKey.TITLE }!!.value
                                    )
                                }
                                false -> {
                                    Text(
                                        text = trackDetails.name,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                    )

                                    Text(
                                        text = trackDetails.artists ?: "No Artists",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = onModeIconClick,
                            content = {
                                Icon(
                                    imageVector = imageVector,
                                    contentDescription = contentDescription
                                )
                            }
                        )
                    }
                )
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Row(modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()) {
                    // TODO: Finish this sorting bar , add a search option , and maybe add a fill recommendation option here as well
                    /*SortFilterButton(
                        coroutineScope = coroutineScope,

                    )*/
                }
            }

            val state = rememberLazyListState()

            val twelve = 12f.dp
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                state = state,
                verticalArrangement = Arrangement.spacedBy(twelve),
                content = {
                    val cardModifier = Modifier.padding(horizontal = twelve).fillMaxWidth()
                    val titleModifier = Modifier.padding(start = twelve,top = twelve,bottom = twelve)
                    val valueModifier = Modifier.padding(start = twelve,bottom = (twelve.value + 4f).dp)

                    items(editModeState.metadataFields) { _it ->
                        val trackMetadata = _it.value

                        if(trackMetadata is TrackMetadata.SingleValue && trackMetadata.defaultValue == null) {
                            trackMetadata.ReadTag(track = track) {
                                trackMetadata.defaultValue = it.getFirst(trackMetadata.fieldKey)
                                trackMetadata.currentValue.value = trackMetadata.defaultValue
                            }

                            Text(
                                modifier = titleModifier,
                                text = TrackMetadata.defaultValueLoading,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            return@items
                        }

                        if(trackMetadata is TrackMetadata.MultipleValues && !trackMetadata.readInitialDefaultValue) {
                            trackMetadata.ReadTag(track = track) {
                                trackMetadata.readInitialDefaultValue = true
                                val tagValues = it.getAll(trackMetadata.fieldKey)
                                trackMetadata.defaultValue.addAll(tagValues)
                                trackMetadata.currentValue.addAll(tagValues)

                            }

                            Text(
                                modifier = titleModifier,
                                text = TrackMetadata.defaultValueLoading,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            return@items
                        }

                        AnimatedContent(inEditMode) { mode ->
                            when (mode) {
                                false -> Card(modifier = cardModifier) {
                                    Text(
                                        modifier = titleModifier,
                                        text = stringResource(id = trackMetadata.stringId),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    if(trackMetadata.inputType is TrackMetadataInputType.CheckBoxWithNoRecommendation) Card(modifier = cardModifier) {
                                        trackMetadata as TrackMetadata.SingleValue

                                        LabeledCheckBox(
                                            checked = trackMetadata.currentValue.value != null,
                                            label = stringResource(trackMetadata.stringId),
                                            onClick = null
                                        )

                                    } else trackMetadata.ReadOnlyDefaultValue(modifier = valueModifier)
                                }
                                true -> when (trackMetadata.inputType) {
                                    TrackMetadataInputType.TextWithNoRecommendation -> TrackMetadataInputType.TextWithNoRecommendation.InputField(
                                        editModeState = editModeState,
                                        trackMetadata = trackMetadata as TrackMetadata.SingleValue
                                    )

                                    TrackMetadataInputType.DateWithNoRecommendation -> TrackMetadataInputType.DateWithNoRecommendation.InputField(
                                        modifier = cardModifier,
                                        editModeState = editModeState,
                                        trackMetadata = trackMetadata as TrackMetadata.SingleValue
                                    )

                                    TrackMetadataInputType.CheckBoxWithNoRecommendation -> TrackMetadataInputType.CheckBoxWithNoRecommendation.InputField(
                                        modifier = cardModifier,
                                        editModeState = editModeState,
                                        trackMetadata = trackMetadata as TrackMetadata.SingleValue
                                    )

                                    TrackMetadataInputType.IntegerWithNoRecommendation -> TrackMetadataInputType.IntegerWithNoRecommendation.InputField(
                                        editModeState = editModeState,
                                        trackMetadata = trackMetadata as TrackMetadata.SingleValue
                                    )

                                    is TrackMetadataInputType.WebsiteWithNoRecommendation -> trackMetadata.inputType.InputField(
                                        editModeState = editModeState,
                                        trackMetadata = trackMetadata as TrackMetadata.SingleValue
                                    )
                                }
                            }
                        }
                    }
                }
            )

            if(!isDialogOpen) return@CollapsableScaffold

            AlertDialog(
                onDismissRequest = { isDialogOpen = false },
                title = { Text("Exit - Edits Will Be Lost!") },
                text = {
                    Text(
                        text = buildAnnotatedString {
                            val bold = SpanStyle(fontWeight = FontWeight.Bold)
                            withStyle(style = bold) {
                                append("Are you sure ")
                            }

                            append("that you want to ")

                            withStyle(style = bold + SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append("exit?\n All your edits will be lost.")
                            }

                            withStyle(style = bold) {
                                append("\nThis action is irreversible!")
                            }
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { appState.navController.popBackStack() },
                        content = {
                            Text(
                                text = "Exit",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                },
                dismissButton = {
                    Button(
                        onClick = { isDialogOpen = false },
                        content = { Text(text = "Cancel") }
                    )
                }
            )
        }
    )
}