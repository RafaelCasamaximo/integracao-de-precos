import csv
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
        self.driver = Edge(options=self.options)

        # Parâmetros da Query
        self.ids = []
        self.gamesInfo = []
        self.quantityCap = 500

        # Parâmetros do JSON
        self.jsonName = 'steamCrawler-' + datetime.now().strftime("%d/%m/%Y %H:%M:%S")
        pass

    def getCategoryURL(self, category: str) -> str:
        return self.baseCategoryURL + category

    def getIdInfo(self, id: int) -> str:
        response = requests.get(f'https://steamspy.com/api.php?request=appdetails&appid={id}')
        if response.status_code != 200:
            response.raise_for_status()
        return response.text

    def addGameInfo(self, idInfoText: str) -> None:
        self.gamesInfo.append(json.loads(idInfoText))
        pass

    def getIdsInfo(self) -> Array:
        for id in tqdm(self.ids):
            idInfoText = self.getIdInfo(id)
            self.addGameInfo(idInfoText)
        return self.gamesInfo

    def saveGamesInfoInJSON(self) -> None:
        gamesInfoFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.gamesInfo, gamesInfoFile, indent=6)
        gamesInfoFile.close()
    
    def getIds(self) -> Array:
        for category in tqdm(self.categories):
            self.driver.get(self.getCategoryURL(category))
            soup = BeautifulSoup(self.driver.page_source, 'html.parser')
            queryResults = soup.find_all('a')
            for result in queryResults:
                if result.has_attr('data-ds-appid'):
                    self.ids.append(result['data-ds-appid'])
        pass

    def applyCap(self) -> None:
        if self.quantityCap != -1:
            random.shuffle(self.ids)
            self.ids = random.sample(self.ids, self.quantityCap)

    def start(self) -> None:
        print('-Iniciando Steam Crawling')
        print('-Crawling dos IDs dos Jogos')
        self.getIds()
        print(f'-Aplicando limite de {self.quantityCap} itens')
        print('-Extraindo Informações dos IDs')
        self.getIdsInfo()
        print(f'-Exportando arquivo: <{self.jsonName}.json>')
        self.saveGamesInfoInJSON()

        
if __name__ == "__main__":
    steamCrawling = SteamCrawling()
    steamCrawling.start()