from enum import Enum

class SettingScreenRoutes(Enum):
    ThemeSelector = ("settings/theme", "Themes", None)  # No description provided
    WidgetSelector = ("settings/widget", "Widgets", "Functionality not yet implemented")
    Audio = ("settings/audio", "Audio", "Customize your music playback experience.")
    Personalize = ("settings/personalize", "Personalize", "Tailor the app to your preferences.")
    Library = ("settings/ondevicelibrary", "Local Library", "Enhance your music library management.")
    BackupRestore = ("settings/backuprestore", "Backup and Restore", "Backup and restore your settings, songs, and playlists")
    About = ("settings/about", "About", "Learn about the app and its creator")


formatted_data = []
for item in SettingScreenRoutes:
  # Access enum member attributes using .value
  route, title, description = item.valuea
  formatted = route.replace('/', '_')
  formatted_string = f"""<string name="{formatted}_title">{title}</string>\n<string name="{formatted}_description">{description}</string>"""
  formatted_data.append(formatted_string)

print('\n'.join(formatted_data))
