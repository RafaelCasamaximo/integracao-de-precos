import csv
import json
import requests
import re
import time
from bs4 import BeautifulSoup
from tqdm import tqdm
from msedge.selenium_tools import Edge, EdgeOptions

class PlaystationCrawling:
    def __init__(self):
        self.basePageURL = 'https://store.playstation.com/pt-br/pages/browse/'
        self.baseGameURL = 'https://store.playstation.com/pt-br/concept/'


        self.options = EdgeOptions()
        self.options.use_chromium = True
        self.driver = Edge(options=self.options)

        self.ids = []
        self.gamesInfo = []

        self.get_games_ids(3)
        self.get_games_info()

    def print_on_file(self, name, data):
        out_file = open(f"{name}.html", "w")
        out_file.write(str(data))
        out_file.close()

    def get_games_ids(self, pageNumbers):

        ids = []

        for i in range(pageNumbers):
            self.driver.get(self.basePageURL + str(i + 1))
            time.sleep(3)
            soup = BeautifulSoup(self.driver.page_source, 'html.parser')
            results = soup.find_all('a', {'data-track-content': 'web:store:concept-tile'})
            print(type(results))
            for result in results:
                try:
                    ids.append(json.loads(result['data-telemetry-meta'])['id'])
                except:
                    continue
            self.ids = ids


    def get_games_info(self):
        for id in self.ids:
            self.driver.get(self.baseGameURL + str(id))


crawling = PlaystationCrawling()