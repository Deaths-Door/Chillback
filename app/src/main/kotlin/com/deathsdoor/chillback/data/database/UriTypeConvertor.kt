package com.deathsdoor.chillback.data.database

import android.net.Uri
import androidx.room.TypeConverter


class UriTypeConvertor {
    @TypeConverter
    fun from(value: String?): Uri? = value?.let { Uri.parse(it) }

    @TypeConverter
    fun into(value: Uri?): String? = value?.toString()
}
