from os.path import join as join_path
import os
from utils import create_translated_strings

if __name__ == "__main__" :
    module_directory = input("Enter Module Directory: ")
    resources_directory = join_path(module_directory,"src","commonMain","resources","MR")
    folder = next(entry for entry in os.listdir(resources_directory) if entry == "base")
    create_translated_strings(resources_directory,folder,lambda target_lang : target_lang)

    print("Finished translating all the strings")
