from ctypes import Array
import json
from typing import Dict
from datetime import datetime

"""
Formato padrão de exportação
Loja
    Identificador
    Nome

Jogo
    Identificador;
    Nome;
    Gênero;
    Linguagens Suportadas;
    Nome da Empresa;
    Gratuíto:

Empresa
    Identificador;
    Descrição Curta;
    Número de Jogos;
    Website;

LojaJogo (relação)
    Identificador da Loja;
    Identificador do Jogo;
    Preço;
"""

class Adapter:
    def __init__(self) -> None:
        self.steamCollection = []
        self.playstationCollection = []
        self.epicCollection = []
        self.collection = None
        self.jsonName = 'crawlerOutput-' + datetime.now().strftime("%d-%m-%Y %H-%M-%S")
        pass

    def handleSteamCollection(self, collection: Array) -> Array:
        steamCollection = []
        for item in collection:
            steamCollection.append(self.handleSteamItem(item))

        self.steamCollection = steamCollection
        return steamCollection

    def handleSteamItem(self, item: Dict) -> Dict:
        steamItem = {
            'id': str(item['appid']),
            'name': item['name'],
            'genre': item['genre'],
            'languages': item['languages'],
            'publisher': item['publisher'],
            'isFree': True if int(item['initialprice']) == 0 else False,
            'originalPrice': int(item['initialprice']),
            'dicountPrice': int(item['price']),
            'dicount': int(item['discount'])
        }
        return steamItem



    def handlePlaystationCollection(self, collection: Array) -> Array:
        playstationCollection = []
        for item in collection:
            playstationCollection.append(self.handlePlaystationItem(item))

        self.playstationCollection = playstationCollection
        return playstationCollection

    def handlePlaystationItem(self, item: Dict) -> Dict:
        playstationItem = {
            'id': item['id'],
            'name': item['name'],
            'genre': item['genre'],
            'languages': item['textLanguages'],
            'publisher': item['publisher'],
            'isFree': True if int(item['originalPrice']) == 'Gratuito' else False,
            'originalPrice': int(item['originalPrice']),
            'dicountPrice': int(item['discountPrice']) if int(item['discountPrice']) != 'Gratuito' else 0,
            'dicount': int(item['discount'])
        }
        return playstationItem
        



    def handleEpicCollection(self, collection: Array) -> Array:
        epicCollection = []
        for item in collection:
            epicCollection.append(self.handleEpicItem(item))

        self.epicCollection = epicCollection
        return epicCollection

    def handleEpicItem(self, item: Dict) -> Dict:
        epicItem = {
            'id': item['id'],
            'name': item['title'],
            'genre': item['tags'],
            'languages': item['languages'],
            'publisher': item['seller']['name'],
            'isFree': True if int(item['price']['totalPrice']['originalPrice']) == 0 else False,
            'originalPrice': int(item['price']['totalPrice']['originalPrice']),
            'dicountPrice': int(item['price']['totalPrice']['discountPrice']),
            'dicount': int(item['price']['totalPrice']['discount'])
        }
        return epicItem


    def handleCollection(self) -> None:
        
        steamData = {
            'time': datetime.now().strftime("%H:%M:%S"),
            'date': datetime.now().strftime("%d/%m/%Y"),
            'games': self.steamCollection
        }

        playstationData = {
            'time': datetime.now().strftime("%H:%M:%S"),
            'date': datetime.now().strftime("%d/%m/%Y"),
            'games': self.playstationCollection      
        }

        epicData = {
            'time': datetime.now().strftime("%H:%M:%S"),
            'date': datetime.now().strftime("%d/%m/%Y"),
            'games': self.epicCollection      
        }
        
        self.collection = {
            'steamCollection': steamData,
            'playstationCollection': playstationData,
            'epicCollection': epicData,
        }

        return self.collection
        
    def handleExport(self) -> None:
        outputFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.collection, outputFile, indent=6)
        outputFile.close()
        pass
