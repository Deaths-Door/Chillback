package com.deathsdoor.chillback.core.layout

import dev.icerock.moko.resources.StringResource
import kotlin.jvm.JvmInline

@JvmInline
expect value class LazyResourceLoader @PublishedApi internal constructor(val value : Any)

expect fun LazyResourceLoader.stringResource(resource : StringResource) : String
expect fun LazyResourceLoader.stringResource(resource : StringResource,vararg args : Any) : String