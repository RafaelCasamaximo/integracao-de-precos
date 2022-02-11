package model;

public class Empresa {
    private Integer id;
    private String descricao_curta;
    private Integer numero_jogos;
    private String website;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao_curta() {
        return descricao_curta;
    }

    public void setDescricao_curta(String descricao_curta) {
        this.descricao_curta = descricao_curta;
    }

    public Integer getNumero_jogos() {
        return numero_jogos;
    }

    public void setNumero_jogos(Integer numero_jogos) {
        this.numero_jogos = numero_jogos;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
