var myChart = new Chart(document.getElementById('grafico'), {type: 'line', data: {}, options: {}})

function readUser(e) {
    e.preventDefault();
    $.get($(this).data('href'), function (data) {
        var json = JSON.parse(JSON.stringify(data));
        var jogo = json[0];
        var $modal = $('.modal-visualizar-jogo');

        $modal.find('.p_id').html('<strong>ID: </strong>' + jogo.left.id);
        $modal.find('.p_nome').html('<strong>Nome: </strong>' + jogo.left.nome);
        $modal.find('.p_genero').html('<strong>Gênero: </strong>' + jogo.left.genero);
        $modal.find('.p_linguagens').html('<strong>Linguagens: </strong>' + jogo.left.linguagens_suportadas);
        $modal.find('.p_suporte').html('<strong>Suporte à Controle: </strong>' + (jogo.left.suporte_a_controle == true ? 'Possui' : 'Não Possui'));
        $modal.find('.p_empresa').html('<strong>Empresa: </strong>' + jogo.left.nome_empresa);
        $modal.find('.p_gratuito').html('<strong>Gratuito: </strong>' + (jogo.left.gratuito == true ? 'Sim' : 'Não'));
        $modal.find('.p_idade').html('<strong>Idade: </strong>' + (jogo.left.idade_requerida == 0 ? 'Indisponível' : 0));
        $modal.find('.p_descricaoCurta').html('<strong>Descrição Curta: </strong>' + (jogo.left.descricao_curta.replace(/ /g,'') == '' ? 'Indisponível' : jogo.left.descricao_curta));
        $modal.find('.p_descricaoLonga').html('<strong>Descrição Longa: </strong>' + (jogo.left.descricao_longa.replace(/ /g,'') == '' ? 'Indisponível' : jogo.left.descricao_longa));
        $modal.find('.p_idEmpresa').html('<strong>ID Empresa: </strong>' + jogo.left.id_empresa);
        $modal.find('.p_lojaCrawl').html('<strong>Loja: </strong>' + jogo.right.loja_crawl);

        var dates  = [];
        var prices = [];
        var preco_por_data = json[1];
        preco_por_data.forEach( ppj => {
            dates.push(ppj.left)
            prices.push(ppj.right / 100)
        });

        var data = {
            labels: dates,
            datasets: [{
                label: 'preço',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: prices,
            }]
        };
        var ctx = document.getElementById('grafico')
        myChart.data = data
        myChart.update()

    });
}

$(document).ready(function () {
    $(document).on('click', '.link_visualizar_jogo', readUser);
});