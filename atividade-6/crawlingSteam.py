from ctypes import Array
from datetime import datetime
import json
import requests
from bs4 import BeautifulSoup
from tqdm import tqdm
from msedge.selenium_tools import Edge, EdgeOptions
import random

class SteamCrawling:
    def __init__(self) -> None:

        # Parâmetros do Crawling
        self.categories = [
            'action',
            'arcade_rhythm',
            'fighting_martial_arts',
            'action_run_jump',
            'action_beat_em_up',
            'action_rogue_like',
            'action_tps',
            'action_fps',
            'adventure',
            'casual',
            'metroidvania',
            'puzzle_matching',
            'adventure_rpg',
            'visual_novel',
            'interactive_fiction',
            'rpg_jrpg',
            'rpg_action',
            'rpg_strategy_tactics',
            'adventure_rpg',
            'rpg_party_based',
            'rpg_turn_based',
            'rogue_like_rogue_lite',
            'sim_building_automation',
            'sim_dating',
            'sim_space_flight',
            'sim_physics_sandbox',
            'sim_business_tycoon',
            'sim_farming_crafting',
            'sim_life',
            'strategy_cities_settlements',
            'tower_defense',
            'strategy_turn_based',
            'strategy_real_time',
            'strategy_grand_4x',
            'strategy_military',
            'strategy_card_board',
            'racing',
            'sports_team',
            'sports',
            'sports_individual',
            'sports_fishing_hunting',
            'racing_sim'
        ]
        self.baseCategoryURL = 'https://store.steampowered.com/category/'

        # Parâmetros do Crawler
        self.options = EdgeOptions()
        self.options.use_chromium = True
        self.options.add_argument("headless")
        self.options.add_argument("disable-gpu")
        self.options.add_argument('log-level=3')
        self.driver = Edge(options=self.options)

        # Parâmetros da Query
        self.ids = []
        self.gamesInfo = []
        self.quantityCap = 10

        # Parâmetros do JSON
        self.jsonName = 'steamCrawler-' + datetime.now().strftime("%d-%m-%Y %H-%M-%S")
        pass

    """Concatena uma categoria com a URL base"""
    def getCategoryURL(self, category: str) -> str:
        return self.baseCategoryURL + category

    """Faz uma requisição para retornar os detalhes do jogo com o id"""
    def getIdInfo(self, id: int) -> str:
        response = requests.get(f'https://steamspy.com/api.php?request=appdetails&appid={id}')
        if response.status_code != 200:
            response.raise_for_status()
        return response.text

    def getGamePrice(self, id: int):
        response = requests.get(f'https://store.steampowered.com/api/appdetails?appids={id}')
        if response.status_code != 200:
            response.raise_for_status()
        responseObject = json.loads(response.text)
        try:
            price = responseObject[list(responseObject.keys())[0]]['data']['price_overview']
        except:
            price = ''
        return price

    """Converte a informação de um jogo de string para um objeto e adiciona no gamesInfo"""
    def addGameInfo(self, idInfoText: str, price) -> None:
        gameItem = json.loads(idInfoText)
        if price != '':
            gameItem['initialprice'] = price['initial']
            gameItem['price'] = price['final']
            gameItem['discount'] = price['initial'] - price['final']
            gameItem['currency'] = 'BRL'
        else:
            gameItem['currency'] = 'USD'
        self.gamesInfo.append(gameItem)
        pass

    """Realiza o processo para todos os IDs de jogos"""
    def getIdsInfo(self) -> Array:
        for id in tqdm(self.ids):
            idInfoText = self.getIdInfo(id)
            price = self.getGamePrice(id)
            self.addGameInfo(idInfoText, price)
        return self.gamesInfo

    """Salva as informações adquiridas num JSON"""
    def saveGamesInfoInJSON(self) -> None:
        gamesInfoFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.gamesInfo, gamesInfoFile, indent=6)
        gamesInfoFile.close()
    
    """Crawling para selecionar os IDs das diferentes categorias"""
    def getIds(self) -> Array:
        for category in tqdm(self.categories):
            self.driver.get(self.getCategoryURL(category))
            soup = BeautifulSoup(self.driver.page_source, 'html.parser')
            queryResults = soup.find_all('a')
            for result in queryResults:
                if result.has_attr('data-ds-appid'):
                    self.ids.append(result['data-ds-appid'])
        pass

    """Aplica um limite de jogos"""
    def applyCap(self) -> None:
        if self.quantityCap != -1:
            random.shuffle(self.ids)
            self.ids = random.sample(self.ids, self.quantityCap)

    """Função que realiza todo o processo do começo ao fim"""
    def start(self) -> None:
        print('-Iniciando Steam Crawling')
        print('-Crawling dos IDs dos Jogos')
        self.getIds()
        print(f'-Aplicando limite de {self.quantityCap} itens')
        self.applyCap()
        print('-Extraindo Informações dos IDs')
        self.getIdsInfo()
        print(f'-Exportando arquivo: <{self.jsonName}.json>')
        self.saveGamesInfoInJSON()

        
if __name__ == "__main__":
    steamCrawling = SteamCrawling()
    steamCrawling.start()