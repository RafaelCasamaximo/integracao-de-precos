# Sistema de Integração de Preços
### Trabalho da disciplina Banco de Dados

Para o trabalho da disciplina será realizado o levantamento de dados, detalhes e preços de jogos digitais através das seguintes plataformas:

* [Steam](https://store.steampowered.com/)
* [Epic Games](https://www.epicgames.com/store/pt-BR/)
* [Playstation Store](https://store.playstation.com/pt-br/pages/latest)

Essas lojas digitais disponibilizam de jogos, muitas vezes recorrentes entre as lojas, e com preços diferentes, por conta de promoções e variações.

### Quais informações serão utilizadas?
Cada uma das listas representa uma entidade. O diagrama Entidade Relacionamento pode ser visto detalhadamente na imagem da atividade 5, junto do arquivo da implementação SQL.

#### Loja
* Identificador
* Nome

#### Jogo

- Identificador;
- Nome;
- Gênero;
- Linguagens Suportadas;
- Suporte a Controle;
- Nome da Empresa;
- Gratuíto:
- Idade Requerida;
- Descrição Curta;
- Descrição Longa;

#### Empresa
- Identificador;
- Descrição Curta;
- Número de Jogos;
- Website;

#### LojaJogo (relação)
- Identificador da Loja;
- Identificador do Jogo;
- Preço;

### Web Crawling
A aquisição das informações para a população do banco de dados necessita de web crawling. Para isso, alguns scripts disponibilizados na atividade 6 realizam a extração dos dados dos sites por meio da biblioteca Selenium e BeautifulSoup e de APIs externas.

