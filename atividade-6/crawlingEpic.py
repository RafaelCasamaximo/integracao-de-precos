from ctypes import Array
from datetime import datetime
import json
from re import I
from epicstore_api import EpicGamesStoreAPI, EGSProductType

class EpicCrawling:
    def __init__(self) -> None:

        # Parâmetros da API
        self.api = EpicGamesStoreAPI(locale='pt-BR', country='BR')

        # Parâmetros da Query
        self.quantityCap = 10
        self.quantityOffset = 0
        self.gamesInfo = []

        # Parâmetros do JSON
        self.jsonName = 'epicCrawler-' + datetime.now().strftime("%d-%m-%Y %H-%M-%S")

        pass

    """Realiza a query na API da EpicGamesStore, salva o resulado e retorna os valores"""
    def getQuery(self) -> Array:
        results = self.api.fetch_catalog(count=self.quantityCap, product_type=EGSProductType.PRODUCT_GAME, start=self.quantityOffset)
        
        aux = []
        for game in results['data']['Catalog']['catalogOffers']['elements']:
            try:
                gameDetails = self.api.get_product(game['urlSlug'])
                tags = gameDetails['pages'][0]['data']['meta']['tags']
                languages = gameDetails['pages'][0]['data']['requirements']['languages']
                game['tags'] = tags
                game['languages'] = languages
            except:
                game['tags'] = ''
                game['languages'] = ''
                pass

            aux.append(game)

        
        self.gamesInfo = aux
        return aux
        
    """Salva as informações adquiridas num JSON"""
    def saveGamesInfoInJSON(self) -> None:
        gamesInfoFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.gamesInfo, gamesInfoFile, indent=6)
        gamesInfoFile.close()

    """Função que realiza todo o processo do começo ao fim"""
    def start(self) -> None:
        print('-Iniciando Epic Crawling')
        print(f'-Limite de {self.quantityCap} itens')
        print('-Extraindo Informações dos IDs')
        self.getQuery()
        print(f'-Exportando arquivo: <{self.jsonName}.json>')
        self.saveGamesInfoInJSON()


if __name__ == "__main__":
    epicCrawling = EpicCrawling()
    epicCrawling.start()