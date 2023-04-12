import java.util.ArrayList;
import java.util.Date;

public class TimeManager {
    private MainFrame view;
    private CardList[] cardLists;

    public TimeManager() {
        cardLists = new CardList[5];
        cardLists[0] = new CardList("Past due");
        cardLists[1] = new CardList("This week");
        cardLists[2] = new CardList("Next week");
        cardLists[3] = new CardList("Later");
        cardLists[4] = new CardList("No due date");
        cardLists[5] = new CardList("Done");
        view = new MainFrame(this);
    }

    public CardList[] getCardLists() {
        sortCards();
        return cardLists;
    }

    private void sortCards() {
        for (CardList c : cardLists) {
            if (c.getCards().size() > 0) {
                ArrayList<Card> cards = c.getCards();
                for (Card card : cards) {
                    Date deadDate = card.getDeadline();
                    if (deadDate != null) {

                    } else {
                        c.removeCard(card);
                        cardLists[4].addCard(card);
                    }
                    if
                }
            }
        }
    }
}
