package com.deathsdoor.chillback.core.media.extensions

import com.eygraber.uri.Uri
import com.eygraber.uri.toURI
import java.io.File
import java.nio.file.Paths

// https://stackoverflow.com/a/72154393/20243803
internal actual fun Uri.toFile(): File = Paths.get(toURI()).toFile()