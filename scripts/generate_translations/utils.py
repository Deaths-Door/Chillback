from bs4 import BeautifulSoup
from os.path import join as join_path
from urllib.parse import quote
import os
from typing import Callable
from deepl.src.deepl import DeeplScrapper;
from xml.sax.saxutils import escape

#use this https://chatgpt.com/c/1164ac1f-314d-4afa-816a-5c07281115ea
# input target_lang output folder_name
def create_translated_strings(resources_directory : str,folder : str,create_folder_name: Callable[str,str]) :
    file_path = join_path(resources_directory,folder,"strings.xml")

    with open(file_path,'r') as rfile :
        xml = BeautifulSoup(rfile.read(),"xml")
        elements = xml.find_all(name = "string")

        tags = []
        string_values = []

        for element in elements :
            tags.append(element["name"])
            string_values.append(element.text)

        try :
            translator = DeeplScrapper()

            for target_lang in DeeplScrapper.supported_languages_excluding_english():
                translate_text = lambda : escape(translator.translate(source_lang ="en",target_lang = target_lang,text = text))

                bulk_translations = [translate_text() for text in string_values]

                folder_name = create_folder_name(target_lang)
                folder_path = join_path(resources_directory,folder_name)
                if not os.path.exists(folder_path):
                    os.makedirs(folder_path)

                strings_xml = join_path(folder_path,"strings.xml")

                with open(strings_xml,mode= "w",encoding="utf-8") as wfile :
                    soup = BeautifulSoup("<resources></resources>","xml")

                    for index ,name_tag in enumerate(tags) :
                        tag = soup.new_tag("string",attrs = {"name" : name_tag})
                        tag.string = bulk_translations[index]
                        soup.resources.append(tag)

                    wfile.write(str(soup))
        except Exception as e:
            print(f"Error occurred: {e}")