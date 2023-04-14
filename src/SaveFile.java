import java.io.Serial;
import java.util.ArrayList;

public class SaveFile implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // put data here
    private ArrayList<CardModel> cards;

    public SaveFile(ArrayList<CardModel> cards) {
        // set data here
        this.cards = cards;
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }
}
