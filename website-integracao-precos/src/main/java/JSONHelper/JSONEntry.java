package JSONHelper;

public class JSONEntry {
    public LojaEntry steam;

    public LojaEntry epic;

    public LojaEntry playstation;

    public JSONEntry()
    {
        steam = new LojaEntry();
        epic = new LojaEntry();
        playstation = new LojaEntry();
    }
}
