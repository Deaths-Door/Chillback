from os.path import join as join_path
import os
from shared_translator import Translator

if __name__ == "__main__":
    module_directory = input("Enter Module Directory: ")
    resources_directory = join_path(module_directory,"src","commonMain","resources","MR")
    folder = next(entry for entry in os.listdir(resources_directory) if entry == "base")

    cache_file = join_path(module_directory, "translation_cache")
    protected_tags_file = join_path(module_directory, "protected_tags.txt")

    translator = Translator(resources_directory, folder, cache_file,lambda target_lang: target_lang)
    translator.create_translated_strings(protected_tags_file)