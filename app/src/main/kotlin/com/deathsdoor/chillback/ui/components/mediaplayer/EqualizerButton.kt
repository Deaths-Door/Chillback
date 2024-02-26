package com.deathsdoor.chillback.ui.components.mediaplayer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deathsdoor.chillback.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerButton(
    modifier : Modifier = Modifier,
    //coroutineScope: CoroutineScope
) {
    /*val sheetState = rememberModalOptionsState(
        coroutineScope = coroutineScope,
        skipPartiallyExpanded = true
    )*/

    IconButton(
        modifier = modifier,
        onClick = { /*sheetState.show()*/ },
        content = {
            Icon(
                painter = painterResource(R.drawable.ic_equalizer),
                contentDescription = "Open Equalizer Settings"
            )
        }
    )

    /*ModalOptions(sheetState, dragHandle = null) {
        // TODO : Create Equalizer Screen , maybe navigate to it idk
        Text("text",modifier = Modifier.fillMaxSize().background(Color.Red))
    }*/
}