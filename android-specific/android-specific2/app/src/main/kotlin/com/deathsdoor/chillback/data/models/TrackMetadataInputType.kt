package com.deathsdoor.chillback.data.models

import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.deathsdoor.chillback.ui.components.action.ResetIcon
import com.deathsdoor.chillback.ui.components.layout.LabeledCheckBox
import com.deathsdoor.chillback.ui.state.MetadataEditScreenState
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextField
import com.dokar.chiptextfield.rememberChipTextFieldState

// TODO : Add recommendations for each of them
sealed interface TrackMetadataInputType {
    object TextWithNoRecommendation : TrackMetadataInputType {
        @Composable
        fun InputField(
            trackMetadata : TrackMetadata,
            editModeState : MetadataEditScreenState
        ) = when (trackMetadata) {
            is TrackMetadata.SingleValue -> OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = trackMetadata.currentValue.value!!,
                onValueChange = { value -> trackMetadata.updateAnyFieldChanged(editModeState,value) },
                trailingIcon = { ResetIcon(trackMetadata.currentValue, trackMetadata.defaultValue!!) },
                label = { Text(stringResource(trackMetadata.stringId))},
                placeholder = { Text(trackMetadata.defaultValue!!) },
            )

            is TrackMetadata.MultipleValues -> {
                val chipState = rememberChipTextFieldState(trackMetadata.defaultValueAsChips())

                ChipTextField(
                    state = chipState,
                    onSubmit = { value ->
                        trackMetadata.currentValue.add(value)
                        trackMetadata.updateAnyFieldChanged(editModeState,value)
                        Chip(value)
                    },
                    trailingIcon = { ResetIcon(trackMetadata.currentValue, trackMetadata.defaultValue) },
                    label = { Text(stringResource(trackMetadata.stringId))},
                )
            }
        }
    }
    object DateWithNoRecommendation : TrackMetadataInputType {
        @Composable
        fun InputField(
            modifier : Modifier,
            trackMetadata : TrackMetadata.SingleValue,
            editModeState : MetadataEditScreenState
        ) = Card(modifier = modifier){
            Text(
                modifier = modifier,
                text = stringResource(trackMetadata.stringId),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            WheelDatePicker(modifier = Modifier.fillMaxWidth()) { snappedDate ->  trackMetadata.updateAnyFieldChanged(editModeState,snappedDate.toString()) }
        }
    }

    object CheckBoxWithNoRecommendation : TrackMetadataInputType {
        @Composable
        fun InputField(
            modifier : Modifier,
            trackMetadata : TrackMetadata.SingleValue,
            editModeState : MetadataEditScreenState
        ) = Card(modifier = modifier){
            LabeledCheckBox(
                checked = trackMetadata.currentValue.value != null,
                label = stringResource(trackMetadata.stringId),
                onClick = { trackMetadata.updateAnyFieldChanged(editModeState,if(trackMetadata.currentValue.value == null) "" else null) }
            )
        }
    }

    class WebsiteWithNoRecommendation private constructor(private val websiteName : String? = null) : TrackMetadataInputType {
        companion object {
            val discogs = WebsiteWithNoRecommendation("discogs")
            val none = WebsiteWithNoRecommendation()
            val wikipedia = WebsiteWithNoRecommendation("wikipedia")
        }

        var isError by mutableStateOf(false)
            private set

        @Composable
        fun InputField(
            trackMetadata : TrackMetadata.SingleValue,
            editModeState : MetadataEditScreenState
        ) {
            websiteName?.let {
                LaunchedEffect(Unit) {
                    trackMetadata.currentValue.value = "www.$websiteName.com/"
                }
            }

            LaunchedEffect(trackMetadata.currentValue.value) {
                val value = trackMetadata.currentValue.value!!
                isError = websiteName?.let { value.contains(it) } ?: true && Patterns.WEB_URL.matcher(value).matches()
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = trackMetadata.currentValue.value!!,
                isError = isError,
                onValueChange = { value -> trackMetadata.updateAnyFieldChanged(editModeState,value) },
                trailingIcon = { ResetIcon(trackMetadata.currentValue, trackMetadata.defaultValue!!) },
                label = { Text(stringResource(trackMetadata.stringId))},
                placeholder = { Text(trackMetadata.defaultValue!!) },
            )
        }
    }

    object IntegerWithNoRecommendation : TrackMetadataInputType {
        @Composable
        fun InputField(
            trackMetadata : TrackMetadata.SingleValue,
            editModeState : MetadataEditScreenState
        ) = OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = trackMetadata.currentValue.value!!,
            onValueChange = { value -> trackMetadata.updateAnyFieldChanged(editModeState,value) },
            trailingIcon = { ResetIcon(trackMetadata.currentValue, trackMetadata.defaultValue!!) },
            label = { Text(stringResource(trackMetadata.stringId))},
            placeholder = { Text(trackMetadata.defaultValue!!) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
