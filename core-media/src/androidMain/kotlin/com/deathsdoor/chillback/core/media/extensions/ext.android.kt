package com.deathsdoor.chillback.core.media.extensions

import androidx.core.net.toFile
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import java.io.File

internal actual fun Uri.toFile(): File = toAndroidUri().toFile()