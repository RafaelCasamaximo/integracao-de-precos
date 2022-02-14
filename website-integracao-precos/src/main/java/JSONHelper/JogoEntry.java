package JSONHelper;

public class JogoEntry {
    public String id;
    public String nome;
    public String genero;
    public String linguagens_suportadas;
    public Boolean suporte_a_controle;
    public String nome_empresa;
    public Boolean gratuito;
    public int idade_requerida;
    public String descricao_curta;
    public String descricao_longa;
    public int id_empresa;
    public int preco_original;
    public int preco_disconto;
    public int disconto;

    public JogoEntry(String id, String nome, String genero, String linguagens_suportadas, Boolean suporte_a_controle, String nome_empresa, Boolean gratuito, int idade_requerida, String descricao_curta, String descricao_longa, int id_empresa, int preco_original, int preco_disconto, int disconto) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.linguagens_suportadas = linguagens_suportadas;
        this.suporte_a_controle = suporte_a_controle;
        this.nome_empresa = nome_empresa;
        this.gratuito = gratuito;
        this.idade_requerida = idade_requerida;
        this.descricao_curta = descricao_curta;
        this.descricao_longa = descricao_longa;
        this.id_empresa = id_empresa;
        this.preco_original = preco_original;
        this.preco_disconto = preco_disconto;
        this.disconto = disconto;
    }
}
