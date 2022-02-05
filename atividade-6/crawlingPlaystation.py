import json
from ctypes import Array
from typing import Any, Dict
from datetime import datetime
import time
from bs4 import BeautifulSoup
from tqdm import tqdm
from msedge.selenium_tools import Edge, EdgeOptions

class PlaystationCrawling:
    def __init__(self):

        # Parâmetros do Crawling
        self.basePageURL = 'https://store.playstation.com/pt-br/pages/browse/'
        self.baseGameURL = 'https://store.playstation.com/pt-br/concept/'

        # Parâmetros do Crawler
        self.options = EdgeOptions()
        self.options.use_chromium = True
        self.options.add_argument("headless")
        self.options.add_argument("disable-gpu")
        self.options.add_argument('log-level=3')
        self.driver = Edge(options=self.options)

        # Parâmetros da Query
        self.pageQuantity = 5
        self.ids = []
        self.gamesInfo = []

        # Parâmetros do JSON
        self.jsonName = 'playstationCrawler-' + datetime.now().strftime("%d-%m-%Y %H-%M-%S")

    """Realiza o processo de pegar o ID dos jogos nas {pageNumber} primeiras páginas"""
    def getGamesIdsByPage(self, pageNumber: int) -> Array:

        ids = []

        for i in tqdm(range(pageNumber)):
            self.driver.get(self.basePageURL + str(i + 1))
            time.sleep(3)
            soup = BeautifulSoup(self.driver.page_source, 'html.parser')
            results = soup.find_all('a', {'data-track-content': 'web:store:concept-tile'})
            for result in results:
                try:
                    ids.append(json.loads(result['data-telemetry-meta'])['id'])
                except:
                    pass
        
        self.ids = ids
        return self.ids

    """Salva em product['attribute'] o conteúdo da tag com o par {property: value}"""
    def crawlAttribute(self, soup: Any, product: Dict, attribute: str, tag: str, property: str, value: str) -> None:
            try:
                results = soup.find_all(tag, {property: value})
                product[attribute] = results[0].text.strip()
            except: 
                pass
        
    """Auxiliar para converter o valor do produto do formado <R$ xxx,yy> para um inteiro xxxyy
       Retorna 0 caso não seja possível
    """
    def getMoneyValue(self, money: str) -> int:
        try:
            value = int(money.replace('R$', '').replace(',', ''))
        except:
            value = 0
        return value

    """Verifica se product possui todos os atributos diferentes de <''>"""
    def checkProductIntegrity(self, product: Dict) -> bool:
        for field in product:
            if product[field] == '':
                return False
        return True

    """Extrai a informação de um único jogo dado o ID. Retorna caso seja bem sucedido."""
    def getGameInfo(self, id: str) -> Dict:

        product = {
            'id': id,
            'name': '',
            'plat': '',
            'release': '',
            'genre': '',
            'publisher': '',
            'textLanguages': '',
            'originalPrice': '',
            'discountPrice': '',
            'discount': '',
        }

        self.driver.get(self.baseGameURL + str(id))
        soup = BeautifulSoup(self.driver.page_source, 'html.parser')

        # Crawling Título
        self.crawlAttribute(soup, product, 'name', 'h1', 'data-qa', 'mfe-game-title#name')

        # Crawling Plataforma
        self.crawlAttribute(soup, product, 'plat', 'dd', 'data-qa', 'gameInfo#releaseInformation#platform-value')

        # Crawling Lançamento
        self.crawlAttribute(soup, product, 'release', 'dd', 'data-qa', 'gameInfo#releaseInformation#releaseDate-value')

        # Crawling Distribuidora/ Desenvolvedora
        self.crawlAttribute(soup, product, 'publisher', 'dd', 'data-qa', 'gameInfo#releaseInformation#publisher-value')

        # Crawling Gênero
        self.crawlAttribute(soup, product, 'genre', 'dd', 'data-qa', 'gameInfo#releaseInformation#genre-value')

        # Crawling Idiomas de Texto
        self.crawlAttribute(soup, product, 'textLanguages', 'dd', 'data-qa', 'gameInfo#releaseInformation#subtitles-value')

        # Crawling do Preço Atual
        self.crawlAttribute(soup, product, 'discountPrice', 'span', 'data-qa', 'mfeCtaMain#offer0#finalPrice')

        # Crawling do Preço Original
        self.crawlAttribute(soup, product, 'originalPrice', 'span', 'data-qa', 'mfeCtaMain#offer0#originalPrice')

        # Cálculo Desconto
        if product['originalPrice'] == '':
            product['originalPrice'] = product['discountPrice']
            product['discount'] = 0
        else:
            product['discount'] = self.getMoneyValue(product['originalPrice']) - self.getMoneyValue(product['discountPrice'])

        if self.checkProductIntegrity(product):
            return product

    """Extrai a informação de todos os ids da lista"""
    def getGamesInfo(self) -> Array:
        for id in tqdm(self.ids):
            game = self.getGameInfo(id)
            self.gamesInfo.append(game)
        return self.gamesInfo
    
    """Salva as informações adquiridas num JSON"""
    def saveGamesInfoInJSON(self) -> None:
        gamesInfoFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.gamesInfo, gamesInfoFile, indent=6)
        gamesInfoFile.close()

    def start(self) -> None:
        print('-Iniciando Playstation Store Crawling')
        print('-Crawling dos IDs dos Jogos')
        print(f'-Extraindo IDs das primeiras {self.pageQuantity} páginas')
        self.getGamesIdsByPage(self.pageQuantity)
        print('-Extraindo Informações dos IDs')
        self.getGamesInfo()
        print(f'-Exportando arquivo: <{self.jsonName}.json>')
        self.saveGamesInfoInJSON()



if __name__ == "__main__":
    steamCrawling = PlaystationCrawling()
    steamCrawling.start()