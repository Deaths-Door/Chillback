package com.deathsdoor.chillback.data.models

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.deathsdoor.chillback.data.media.MediaMetadataExtractor
import com.deathsdoor.chillback.ui.state.MetadataEditScreenState
import com.dokar.chiptextfield.BasicChipTextField
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.rememberChipTextFieldState
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag

@Stable
sealed class TrackMetadata private constructor(
    val fieldKey: FieldKey,
    // Pre-defined frequency (0-255)
    val frequencyOfUsage : UByte,
    @StringRes val stringId : Int,
    val category : MetadataCategory,
    val inputType : TrackMetadataInputType
) {

    @Composable
    abstract fun ReadOnlyDefaultValue(modifier : Modifier)

    abstract suspend fun readTag(tag : Tag)

    class SingleValue(
        fieldKey: FieldKey,
        frequencyOfUsage : UByte,
        @StringRes stringId : Int,
        category : MetadataCategory,
        inputType : TrackMetadataInputType //= TrackMetadataInputType.TextWithNoRecommendation
    ) : TrackMetadata(fieldKey, frequencyOfUsage,stringId, category, inputType) {
        val currentValue = mutableStateOf<String?>(null)
        var defaultValue by mutableStateOf<String?>(null)

        fun updateAnyFieldChanged(editScreenState: MetadataEditScreenState,newValue : String?) {
            editScreenState.updateAnyFieldChanged<String?>(currentValue.value,defaultValue)
            currentValue.value = newValue
        }


        override suspend fun readTag(tag : Tag) {
            defaultValue = tag.getFirst(fieldKey)
            currentValue.value = defaultValue
        }

        @Composable
        override fun ReadOnlyDefaultValue(modifier : Modifier) = Text(
            modifier = modifier,
            text = defaultValue.orEmptyMessage()
        )
    }

    class MultipleValues(
        fieldKey: FieldKey,
        frequencyOfUsage : UByte,
        @StringRes stringId : Int,
        category : MetadataCategory,
        inputType : TrackMetadataInputType// = TrackMetadataInputType.TextWithNoRecommendation
    ) : TrackMetadata(fieldKey, frequencyOfUsage,stringId, category, inputType) {
        var readInitialDefaultValue by mutableStateOf(false)

        val currentValue = mutableStateListOf("")
        val defaultValue = mutableStateListOf("")

        fun defaultValueAsChips(): List<Chip> =
            if(defaultValue.firstOrNull().isNullOrEmpty()) emptyList()
            else defaultValue.map { Chip(it) }

        fun updateAnyFieldChanged(editScreenState: MetadataEditScreenState,newValue: String) {
            editScreenState.updateAnyFieldChanged<List<String>>(currentValue,defaultValue)
            currentValue.add(newValue)
        }

        override suspend fun readTag(tag : Tag) {
            readInitialDefaultValue = true
            val tagValues = tag.getAll(fieldKey)
            defaultValue.addAll(tagValues)
            currentValue.addAll(tagValues)
        }

        @Composable
        override fun ReadOnlyDefaultValue(modifier: Modifier) =
            if(defaultValue.firstOrNull().isNullOrEmpty()) Text(
                modifier = modifier,
                text = null.orEmptyMessage()
            ) else {
                val chipState = rememberChipTextFieldState(chips = defaultValueAsChips())

                BasicChipTextField(
                    state = chipState,
                    onSubmit = { null },
                    readOnly = true,
                    readOnlyChips = true,
                )
            }
    }

   companion object {
       const val defaultValueLoading = "Fetching .."
       @Composable private fun String?.orEmptyMessage() = if(isNullOrEmpty()) "Field has no value.." else this

       @Suppress("RedundantSuspendModifier")
       suspend fun createTag(track: Track) : Tag {
           val audioFile = MediaMetadataExtractor.audioFileFrom(track.sourcePath)
           return audioFile.tagOrCreateDefault
       }
   }
}
