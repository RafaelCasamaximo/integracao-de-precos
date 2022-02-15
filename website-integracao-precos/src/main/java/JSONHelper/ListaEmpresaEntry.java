package JSONHelper;
import model.Empresa;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListaEmpresaEntry {
    public ArrayList<EmpresaEntry> empresas;

    public ListaEmpresaEntry(JSONArray steamJsonArray, JSONArray epicJsonArray, JSONArray playstationJsonArray) throws JSONException {

        this.empresas = new ArrayList<EmpresaEntry>();
        int id = 0;
        for (int i = 0; i < steamJsonArray.length(); i++)
        {
            JSONObject jogoJson = steamJsonArray.getJSONObject(i);

            id++;
            EmpresaEntry empresa = new EmpresaEntry(jogoJson.get("publisher").toString(), String.valueOf(id));
            if(!this.empresas.contains(empresa))
            {
                this.empresas.add(empresa);
            }
        }

        for (int i = 0; i < epicJsonArray.length(); i++)
        {
            JSONObject jogoJson = epicJsonArray.getJSONObject(i);

            id++;
            EmpresaEntry empresa = new EmpresaEntry(jogoJson.get("publisher").toString(), String.valueOf(id));
            if(!this.empresas.contains(empresa))
            {
                this.empresas.add(empresa);
            }
            if(this.empresas.contains(empresa))
            {
                this.empresas.get(empresas.indexOf(empresa)).num_jogos++;
            }
        }

        for (int i = 0; i < playstationJsonArray.length(); i++)
        {
            JSONObject jogoJson = playstationJsonArray.getJSONObject(i);

            id++;
            EmpresaEntry empresa = new EmpresaEntry(jogoJson.get("publisher").toString(), String.valueOf(id));
            if(!this.empresas.contains(empresa))
            {
                this.empresas.add(empresa);
            }
        }
    }
}
