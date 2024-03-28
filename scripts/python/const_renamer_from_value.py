print('\n'.join([line.replace("THEME",line[len("private const val THEME = \""):][:-1].upper()) for line in """
private const val THEME = "theme"
private const val THEME = "widget"
private const val THEME = "audio"
private const val THEME = "personalize"
private const val THEME = "ondevicelibrary"
private const val THEME = "backuprestore"
private const val THEME = "about"
""".split('\n')]))