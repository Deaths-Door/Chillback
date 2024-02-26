package com.deathsdoor.chillback.ui.components.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.models.TrackCollection
import com.deathsdoor.chillback.data.models.TrackCollectionWithTracks


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LazyTrackCollectionList(
    modifier: Modifier = Modifier,
    collections : List<TrackCollectionWithTracks>?,
    placeHolder : @Composable () -> Unit,
    onPinChange : (TrackCollection) -> Unit,
    onDelete : (TrackCollection) -> Unit
) {

}
// TODO : EITHER DELETE THIS FILE < OR REIMPL IT WITH NEW APIS
/*@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LazyTrackCollectionList(
    modifier: Modifier = Modifier,
    collections : List<TrackCollectionWithTracks>?,
    placeHolder : @Composable () -> Unit,
    onPinChange : (TrackCollection) -> Unit,
    onDelete : (TrackCollection) -> Unit
) {
    // TODO : NOT UPDATING WHEN COLLECTION CHANGES -> condition
    if (collections.isNullOrEmpty()) {
        placeHolder()
        return
    }

    val modalOptionsState = rememberModalOptionsState(skipPartiallyExpanded = true)

    var itemIndex: Int? by remember { mutableStateOf(null) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            itemsIndexed(collections) { index , _it ->
                val currentItem by rememberUpdatedState(_it)

                val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
                    if (dismissValue.isDelete) {
                        onDelete(currentItem.collection)
                        return@rememberDismissState true
                    }

                    if (dismissValue == DismissValue.DismissedToEnd) onPinChange(currentItem.collection)

                    false
                })

                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        if (dismissState.targetValue == DismissValue.Default) return@SwipeToDismiss

                        val color = when (dismissState.isDelete) {
                            true -> MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                            false -> MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f)
                        }

                        val backgroundColor by animateColorAsState(color)

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor)
                                .padding(horizontal = 16.dp),
                            contentAlignment = if (dismissState.isDelete) Alignment.CenterEnd else Alignment.CenterStart,
                            content = {
                                when (dismissState.isDelete) {
                                    true -> Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = themeBasedTint()
                                    )

                                    false -> TrackCollectionItemPushPinImage(isPinned = currentItem.collection.isPinned)
                                }
                            }
                        )
                    },
                    dismissContent = {
                        TrackCollectionItem(
                            trackCollection = currentItem,
                            modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .combinedClickable(
                                    role = Role.Button,
                                    onClick = { /*TODO : NAVIGATE TO THIS */ },
                                    onClickLabel = "Navigate to see tracks in songs",
                                    onLongClickLabel = "Show collections options",
                                    onLongClick = {
                                        itemIndex = index
                                        modalOptionsState.show()
                                    }
                                ),
                        )
                    }
                )
            }
        }
    )

    itemIndex?.let {
        /*val trackCollectionItem = collections[it]

        TrackCollectionExtraOptions(
            state = modalOptionsState,
            trackCollection = trackCollectionItem,

        )*/
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val DismissValue.isDelete get() = this == DismissValue.DismissedToStart
@OptIn(ExperimentalMaterial3Api::class)
private val DismissState.isDelete get() = targetValue.isDelete*/