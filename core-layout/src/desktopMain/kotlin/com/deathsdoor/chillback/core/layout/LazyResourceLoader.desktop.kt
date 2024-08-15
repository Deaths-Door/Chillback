package com.deathsdoor.chillback.core.layout

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

// Source Code of stringResource - jvm

actual fun LazyResourceLoader.stringResource(resource: StringResource): String {
    return StringDesc.Resource(resource).localized()
}

actual fun LazyResourceLoader.stringResource(
    resource: StringResource,
    vararg args: Any,
): String {
    return StringDesc.ResourceFormatted(resource, *args).localized()
}