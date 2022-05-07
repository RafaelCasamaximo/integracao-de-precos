from ctypes import Array
import json
from typing import Any, Dict
from datetime import datetime
import re

from stringNormalizer import StringNormalizer

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
            steamItem = self.handleSteamItem(item)
            if steamItem != None and self.checkProductIntegrity(steamItem):
                steamCollection.append(steamItem)

        self.steamCollection = steamCollection
        return steamCollection

    def handleSteamItem(self, item: Dict) -> Dict:
        try:
            steamItem = {
            'id': str(item['appid']),
            'name': item['name'],
            'genre': item['genre'],
            'languages': item['languages'],
            'publisher': item['publisher'],
            'isFree': True if item['initialprice'] == '0' else False,
            'originalPrice': int(item['initialprice']),
            'dicountPrice': int(item['price']),
            'dicount': int(item['discount']),
            'currency': item['currency']
            }
            return steamItem
        except:
            pass
        return None



    def handlePlaystationCollection(self, collection: Array) -> Array:
        playstationCollection = []
        for item in collection:
            playstationItem = self.handlePlaystationItem(item)
            if playstationItem != None and self.checkProductIntegrity(playstationItem):
                playstationCollection.append(playstationItem)

        self.playstationCollection = playstationCollection
        return playstationCollection

    def checkIfProductIsFree(self, item: Dict) -> bool:
        if item['originalPrice'] == 'Gratuito':
            return True
        return False

    def priceToNumber(self, price: Any) -> int:
        value = re.sub('[^0-9]', '', str(price))
        return value

    def tryConvertToNumber(self, value):
        result = 0
        try:
            result = int(value)
        except:
            result = value
        return value

    def handlePlaystationItem(self, item: Dict) -> Dict:

        if item == None:
            return

        playstationItem = {
            'id': item['id'],
            'name': item['name'],
            'genre': item['genre'],
            'languages': item['textLanguages'],
            'publisher': item['publisher'],
            'isFree': True if item['originalPrice'] == 'Gratuito' else False,
            'originalPrice': self.tryConvertToNumber(self.priceToNumber(item['originalPrice'])),
            'dicountPrice': self.tryConvertToNumber(self.priceToNumber(item['discountPrice'])) if item['discountPrice'] != 'Gratuito' else 0,
            'dicount': self.tryConvertToNumber(self.priceToNumber(item['discount'])),
            'currency': 'BRL'
        }
        return playstationItem
        



    def handleEpicCollection(self, collection: Array) -> Array:
        epicCollection = []
        for item in collection:
            epicItem = self.handleEpicItem(item)
            if epicItem != None and self.checkProductIntegrity(epicItem):
                epicCollection.append(epicItem)


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
            'dicount': int(item['price']['totalPrice']['discount']),
            'currency': 'BRL'
        }
        return epicItem

    def checkProductIntegrity(self, product: Dict) -> bool:
        for field in product:
            if product[field] == '':
                return False
        return True

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
        
    def handleExport(self, simpleExportName) -> None:
        outputFile = None
        if simpleExportName:
            outputFile = open(f'crawlerOutput.json', 'w')
        else:
            outputFile = open(f'{self.jsonName}.json', 'w')
        json.dump(self.collection, outputFile, indent=6)
        outputFile.close()
        pass

    def handleFilter(self) -> None:
        #
        #   FOR ITEM
        #       for item posterior daquele
        #           caracteres repetidos
        #           se frequencia de caracteres > 70%
        #               merge dados
        #

        # Organiza os dados de maneira que o método possa iterar sobre todos os elementos comparando o nome
        totalCollection = []
        steamCollectionSize = len(self.steamCollection)
        epicCollectionSize = len(self.epicCollection)
        playstationCollectionSize = len(self.playstationCollection)
        
        if steamCollectionSize != 0:
            totalCollection.extend(self.steamCollection)
        if epicCollectionSize != 0:
            totalCollection.extend(self.epicCollection)
        if playstationCollectionSize != 0:
            totalCollection.extend(self.playstationCollection)

        # Itera sobre todos os elementos comparando o nome. Caso o nome normalizado coincida o método substitui o nome, a publisher e o genero do jogo
        for outerIdx, outerItem in enumerate(totalCollection):
            for innerIdx, innerItem in enumerate(totalCollection[outerIdx:]):
                if outerIdx == innerIdx + outerIdx:
                    continue
                # Compare attributes to make sure that it isnt equivalent or semi-equal
                stringNormalizer = StringNormalizer()
                if stringNormalizer.compareStrings(outerItem['name'], innerItem['name']):
                    innerItem['name'] = outerItem['name']
                    innerItem['publisher'] = outerItem['publisher']
                    innerItem['genre'] = outerItem['genre']
                pass
        
        # Divide novamente entre as 3 coleções individuais
        self.steamCollection = totalCollection[0:steamCollectionSize]
        self.epicCollection = totalCollection[steamCollectionSize:epicCollectionSize]
        self.playstationCollection = totalCollection[steamCollectionSize:epicCollectionSize]
        return