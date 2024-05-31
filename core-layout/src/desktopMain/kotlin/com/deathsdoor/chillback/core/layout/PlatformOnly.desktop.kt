package com.deathsdoor.chillback.core.layout

import androidx.compose.runtime.Composable

@Composable
actual inline fun AndroidOnly(content: @Composable () -> Unit) = Unit