package model;

public class Loja {
    public Loja(){

    }
    public Loja(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    private Integer id;
    private String nome;

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
}
