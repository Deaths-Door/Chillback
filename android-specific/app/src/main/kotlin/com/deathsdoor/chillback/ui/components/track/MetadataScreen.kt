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
import androidx.compose.runtime.LaunchedEffect
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
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.data.media.MediaMetadataExtractor
import com.deathsdoor.chillback.data.models.MetadataCategory
import com.deathsdoor.chillback.data.models.Track
import com.deathsdoor.chillback.data.models.TrackMetadata
import com.deathsdoor.chillback.data.models.TrackMetadataInputType
import com.deathsdoor.chillback.ui.components.action.SortFilterButton
import com.deathsdoor.chillback.ui.components.action.createCompartorFrom
import com.deathsdoor.chillback.ui.components.layout.CollapsableScaffold
import com.deathsdoor.chillback.ui.components.layout.LabeledCheckBox
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.deathsdoor.chillback.ui.providers.LocalErrorSnackbarState
import com.deathsdoor.chillback.ui.state.MetadataEditScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jaudiotagger.tag.FieldKey

@Composable
fun TrackMetadataScreen(track : Track) {
    val appState = LocalAppState.current
    val errorSnackbarState = LocalErrorSnackbarState.current
    // Since this screen is only navigable , when we have the details , so it should be non-null
    // However , there maybe be a delay between the object being placed in cache , and accessing it , hence mutableStateOf to fetch it
    var trackDetails by remember { mutableStateOf(appState.musicRepository.trackDetailsOrNull(track)) }

    if(trackDetails == null) {
        LaunchedEffect(Unit) {
            while(true) {
                val newValue = appState.musicRepository.trackDetailsOrNull(track)
                if(newValue != null){
                    trackDetails = newValue
                    break
                }
                else delay(500L)
            }
        }
    }


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

            editModeState.copyIntoTrackDetails(trackDetails = trackDetails!!)?.let {
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
                    uri = trackDetails?.artwork,
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
                                        // TODO : Fix this
                                        trackMetadata = editModeState.metadataFields.find { it.value.fieldKey == FieldKey.TITLE }?.value ?: editModeState.metadataFields[0].value
                                    )
                                }
                                false -> {
                                    Text(
                                        text = trackDetails?.name ?: "Wait a moment..",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                    )

                                    Text(
                                        text = trackDetails?.artists ?: "No Artists",
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
                    // TODO: add a search option , and maybe add a fill recommendation option here as well

                    val criteria = mutableListOf(
                        stringResource(R.string.sort_by_frequency_of_usage),
                        stringResource(R.string.sort_by_only_single_value)
                    )
                    criteria.addAll(MetadataCategory.values().map { stringResource(it.stringId) })

                    val enabled = mutableStateOf(false)
                    SortFilterButton(
                        coroutineScope = coroutineScope,
                        enabled = enabled,
                        criteria = criteria,
                        fetch = {
                            editModeState.metadataFields.any {
                                !it.isInitialized()
                                        || (it.value is TrackMetadata.SingleValue && (it.value as TrackMetadata.SingleValue).defaultValue == null)
                                        || (it.value is TrackMetadata.MultipleValues && !(it.value as TrackMetadata.MultipleValues).readInitialDefaultValue)
                            }
                        },
                        onFetch = {
                            val tag = TrackMetadata.createTag(track = track)

                           editModeState.metadataFields.forEach {
                                it.value.readTag(tag = tag)
                           }
                        },
                        onSort = { isAscending : Boolean,appliedMethods : List<Int> ->
                            editModeState.metadataFields.sortedWith(
                                createCompartorFrom(
                                    isAscending = isAscending,
                                    sortMethods = appliedMethods,
                                    create = { _it , method ->
                                        val field = _it.value
                                        when(method) {
                                            0 -> field.frequencyOfUsage
                                            1 -> field is TrackMetadata.SingleValue
                                            else -> field.category === MetadataCategory.values()[method - 2]
                                        }
                                    }
                                )
                            )
                        }
                    )
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
                            LaunchedEffect(Unit) {
                               trackMetadata.readTag(tag = TrackMetadata.createTag(track = track))
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
                            LaunchedEffect(Unit) {
                                trackMetadata.readTag(tag = TrackMetadata.createTag(track = track))
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
                                        trackMetadata = trackMetadata
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