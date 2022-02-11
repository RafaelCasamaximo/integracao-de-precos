package model;

import java.sql.Date;

public class LojaJogos {
    private Integer id_loja;
    private Integer id_jogo;
    private Float preco_jogo;
    private String loja_crawl;
    private Date data_crawl;

    public Integer getId_loja() {
        return id_loja;
    }

    public void setId_loja(Integer id_loja) {
        this.id_loja = id_loja;
    }

    public Integer getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(Integer id_jogo) {
        this.id_jogo = id_jogo;
    }

    public Float getPreco_jogo() {
        return preco_jogo;
    }

    public void setPreco_jogo(Float preco_jogo) {
        this.preco_jogo = preco_jogo;
    }

    public String getLoja_crawl() {
        return loja_crawl;
    }

    public void setLoja_crawl(String loja_crawl) {
        this.loja_crawl = loja_crawl;
    }

    public Date getData_crawl() {
        return data_crawl;
    }

    public void setData_crawl(Date data_crawl) {
        this.data_crawl = data_crawl;
    }
}
