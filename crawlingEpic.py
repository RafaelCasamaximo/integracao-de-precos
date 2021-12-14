import json
from epicstore_api import EpicGamesStoreAPI, OfferData

api = EpicGamesStoreAPI(locale="pt-BR", country="BR")
resultado = api.fetch_catalog(count=500)

out_file = open("myfile.json", "w")
json.dump(resultado, out_file, indent = 6)
out_file.close()

