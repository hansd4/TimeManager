import java.util.ArrayList;
import java.util.Date;

public class CardList {
    private String title;
    private ArrayList<Card> cards;

    public CardList(String title) {
        this.title = title;
        this.cards = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> newCards) {
        cards = newCards;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
