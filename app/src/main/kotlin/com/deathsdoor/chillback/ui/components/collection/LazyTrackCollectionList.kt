package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.deathsdoor.chillback.data.models.TrackCollection
import kotlinx.coroutines.CoroutineScope

// TODO: Make sure that pinned playlists for always at the top
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyTrackCollectionList(
    modifier : Modifier = Modifier,
    collections : List<TrackCollection>?,
    coroutineScope: CoroutineScope,
    placeHolderText : (@Composable () -> AnnotatedString)? = null,
    onPinChange : (TrackCollection) -> Unit,
    onDelete : (TrackCollection) -> Unit
) = Column(modifier) {
    // TODO : Enable this again later
/*
    val isSingleItemPerRow = rememberIsSingleItemRow()

    // TODO : ADD Option to sort paylists by track children / "Number of tracks",
    LazyOptionsRow(
        coroutineScope = coroutineScope,
        isSingleItemPerRow = isSingleItemPerRow,
        data = collections,
        criteria = listOf("Name","Is pinned"),
        // TODO : Correct this later on
        fetch = { false },
        onFetch = {},
        onSort = { a,b -> }
    )

    LazyDismissibleList(
        items = collections,
        key = { _ , collection -> collection.id },
        isSingleItemPerRow = isSingleItemPerRow.value,
        confirmValueChange = { dismissValue , _ , collection ->
            if (dismissValue.isDelete) {
                onDelete(collection)
                return@LazyDismissibleList true
            }

            if (dismissValue == DismissValue.DismissedToEnd) onPinChange(collection)

            false
        },
        endToStartColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
        startToEndColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
        swipeableContent = { isStartToEnd , collection ->
            if (isStartToEnd) TrackCollectionItemPushPinImage(isPinned = collection.isPinned)
            else Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
            )
        },
        placeHolder = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    Text(
                        // TODO : CREATE TEXT HERE
                        text = if(placeHolderText != null) placeHolderText() else TODO(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // TODO : Add add tracks to playlist button here , only if user defined == false
                }
            )
        },
        optionContent = { state , collection -> TODO("Uncomment and update implementation of TrackCollectionExtraOptions")/*TrackCollectionExtraOptions(state,)*/ },
        content = { index , collection , isSelected , onLongClick ->
            TrackCollectionItem(
                collection = collection,
                isSingleItemPerRow = isSingleItemPerRow.value,
                modifier = Modifier
                    .combinedClickable(
                        onClick = { /*TODO : NAVIGATE TO APPORIATE SCREEN */ },
                        onClickLabel = "Navigate to see tracks in songs",
                        onLongClickLabel = "Show collections options",
                        onLongClick = onLongClick
                    ),
            )
        }
    )*/
}

@OptIn(ExperimentalMaterial3Api::class)
private val DismissValue.isDelete get() = this == DismissValue.DismissedToStart