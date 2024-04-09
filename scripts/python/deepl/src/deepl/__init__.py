from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from urllib.parse import quote

class DeeplScrapper() :
    def __init__(self,driver = webdriver.Chrome(),timeout = 10) -> None:
        self.timeout = timeout
        self.driver = driver
        self.driver.get("https://www.deepl.com/en/translator")
        self.close_cookies()

    def __del__(self) -> None :
        self.driver.quit()
        
    
    def supported_languages_excluding_english() -> list[str] :
        return['bg', 'cs', 'da', 'nl', 'et', 'fi', 'fr', 'de', 'el', 'hu', 'it', 'lv', 'lt', 'no', 'pl', 'pt-br', 'pt', 'ro', 'sk', 'sl', 'es', 'sv', 'ar', 'zh', 'id', 'ja', 'ko', 'tr', 'uk']
    
    def close_cookies(self) -> None :
        script = """const cookie_policy= document.querySelector("#gatsby-focus-wrapper > div");if(cookie_policy !== null) cookie_policy.remove()"""
        self.driver.execute_script(script)
    
    def translate(self,source_lang : str,target_lang : str,text : str) -> str:  
        #https://www.deepl.com/en/translator#en/fr/hello
        self.driver.get(f"https://www.deepl.com/{source_lang}/translator#{source_lang}/{target_lang}/{quote(text)}")
        
        # This was not needed after setting this in the url
        #self._select_source_language(lang=source_lang)
        #self._select_target_language(lang=target_lang)

        self._input_source_language_text(text=text)
        self._wait_for_translation()

        return self._read_translation()

    def _select_source_language(self,lang : str) :
        script = f"""document.querySelector("#headlessui-popover-button-3 > span > span > strong").setAttribute("dl-selected-lang","{lang}")"""
        self.driver.execute_script(script)
    
    def _select_target_language(self,lang : str) :
        script = f"""document.querySelector("#headlessui-popover-button-5 > span > span").setAttribute("selected-lang","{lang}")"""
        self.driver.execute_script(script)

    def _input_source_language_text(self,text : str) :
        script = r"""
        const input_fields = document.querySelector("section[aria-labelledby=\"translation-source-heading\"] > div > div > d-textarea");
/*Replace with text*/
input_fields.value = arguments[0];
var evt = document.createEvent("Events");
evt.initEvent("change", true, true);
input_fields.dispatchEvent(evt);
"""
        self.driver.execute_script(script,text)

    def _wait_for_translation(self) :
        element = """section[aria-labelledby=\"translation-source-heading\"] > div > div > div[data-testid=\"translator-inline-loading-indicator\"]"""
        # Temp solution
        import time 
        time.sleep(1)

        # Wait for translation
        wait = WebDriverWait(self.driver, self.timeout)
        wait.until(EC.invisibility_of_element_located((By.CSS_SELECTOR,element)))

    def _read_translation(self) -> str:
        
        script = r"""
            return Array.from(document.querySelectorAll("d-textarea")[1].childNodes[0].childNodes).map(p => p.children[0].textContent).join("\n")
        """ 
        return self.driver.execute_script(script)
