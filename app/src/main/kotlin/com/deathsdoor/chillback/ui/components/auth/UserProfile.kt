package com.deathsdoor.chillback.ui.components.auth

import Dropdown
import Easing
import EnterAnimation
import StackedSnackbarDuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.androidpoet.dropdown.ExitAnimation
import com.deathsdoor.chillback.ui.navigation.forEachCategory
import com.deathsdoor.chillback.ui.providers.InitializeProvidersForPreview
import com.deathsdoor.chillback.ui.providers.LocalSnackbarState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dropDownMenu

@Composable
fun UserProfilePhoto(modifier : Modifier = Modifier) {
    val snackbar = LocalSnackbarState.current
    val placeHolder = rememberVectorPainter(image =  Icons.Default.AccountCircle)

    AsyncImage(
        modifier = modifier.clip(CircleShape),
        model = if(LocalInspectionMode.current) null else Firebase.auth.currentUser?.photoUrl,
        contentDescription = "Your Profile Picture",
        onError = {
            snackbar.showErrorSnackbar(
                title = "Profile picture loading failed",
                description = "There was an error loading your profile picture. Please try again later.",
                duration = StackedSnackbarDuration.Short
            )
        },
        fallback = placeHolder,
        placeholder =placeHolder
    )
}

@Composable
fun UserProfilePhotoWithDropDown(modifier : Modifier = Modifier) {
    var isOpen by remember { mutableStateOf(false) }
    UserProfilePhoto(
        modifier = modifier.size(
            height = 124.dp,
            width = 124.dp
        ).clickable(
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