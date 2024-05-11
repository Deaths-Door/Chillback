from os.path import join as join_path
from utils import create_translated_strings

if __name__ == "__main__" :
    module_directory = input("Enter Module Directory: ")
    resources_directory = join_path(module_directory,"composeApp","src","commonMain","composeResources")
    folder = next(entry for entry in os.listdir(resources_directory) if entry == "values")
    create_translated_strings(resources_directory,folder,lambda target_lang : f"{folder}-{target_lang}")
    print("Finished translating all the strings")