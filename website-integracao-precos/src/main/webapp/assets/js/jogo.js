
function readUser(e) {
    e.preventDefault();
    $.get($(this).data('href'), function (data) {
        var jogo = JSON.parse(JSON.stringify(data));
        var $modal = $('.modal-visualizar-jogo');
        var $grafico = $('.aaa');

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

        var ctx = document.getElementById('grafico');
        var labels = [
            'January',
            'February',
            'March',
            'April',
            'May',
            'June',
        ];
        var data = {
            labels: labels,
            datasets: [{
                label: 'My First dataset',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: [0, 10, 5, 2, 20, 30, 45],
            }]
        };
        var myChart = new Chart(
            ctx,
            {
                type: 'line',
                data,
                options: {}
            }
        );

    });
}

$(document).ready(function () {
    $(document).on('click', '.link_visualizar_jogo', readUser);
});