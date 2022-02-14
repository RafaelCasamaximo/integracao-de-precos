package JSONHelper;

public class EmpresaEntry {
    public String nome;
    public String id;

    public EmpresaEntry(String nome, String id) {
        this.nome = nome;
        this.id = id;
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
