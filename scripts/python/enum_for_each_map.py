from enum import Enum

class SettingScreenRoutes(Enum):
    ThemeSelector = ("settings/theme", "Themes", None)  # No description provided
    WidgetSelector = ("settings/widget", "Widgets", "Functionality not yet implemented")
    Audio = ("settings/audio", "Audio", "Customize your music playback experience.")
    Personalize = ("settings/personalize", "Personalize", "Tailor the app to your preferences.")
    Library = ("settings/lib", "Local Library", "Enhance your music library management.")
    BackupRestore = ("settings/backuprestore", "Backup & Restore", "Backup and restore your settings, songs, and playlists")
    About = ("settings/about", "About", "Learn about the app and its creator")


def get_formatted_data():
  """
  Extracts title and description from SettingScreenRoutes enum and formats them.
  """
  formatted_data = []
  for item in SettingScreenRoutes:
    route ,title, description = item.value
    caps = route[len("settings/"):].upper()
    snakecase = route.replace("/","_")
    name = route.index('/')
    formatted_data.append(f"on({caps},Icons.Default.Settings,R.strings.{snakecase}_title,R.strings.{name}_description)")
  return formatted_data

formatted_data = '\n'.join(get_formatted_data())
print(formatted_data)
