package model;

public class Jogo {
    private Integer id;
    private String nome;
    private String linguagens_suportadas;
    private Boolean suporte_a_controle;
    private String nome_empresa;
    private Boolean gratuito;
    private Integer idade_requerida;
    private String descricao_curta;
    private String descricao_longa;
    private Integer id_empresa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLinguagens_suportadas() {
        return linguagens_suportadas;
    }

    public void setLinguagens_suportadas(String linguagens_suportadas) {
        this.linguagens_suportadas = linguagens_suportadas;
    }

    public Boolean getSuporte_a_controle() {
        return suporte_a_controle;
    }

    public void setSuporte_a_controle(Boolean suporte_a_controle) {
        this.suporte_a_controle = suporte_a_controle;
    }

    public String getNome_empresa() {
        return nome_empresa;
    }

    public void setNome_empresa(String nome_empresa) {
        this.nome_empresa = nome_empresa;
    }

    public Boolean getGratuito() {
        return gratuito;
    }

    public void setGratuito(Boolean gratuito) {
        this.gratuito = gratuito;
    }

    public Integer getIdade_requerida() {
        return idade_requerida;
    }

    public void setIdade_requerida(Integer idade_requerida) {
        this.idade_requerida = idade_requerida;
    }

    public String getDescricao_curta() {
        return descricao_curta;
    }

    public void setDescricao_curta(String descricao_curta) {
        this.descricao_curta = descricao_curta;
    }

    public String getDescricao_longa() {
        return descricao_longa;
    }

    public void setDescricao_longa(String descricao_longa) {
        this.descricao_longa = descricao_longa;
    }

    public Integer getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(Integer id_empresa) {
        this.id_empresa = id_empresa;
    }
}
