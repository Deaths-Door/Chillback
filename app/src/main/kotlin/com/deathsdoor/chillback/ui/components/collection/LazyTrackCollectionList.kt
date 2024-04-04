package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.deathsdoor.chillback.data.media.TrackCollectionRepository
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.ui.components.action.LazyOptionsRow
import com.deathsdoor.chillback.ui.components.action.rememberIsSingleItemRow
import com.deathsdoor.chillback.ui.components.layout.LazyDismissibleSelectableList
import com.deathsdoor.chillback.ui.extensions.applyIf
import com.deathsdoor.chillback.ui.extensions.styledText
import com.deathsdoor.chillback.ui.navigation.navigateToTrackCollectionScreen
import com.deathsdoor.chillback.ui.providers.LocalAppState
import com.dragselectcompose.core.rememberDragSelectState
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyTrackCollectionList(
    modifier : Modifier = Modifier,
    coroutineScope: CoroutineScope,
    collections : List<TrackCollection>?,
    onClickLabel : String = "Open Collection",
    onClick : (TrackCollection) -> TrackCollectionRepository,
    onPinChange : (TrackCollection) -> Unit,
    onDelete : (TrackCollection) -> Unit,
    placeHolderText : @Composable () -> AnnotatedString = {
        styledText(
            plain0 = "There are no playlists to display yet.\n",
            colored0 = "Explore",
            plain1 = " and discover some music ",
            colored1 = "to fill your library!"
        )
    },

    placeHolderContent: (@Composable ColumnScope.() -> Unit)? = null
) = Column(modifier = modifier) {
    val isSingleItemPerRow = rememberIsSingleItemRow()
    val draggableState = rememberDragSelectState<TrackCollection>()

    // TODO : ADD Option to sort paylists by track children / "Number of tracks",
    LazyOptionsRow(
        coroutineScope = coroutineScope,
        draggableState = draggableState,
        isSingleItemPerRow = isSingleItemPerRow,
        data = collections,
        criteria = listOf("Name","Is pinned"),
        // TODO : Correct this later on
        fetch = { false },
        onFetch = {},
        onSort = { a,b -> },
    )

    val appState = LocalAppState.current

    LazyDismissibleSelectableList(
        coroutineScope = coroutineScope,
        items = collections,
        key = { it.id },
        isSingleItemPerRow = isSingleItemPerRow.value,
        endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
        startToEndColor = Color(0xFFCCE5D6),
        swipeableContent = { isStartToEnd , collection ->
            if (isStartToEnd) TrackCollectionItemPushPinImage(isPinned = collection.isPinned)
            else Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
            )
        },
        confirmValueChange = { dismissValue , collection ->
            when(dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete(collection)
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onPinChange(collection)
                    false
                }
                else -> false
            }
        },
        optionContent = { _ , state , collection -> TODO("Uncomment and update implementation of TrackCollectionExtraOptions")/*TrackCollectionExtraOptions(state,)*/ },
        placeHolder = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    Text(
                        text = placeHolderText(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    placeHolderContent?.invoke(this)
                }
            )
        },
        content = { contentModifier, item, isSelected, onLongClick ->
            @Suppress("NAME_SHADOWING")
            val contentModifier = contentModifier.applyIf(isSelected != null) {
                combinedClickable(
                    onClick = {
                        appState.navigateToTrackCollectionScreen(repository = onClick(item))
                    },
                    onClickLabel = onClickLabel,
                    onLongClick = onLongClick,
                    onLongClickLabel = "Show extra options for collection",
                )
            }

            if(isSingleItemPerRow.value) TrackCollectionRowItem(
                modifier = contentModifier,
                collection = item,
                isSelected = isSelected,
                draggableState = draggableState
            ) else TrackCollectionCard(
                modifier = contentModifier,
                collection = item,
                isSelected = isSelected,
                draggableState = draggableState
            )
        }
    )
}