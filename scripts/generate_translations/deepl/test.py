from src.deepl import DeeplScrapper;

scrapper = DeeplScrapper()

o = scrapper.translate("en","de","see you soon")
print("Done = " + o)