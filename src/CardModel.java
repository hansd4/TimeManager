import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CardModel implements Serializable { // Card Model, for saving. No GUI involved whatsoever
    private Priority priority;
    private String title;
    private String description;
    private Date startDate;
    private Date deadline; // CAN BE NULL
    private int progress;
    private ArrayList<CardModel> subCardsModel;
    private CardModel parentCardModel;
    private boolean expanded;

    public CardModel(Priority priority,
                     String title,
                     String description,
                     Date startDate,
                     Date deadline,
                     int progress,
                     ArrayList<Card> subCards,
                     Card parentCard,
                     boolean expanded) {
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.progress = progress;

        subCardsModel = new ArrayList<CardModel>();
        for (Card c : subCards) {
            subCardsModel.add(TimeManager.toCardModel(c));
        }

        this.expanded = expanded;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getProgress() {
        return progress;
    }

    public ArrayList<CardModel> getSubCardsModel() {
        return subCardsModel;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
