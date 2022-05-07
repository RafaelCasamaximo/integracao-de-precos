from email.policy import default
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
@click.option('--steamItemCap', '-sic', default=200, help='Cap the maximum item quantity from Steam.')
@click.option('--playstationPageCap', '-ppc', default=10, help='Cap the page quantity from playstation store.')
@click.option('--epicItemCap', '-eic', default=200, help='Cap the maximum item quantity from Epic.')
@click.option('--epicItemOffset', '-eio', default=0, help='Offset from the start item on Epic.')
@click.option('--simpleExportName', '-sen', is_flag=True, help='Export the file without the date and time.')
def cli(steam, playstation, epic, steamitemcap, playstationpagecap, epicitemcap, epicitemoffset, simpleExportName):
    
    adapter = Adapter()
    steamData = None
    playstationData = None
    epicData = None

    steamCrawling = SteamCrawling()
    steamCrawling.quantityCap = steamitemcap
    playstationCrawling = PlaystationCrawling()
    playstationCrawling.pageQuantity = playstationpagecap
    epicCrawling = EpicCrawling()
    epicCrawling.quantityCap = epicitemcap
    epicCrawling.quantityOffset = epicitemoffset

    if steam:
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
        print('-Iniciando Playstation Store Crawling')
        print('-Crawling dos IDs dos Jogos')
        print(f'-Extraindo IDs das primeiras {playstationCrawling.pageQuantity} páginas')
        playstationCrawling.getGamesIdsByPage(playstationCrawling.pageQuantity)
        print('-Extraindo Informações dos IDs')
        playstationData = playstationCrawling.getGamesInfo()

        adapter.handlePlaystationCollection(playstationData)
        pass

    if epic:
        print('-Iniciando Epic Crawling')
        print(f'-Limite de {epicCrawling.quantityCap} itens')
        print('-Extraindo Informações dos IDs')
        epicData = epicCrawling.getQuery()

        adapter.handleEpicCollection(epicData)
        pass

    adapter.handleCollection()
    adapter.handleFilter()
    adapter.handleExport(simpleExportName)
    

    pass


if __name__ == '__main__':
    cli()
