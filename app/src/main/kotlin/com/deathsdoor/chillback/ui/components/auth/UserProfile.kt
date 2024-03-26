package com.deathsdoor.chillback.ui.components.auth

import Dropdown
import Easing
import EnterAnimation
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.androidpoet.dropdown.ExitAnimation
import com.deathsdoor.chillback.ui.components.track.ArtworkWithFailureInformer
import com.deathsdoor.chillback.ui.navigation.forEachCategory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dropDownMenu

@Composable
@NonRestartableComposable
fun UserProfilePhoto(modifier : Modifier = Modifier) = ArtworkWithFailureInformer(
    modifier = modifier,
    model = if(LocalInspectionMode.current) null else Firebase.auth.currentUser?.photoUrl,
    contentDescription = "Your Profile Picture",
    placeHolder = rememberVectorPainter(image =  Icons.Default.AccountCircle),
    contentScale = ContentScale.FillBounds
)

@Composable
fun UserProfilePhotoWithDropDown(modifier : Modifier = Modifier) {
    var isOpen by remember { mutableStateOf(false) }
    UserProfilePhoto(
        modifier = modifier
            .size(
                height = 124.dp,
                width = 124.dp
            )
            .clickable(
                role = Role.DropdownList,
                onClickLabel = "Show Profile Quick-Actions",
                onClick = { isOpen = true }
            )
    )

    val resources = LocalContext.current.resources

    Dropdown(
        isOpen = isOpen,
        menu = dropDownMenu<String> {
            item(id = "settings",title= "Settings") {
                icon(Icons.Default.Settings)

                forEachCategory { id , icon , title , _ ->
                    item(id = id,title = resources.getString(title)) {
                        icon(icon)
                    }
                }
            }

        },
        onItemSelected = {
           // TODO : Implement this in the future
        },
        onDismiss = { isOpen = false },
        enter = EnterAnimation.SharedAxisXForward,
        exit = ExitAnimation.SlideOutHorizontally,
        easing = Easing.LinearOutSlowInEasing,
        enterDuration = 400,
        exitDuration = 400,
    )
}