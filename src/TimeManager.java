import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class TimeManager {
    private MainFrame view;
    private CardList[] cardLists;
    private ArrayList<CardLabel> labels;

    public TimeManager() {
        cardLists = new CardList[6];
        cardLists[0] = new CardList("Past due");
        cardLists[1] = new CardList("This week");
        cardLists[2] = new CardList("Next week");
        cardLists[3] = new CardList("Later");
        cardLists[4] = new CardList("No due date");
        cardLists[5] = new CardList("Done");
        labels = new ArrayList<>();
        view = new MainFrame(this);

        try {
            Thread.sleep(60000);
            update();
        } catch (Exception ignored) {

        }
    }

    public void newCard() {
        Card newCard = new Card(this);
        new CardEditor(newCard, this);
        cardLists[0].getCards().add(newCard);
    }

    public void update() {
        view.displayCardLists();
    }

    public CardList[] getCardLists() {
        sortCards();
        return cardLists;
    }

    public ArrayList<CardLabel> getLabels() {
        return labels;
    }

    public ArrayList<Card> getCardsBesides(Card card) {
        System.out.println("Starting search");
        ArrayList<Card> result = new ArrayList<>();
        for (CardList list : cardLists) {
            System.out.println("Going through card list " + list);
            for (Card c : list.getCards()) {
                System.out.println("Going through card " + c);
                if (!card.equals(c) && !result.contains(c)) {
                    result.addAll(c.getAllSubCards(card));
                    if (c.getParentCard() == null || !c.getParentCard().equals(card)) {
                        result.add(c);
                    }
                    System.out.println("Finished card search. Result is now " + result);
                } else {
                    System.out.println("Denied, card to be avoided found");
                }
            }
        }
        Card placeholder = new Card(this);
        placeholder.setTitle("None");
        result.add(0, placeholder);
        return result;
    }

    public ArrayList<Card> getCardsBesidesParent(Card card) {
        System.out.println("Starting");
        ArrayList<Card> result = new ArrayList<>();
        ArrayList<Card> cardsToAvoid = card.getAllParentCards();
        for (CardList list : cardLists) {
            for (Card c : list.getCards()) {
                if (!cardsToAvoid.contains(c)) {
                    result.addAll(c.getAllSubCards());
                    result.add(c);
                    System.out.println("Adding ");
                }
            }
        }
        // remove duplicates
        for (int i = 0; i < result.size() - 1; i++) {
            Card c = result.get(i);
            for (int j = i + 1; j < result.size(); j++) {
                Card otherCard = result.get(j);
                if (c.equals(otherCard)) {
                    result.remove(j);
                    j--;
                }
            }
        }
        System.out.println("For card " + card + ", returning: " + result);
        return result;
    }

    private void sortCards() { // sorts all cards into their respective lists based on deadline and completion status
        for (CardList c : cardLists) {
            if (c.getCards().size() > 0) {
                String listTitle = c.getTitle();

                Calendar twoWeeksFromNow = Calendar.getInstance();
                twoWeeksFromNow.add(Calendar.WEEK_OF_MONTH, 2);
                Calendar oneWeekFromNow = Calendar.getInstance();
                oneWeekFromNow.add(Calendar.WEEK_OF_MONTH, 1);
                Date now = Calendar.getInstance().getTime();

                ArrayList<Card> cards = c.getCards();
                for (int i = 0; i < cards.size(); i++) {
                    Card card = cards.get(i);
                    Date deadDate = card.getDeadline();
                    int progress = card.getProgress();
                    boolean done = progress >= 100;

                    if (deadDate == null) { // no due date
                        if (!listTitle.equals("No due date")) { // card in wrong list, switching to no due date list
                            switchLists(card, c, cardLists[4]);
                            i--;
                        } // otherwise, keep card in list
                    } else if (done) { // card done, 100% progress
                        if (!listTitle.equals("Done")) { // card in wrong list, switching to done list
                            switchLists(card, c, cardLists[5]);
                            i--;
                        } // otherwise, keep card in list
                    } else { // time based, not done, has a due date. sort into time period
                        if (deadDate.after(twoWeeksFromNow.getTime())) { // more than 2 weeks. list later
                            if (!listTitle.equals("Later")) { // card in wrong list, switching to later list
                                switchLists(card, c, cardLists[3]);
                                i--;
                            }
                        } else if (deadDate.after(oneWeekFromNow.getTime())) { // more than 1 week. list next week
                            if (!listTitle.equals("Next week")) {
                                switchLists(card, c, cardLists[2]);
                                i--;
                            }
                        } else if (deadDate.after(now)) { // less than one week, still not due. list this week
                            if (!listTitle.equals("This week")) {
                                switchLists(card, c, cardLists[1]);
                                i--;
                            }
                        } else { // past due. list past due
                            if (!listTitle.equals("Past due")) { // card in wrong list, switching to past due
                                switchLists(card, c, cardLists[0]);
                                i--;
                            }
                        }
                    }
                }
            }
        }
    }

    private void switchLists(Card c, CardList list1, CardList list2) {
        list1.removeCard(c);
        list2.addCard(c);
    }
}
