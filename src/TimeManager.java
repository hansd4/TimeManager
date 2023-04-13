import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class TimeManager {
    private MainFrame view;
    private CardList[] cardLists;

    public TimeManager() {
        cardLists = new CardList[6];
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
                Iterator<Card> cardIterator = cards.iterator();
                while (cardIterator.hasNext()) {
                    Card card = cardIterator.next();
                    Date deadDate = card.getDeadline();
                    int progress = card.getProgress();
                    boolean done = progress >= 100;

                    if (deadDate == null) { // no due date
                        if (!listTitle.equals("No due date")) { // card in wrong list, switching to no due date list
                            switchLists(card, c, cardLists[4]);
                        } // otherwise, keep card in list
                    } else if (done) { // card done, 100% progress
                        if (!listTitle.equals("Done")) { // card in wrong list, switching to done list
                            switchLists(card, c, cardLists[5]);
                        } // otherwise, keep card in list
                    } else { // time based, not done, has a due date. sort into time period
                        if (deadDate.after(twoWeeksFromNow.getTime())) { // more than 2 weeks. list later
                            if (!listTitle.equals("Later")) { // card in wrong list, switching to later list
                                switchLists(card, c, cardLists[3]);
                            }
                        } else if (deadDate.after(oneWeekFromNow.getTime())) { // more than 1 week. list next week
                            if (!listTitle.equals("Next week")) {
                                switchLists(card, c, cardLists[2]);
                            }
                        } else if (deadDate.after(now)) { // less than one week, still not due. list this week
                            if (!listTitle.equals("This week")) {
                                switchLists(card, c, cardLists[1]);
                            }
                        } else { // past due. list past due
                            if (!listTitle.equals("Past due")) { // card in wrong list, switching to past due
                                switchLists(card, c, cardLists[0]);
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
