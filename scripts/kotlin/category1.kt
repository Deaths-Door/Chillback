fun main() {
    val output = MetadataCategory.values().joinToString("\n") {
        """<string name="sort_by_${it.toString().lowercase()}">Is ${it.displayName} category</string>"""
    }

    val output2 = MetadataCategory.values().joinToString(",\n"){
        """${it.name}(R.string.sort_by_${it.toString().lowercase()})"""
    }

    print(output)
}

enum class MetadataCategory(val displayName: String) {
    AUDIO_FINGERPRINT("Audio Identifiers"),
    ALBUM("Album Information"),
    AUDIO_PROPERTIES("Audio Properties"),
    ARTIST("Artist Information"),
    ADDITIONAL_CONTENT("Additional Content"),
    CLASSIFICATIONS("Classifications"),
    CREDITS("Credits"),
    MUSICBRAINZ("Musicbrainz"),
    TRACK("Track Information"),
}