fun main() {
    print(checkformissingones())
//print(generateFieldContent())
}

fun checkformissingones() : String {
    return FieldKey.values().mapNotNull { if(string.contains(it.name)) null else it }.joinToString("\n")
}

// The Default Value and the currentChangedValue
fun generateNullableMutableStates() = joinFieldKeysUsing { it , _ ->
    val name = it.trackMutableState()
    val nullMutableState = "mutableStateOf<String?>(null)"
    "private val $name = $nullMutableState\nprivate val ${it.trackDefaultValue()} by $nullMutableState"
}

fun generateSetFieldsForUpdateTags() = joinFieldKeysUsing { it , key ->
    "tag.setField(FieldKey.$key,track$it.value)"
}

fun generateFieldContent() =  joinFieldKeysUsing { it , key ->
    val defaultValue = it.trackDefaultValue()
    """
scope.item {
if($defaultValue == null) {
LaunchedEffect(Unit) {
if($defaultValue == null) return@LaunchedEffect
$defaultValue = AudioFile.read(track.sourcePath).tagOrCreateDefault.getField($key)
}
}

content("${it.deCamelCase()}",${it.trackMutableState()},$defaultValue)
}"""
}

fun String.trackMutableState() = "track$this"
fun String.trackDefaultValue() = "${this.trackMutableState()}Default"
fun joinFieldKeysUsing(separator : String = "\n", map : (String, String) -> String) = FieldKey.values().joinToString(separator= separator) { key ->
    val asCamelCase = key.name.replace("_"," ").split(" ").joinToString(separator = " ") {
        it.lowercase().replaceFirstChar { char -> char.titlecase() }
    }.replace(" ","")
    map(asCamelCase,key.name)
}

fun String.deCamelCase() : String {
    if (isEmpty()) return this

    val result = StringBuilder()
    result.append(this[0])
    for (i in 1 until this.length) {
        if (Character.isUpperCase(this[i])) result.append(" ")
        result.append(this[i])
    }
    return result.toString()
}