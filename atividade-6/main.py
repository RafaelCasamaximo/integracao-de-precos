import click

from adapter import Adapter
from crawlingSteam import SteamCrawling
from crawlingPlaystation import PlaystationCrawling
from crawlingEpic import EpicCrawling

@click.command()
# Parâmetros de Crawlers
@click.option('--steam', '-s', is_flag=True, help='Execute Steam Store General Crawling.')
@click.option('--playstation', '-p', is_flag=True, help='Execute Playstation Store General Crawling.')
@click.option('--epic', '-e', is_flag=True, help='Execute Epic Games General Crawling.')
def cli(steam, playstation, epic):
    
    adapter = Adapter()
    steamData = None
    playstationData = None
    epicData = None

    if steam:
        steamCrawling = SteamCrawling()
        print('-Iniciando Steam Crawling')
        print('-Crawling dos IDs dos Jogos')
        steamCrawling.getIds()
        print(f'-Aplicando limite de {steamCrawling.quantityCap} itens')
        steamCrawling.applyCap()
        print('-Extraindo Informações dos IDs')
        steamData = steamCrawling.getIdsInfo()

        adapter.handleSteamCollection(steamData)
        pass

    if playstation:
        playstationCrawling = PlaystationCrawling()
        print('-Iniciando Playstation Store Crawling')
        print('-Crawling dos IDs dos Jogos')
        print(f'-Extraindo IDs das primeiras {playstationCrawling.pageQuantity} páginas')
        playstationCrawling.getGamesIdsByPage(playstationCrawling.pageQuantity)
        print('-Extraindo Informações dos IDs')
        playstationData = playstationCrawling.getGamesInfo()

        adapter.handlePlaystationCollection(playstationData)
        pass

    if epic:
        epicCrawling = EpicCrawling()
        print('-Iniciando Epic Crawling')
        print(f'-Limite de {epicCrawling.quantityCap} itens')
        print('-Extraindo Informações dos IDs')
        epicData = epicCrawling.getQuery()

        adapter.handleEpicCollection(epicData)
        pass

    adapter.handleCollection()
    adapter.handleExport()
    

    pass


if __name__ == '__main__':
    cli()
