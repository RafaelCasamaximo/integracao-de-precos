package JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LojaEntry {
    public String date;
    public String time;
    public ArrayList<JogoEntry> jogos;

    public LojaEntry(){
        jogos = new ArrayList<JogoEntry>();
    }

    public void JsonArrayToList(JSONArray jogoJsonArray) throws JSONException {
        for (int i = 0; i < jogoJsonArray.length(); i++)
        {
            JSONObject jogoJson = jogoJsonArray.getJSONObject(i);
            JogoEntry jogoAux = new JogoEntry(
                    jogoJson.get("id").toString(),
                    jogoJson.get("name").toString(),
                    jogoJson.get("genre").toString(),
                    jogoJson.get("languages").toString(),
                    false,
                    jogoJson.get("publisher").toString(),
                    (jogoJson.get("isFree").toString() == "true" ? true : false),
                    0,
                    "",
                    "",
                    0,
                    Integer.parseInt(jogoJson.get("originalPrice").toString()),
                    Integer.parseInt(jogoJson.get("dicountPrice").toString()),
                    Integer.parseInt(jogoJson.get("dicount").toString())
            );

            jogos.add(jogoAux);
        }
    }
}
