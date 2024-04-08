package com.deathsdoor.chillback.homepage.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler

val emailAddressRegex by lazy {
    Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}

internal const val PROJECT_HOME_PAGE = "https://github.com/Deaths-Door/Chillback"