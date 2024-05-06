package com.deathsdoor.chillback.data.extensions

import androidx.compose.runtime.Composable
import com.benasher44.uuid.Uuid
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UuidSerializer : KSerializer<Uuid> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "Uuid",
        kind = PrimitiveKind.STRING
    )
    override fun deserialize(decoder: Decoder) = Uuid.fromString(decoder.decodeString())!!
    override fun serialize(encoder: Encoder, value: Uuid) = encoder.encodeString(value.toString())
}