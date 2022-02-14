
function readUser(e) {
    e.preventDefault();
    $.get($(this).data('href'), function (data) {
        var jogo = JSON.parse(JSON.stringify(data));
        var $modal = $('.modal-visualizar-jogo');

        $modal.find('.p_id').html('<strong>ID: </strong>' + jogo.id);
        $modal.find(".p_nome").html('<strong>Nome: </strong>' + jogo.nome);
        $modal.find('.p_genero').html('<strong>Gênero: </strong>' + jogo.genero);
        $modal.find('.p_linguagens').html('<strong>Linguagens: </strong>' + jogo.linguagens_suportadas);
        $modal.find('.p_suporte').html('<strong>Suporte à Controle: </strong>' + (jogo.suporte_a_controle == true ? 'Possui' : 'Não Possui'));
        $modal.find('.p_empresa').html('<strong>Empresa: </strong>' + jogo.nome_empresa);
        $modal.find('.p_gratuito').html('<strong>Gratuito: </strong>' + (jogo.gratuito == true ? 'Sim' : 'Não'));
        $modal.find('.p_idade').html('<strong>Idade: </strong>' + jogo.idade_requerida);
        $modal.find('.p_descricaoCurta').html('<strong>Descrição Curta: </strong>' + jogo.descricao_curta);
        $modal.find('.p_descricaoLonga').html('<strong>Descrição Longa: </strong>' + jogo.descricao_longa);
        $modal.find('.p_idEmpresa').html('<strong>ID Empresa: </strong>' + jogo.id_empresa);
    });
}

$(document).ready(function () {
    $(document).on('click', '.link_visualizar_jogo', readUser);
});