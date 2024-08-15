package com.deathsdoor.chillback.core.layout

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

// Source Code of stringResource - android
actual fun LazyResourceLoader.stringResource(resource: StringResource): String {
    return StringDesc.Resource(resource).toString(value as Context)
}

actual fun LazyResourceLoader.stringResource(
    resource: StringResource,
    vararg args: Any,
): String {
    return StringDesc.ResourceFormatted(resource, *args).toString(value as Context)
}