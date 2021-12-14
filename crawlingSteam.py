import csv
import json
import requests
from bs4 import BeautifulSoup
from tqdm import tqdm
from msedge.selenium_tools import Edge, EdgeOptions

class SteamCrawling:
    def __init__(self):
        self.categories = ['action','arcade_rhythm','fighting_martial_arts','action_run_jump','action_beat_em_up','action_rogue_like','action_tps','action_fps','adventure','casual','metroidvania','puzzle_matching',
        'adventure_rpg','visual_novel','interactive_fiction','rpg_jrpg','rpg_action','rpg_strategy_tactics','adventure_rpg','rpg_party_based','rpg_turn_based','rogue_like_rogue_lite','sim_building_automation',
        'sim_dating','sim_space_flight','sim_physics_sandbox','sim_business_tycoon','sim_farming_crafting','sim_life','strategy_cities_settlements','tower_defense','strategy_turn_based','strategy_real_time',
        'strategy_grand_4x','strategy_military','strategy_card_board','racing','sports_team','sports','sports_individual','sports_fishing_hunting','racing_sim']

        self.baseCategoryURL = 'https://store.steampowered.com/category/'

        self.options = EdgeOptions()
        self.options.use_chromium = True
        self.driver = Edge(options=self.options)

        self.ids = []
        self.gamesInfo = []

    def get_url(self, category):
        return self.baseCategoryURL + category

    def get_ids_info(self):
        print('EXTRAINDO INFORMAÇÕES DOS IDS')
        for id in tqdm(self.ids):
            r = requests.get('https://steamspy.com/api.php?request=appdetails&appid=' + id)
            self.gamesInfo.append(json.loads(r.text))

        out_file = open("myfile.json", "w")
        json.dump(self.gamesInfo, out_file, indent = 6)
        out_file.close()
        


    def get_data(self):
        print('EXTRAINDO IDS')
        for category in tqdm(self.categories):
            self.driver.get(self.get_url(category))
            soup = BeautifulSoup(self.driver.page_source, 'html.parser')
            results = soup.find_all('a')
            for result in results:
                if result.has_attr('data-ds-appid'):
                    self.ids.append(result['data-ds-appid'])
        self.get_ids_info()


    # div NewReleasesRows
crawling = SteamCrawling()
crawling.get_data()
