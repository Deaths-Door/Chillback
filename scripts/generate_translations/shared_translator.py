from bs4 import BeautifulSoup
from os.path import join as join_path
from urllib.parse import quote
import os
from typing import Callable
from deepl.src.deepl import DeeplScrapper;
from xml.sax.saxutils import escape
from typing import Callable, Dict, List, Set
from concurrent.futures import wait , as_completed, ThreadPoolExecutor
import shelve
import dbm.dumb
from selenium.webdriver import Chrome;
import threading

class Translator:
    def __init__(self, resources_directory: str, folder: str, cache_file: str, create_folder_name: Callable[[str], str]):
        self.resources_directory = resources_directory
        self.folder = folder
        self.create_folder_name = create_folder_name
        self.cache_file = cache_file
        self.lock = threading.Lock()

    def read_strings_file(self) -> Dict[str, str]:
        file_path = join_path(self.resources_directory, self.folder, "strings.xml")
        with open(file_path, 'r', encoding='utf-8') as rfile:
            xml = BeautifulSoup(rfile.read(), "xml")
            elements = xml.find_all(name="string")
            return {element["name"]: element.text for element in elements}

    def write_strings_file(self, translations: Dict[str, str], target_lang: str):
        folder_name = self.create_folder_name(target_lang)
        folder_path = join_path(self.resources_directory, folder_name)
        os.makedirs(folder_path, exist_ok=True)

        strings_xml_path = join_path(folder_path, "strings.xml")
        with open(strings_xml_path, mode="w", encoding="utf-8") as wfile:
            soup = BeautifulSoup("<resources></resources>", "xml")
            for name, translation in translations.items():
                tag = soup.new_tag("string", attrs={"name": name})
                # this is done to 'escape' unicode characters so that android mergeResource tasks can succeed
                tag.string = escape(translation.encode('ascii', 'backslashreplace').decode('utf-8'))
                soup.resources.append(tag)
            wfile.write(str(soup))

    def read_existing_translations(self, target_lang: str) -> Set[str]:
        folder_name = self.create_folder_name(target_lang)
        strings_xml_path = join_path(self.resources_directory, folder_name, "strings.xml")
        if os.path.exists(strings_xml_path):
            with open(strings_xml_path, 'r', encoding='utf-8') as rfile:
                xml = BeautifulSoup(rfile.read(), "xml")
                elements = xml.find_all(name="string")
                return {element["name"] for element in elements}
        return set()

    def read_protected_tags(self, protected_tags_file: str) -> Set[str]:
        if os.path.exists(protected_tags_file):
            with open(protected_tags_file, 'r', encoding='utf-8') as rfile:
                return {line.strip() for line in rfile if line.strip()}
        return set()

    def translate_strings(self,scrapper : DeeplScrapper,string_values: List[str],target_lang: str,source_lang : str="en") -> List[str]:
        return [scrapper.translate(source_lang=source_lang, target_lang=target_lang, text=text) for text in string_values]

    def create_translated_strings_for_lang(self, target_lang: str, string_values: Dict[str, str], protected_tags: Set[str]):
        existing_tags = self.read_existing_translations(target_lang)
        translations = {}
        
        with shelve.open(self.cache_file, flag='c', protocol=None, writeback=False) as cache:
            cache.dict = dbm.dumb.open(self.cache_file, 'c')
            cached_translations = cache.get(target_lang, {})

            with Chrome() as driver :
                scrapper = DeeplScrapper(driver=driver)

                cached_names=[]
                to_translate=[]

                for name, text in string_values.items() :
                    if name not in existing_tags and name not in protected_tags:
                        cached_names.append(name) if text in cached_translations else to_translate.append((name))

                translated = self.translate_strings(
                    scrapper=scrapper,
                    target_lang=target_lang,
                    string_values=[string_values[name] for name in to_translate]
                )

                for name in cached_names:
                    translations[name] = cached_translations[string_values[name]]

                for name, text in zip(to_translate, translated):
                    translations[name] = text
                    with self.lock:
                        if target_lang not in cache:
                            cache[target_lang] = {}
                        cache[target_lang][string_values[name]] = text

        if translations:
            self.write_strings_file(translations, target_lang)

    def create_translated_strings(self, protected_tags_file: str):
        string_values = self.read_strings_file()
        protected_tags = self.read_protected_tags(protected_tags_file)
        target_langs = DeeplScrapper.supported_languages_excluding_english()


        with ThreadPoolExecutor() as executor:
            futures = [executor.submit(self.create_translated_strings_for_lang, lang, string_values, protected_tags) for lang in target_langs]
            for future in as_completed(futures):
                try:
                    future.result()
                except Exception as e:
                    print(f"Task generated an exception: {e}")

        print("Finished translating all the strings")