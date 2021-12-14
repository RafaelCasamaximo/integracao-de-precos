import json
from epicstore_api import EpicGamesStoreAPI, OfferData

api = EpicGamesStoreAPI(locale="pt-BR", country="BR")
resultado = api.fetch_catalog(count=500)

out_file = open("myfile-epic.json", "w")
json.dump(resultado, out_file, indent=6)
out_file.close()

data = {'data': []}

for i in range(len(resultado['data']['Catalog']['catalogOffers']['elements'])):
    data['data'].append({'appid': resultado['data']['Catalog']['catalogOffers']['elements'][i]['id'],
                         'name': resultado['data']['Catalog']['catalogOffers']['elements'][i]['title'],
                         'publisher': resultado['data']['Catalog']['catalogOffers']['elements'][i]['seller']['name'],
                         'price': resultado['data']['Catalog']['catalogOffers']['elements'][i]['price']['totalPrice']['discountPrice'],
                         'initialprice': resultado['data']['Catalog']['catalogOffers']['elements'][i]['price']['totalPrice']['originalPrice'],
                         'discount': resultado['data']['Catalog']['catalogOffers']['elements'][i]['price']['totalPrice']['discount']})

resultado_epic = open("resultado-epic.json", "w")
json.dump(data, resultado_epic, indent=6)
