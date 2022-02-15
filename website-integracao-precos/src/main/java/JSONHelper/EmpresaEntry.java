package JSONHelper;

public class EmpresaEntry {
    public String nome;
    public String id;
    public int num_jogos;

    public EmpresaEntry(String nome, String id) {
        this.nome = nome;
        this.id = id;
        this.num_jogos = 1;
    }

    public boolean equals(Object o)
    {
        EmpresaEntry aux = (EmpresaEntry) o;
        if(this.nome.equals(aux.nome))
        {
            return true;
        }
        return false;
    }
}
