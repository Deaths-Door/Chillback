package com.deathsdoor.chillback.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import com.deathsdoor.chillback.welcome.components.DownloadApplication
import com.deathsdoor.chillback.welcome.components.ExternalReviewCards
import com.deathsdoor.chillback.welcome.components.ExternalReviewTitle
import com.deathsdoor.chillback.welcome.components.Features
import com.deathsdoor.chillback.welcome.components.Footer
import com.deathsdoor.chillback.welcome.components.MainContent
import com.deathsdoor.chillback.welcome.components.StickyHeader
import com.deathsdoor.chillback.welcome.components.SubscribeToNewsLetter
import com.deathsdoor.chillback.welcome.components.icons.Waves

/// The theming will be initialized by the main application
@Composable
fun Homepage() = Surface {
    val windowSize = LocalWindowSize.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        val scrollState = rememberLazyListState()

        val isFirstItemVisible by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex == 0
            }
        }

        if(isFirstItemVisible) Image(
            modifier = Modifier.align(Alignment.TopEnd).offset(x = 100.dp),
            imageVector = Icons.Waves,
            contentDescription = null
        )

        val fillMaxWidth90 = Modifier.fillMaxWidth(0.9f)
        val headerHeight = 72.dp

        LazyColumn(
            modifier = fillMaxWidth90.padding(top = headerHeight).fillMaxHeight(),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(72.dp),
            content =  {
                item {
                    MainContent(headerHeight = headerHeight,scrollState = scrollState)
                }
                item {
                    Features()
                }
                item {
                    HorizontalDivider(thickness = 2.5.dp)
                }
                item {
                    DownloadApplication()
                }
                item {
                    HorizontalDivider(thickness = 2.5.dp)
                }
                item {
                    ExternalReviewTitle()
                }
                item {
                    ExternalReviewCards()
                }
                item {
                    SubscribeToNewsLetter()
                }
                item {
                    HorizontalDivider(thickness = 2.5.dp)

                    Footer(modifier = Modifier.padding(vertical = 32.dp))
                }
            }
        )

        // This is at the end so that the divider is over the page content
        Column(modifier = Modifier.heightIn(max = headerHeight).align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
            StickyHeader(modifier = fillMaxWidth90)

            val isPartiallyScrolled by remember {
                derivedStateOf {
                    val layoutInfo = scrollState.layoutInfo
                    val firstItem = layoutInfo.visibleItemsInfo.firstOrNull() ?: return@derivedStateOf false
                    firstItem.offset < layoutInfo.viewportStartOffset
                }
            }

            if(isPartiallyScrolled) HorizontalDivider()
        }

        if(windowSize.widthSizeClass != WindowWidthSizeClass.Compact) {
            val inverseSurface = MaterialTheme.colorScheme.inverseSurface

            val scrollBarStyle =  LocalScrollbarStyle.current.copy(
                shape = CircleShape,
                unhoverColor = inverseSurface.copy(alpha = 0.12f),
                hoverColor = inverseSurface.copy(alpha = 0.50f)
            )

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).padding(top = 12.dp,bottom = 12.dp,end = 8.dp),
                adapter = rememberScrollbarAdapter(scrollState),
                style = scrollBarStyle
            )
        }
    }
}