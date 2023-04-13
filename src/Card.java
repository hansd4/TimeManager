import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Card extends JPanel {
    // UI components
    private JPanel mainPanel;
    private JSplitPane prioritySplitPane;
    private JButton mainButton;
    private JSplitPane labelProgressSplitPane;
    private JSplitPane progressSplitPane;
    private JSplitPane deadlineSplitPane;
    private JLabel deadlineLabel;
    private JProgressBar deadlineBar;
    private JSplitPane doneSplitPane;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JPanel priorityLabelPanel;
    private JSplitPane labelSplitPane;
    private JPanel cardCategoryPanel;
    private JButton expandButton;

    // data
    private Priority priority;
    private String title;
    private String description;
    private Date startDate;
    private Date deadline; // CAN BE NULL
    private int progress;
    private ArrayList<Card> subCards;
    private ArrayList<CardLabel> labels;
    private Card parentCard;
    private boolean expanded;

    public Card(Priority priority,
                String title,
                String description,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<CardLabel> labels) {
        super();

        // data init
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = new ArrayList<>();
        this.labels = labels;
        this.parentCard = null;
        this.expanded = false;

        // TODO: init card GUI with data

        this.setVisible(true);
    }

    public Card(Priority priority,
                String title,
                String description,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<CardLabel> labels,
                Card parentCard) {
        super();

        // data init
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = new ArrayList<>();
        this.labels = labels;
        this.parentCard = parentCard;
        this.expanded = false;

        // TODO: init card GUI with data

        this.setVisible(true);
    }

    public Card() {
        super();

        // data init
        this.priority = Priority.NO;
        this.title = "";
        this.description = "";
        this.startDate = Calendar.getInstance().getTime();
        this.deadline = null; // CAN BE NULL
        this.progress = 0;
        this.subCards = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.parentCard = null;
        this.expanded = false;

        // TODO: init card GUI with data

        this.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
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

    public ArrayList<Card> getSubCards() {
        return subCards;
    }

    public ArrayList<Card> getAllSubCards() {
        ArrayList<Card> subSubCards = new ArrayList<>();
        for (Card c : subCards) {
            subSubCards.addAll(c.getAllSubCards());
        }
        subSubCards.addAll(subCards);
        return subSubCards;
    }

    public ArrayList<Card> getAllSubCards(Card cardToAvoid) {
        ArrayList<Card> subSubCards = new ArrayList<>();
        if (!this.equals(cardToAvoid)) {
            for (Card c : subCards) {
                if (!c.equals(cardToAvoid)) {
                    subSubCards.addAll(c.getAllSubCards());
                }
            }
            subSubCards.addAll(subCards);
        }
        return subSubCards;
    }

    public ArrayList<CardLabel> getLabels() {
        return labels;
    }

    public Card getParentCard() {
        return parentCard;
    }

    public ArrayList<Card> getAllParentCards() {
        ArrayList<Card> result = new ArrayList<>();
        if (parentCard != null) {
            result.add(parentCard);
            result.addAll(parentCard.getAllParentCards());
        }
        return result;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setPriority(Priority newPriority) {
        priority = newPriority;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setDescrption(String newDescription) {
        description = newDescription;
    }

    public void setStartDate(Date newStartDate) {
        startDate = newStartDate;
    }

    public void setDeadline(Date newDeadline) {
        deadline = newDeadline;
    }

    public void setProgress(int newProgress) {
        progress = newProgress;
    }

    public void setSubCards(ArrayList<Card> newSubCards) {
        subCards = newSubCards;
    }

    public void addSubCard(Card subCard) {
        subCard.setParentCard(this);
        subCards.add(subCard);
    }

    public void removeSubCard(Card subCard) {
        subCard.setParentCard(null);
        subCards.remove(subCard);
    }

    public void setLabels(ArrayList<CardLabel> newLabels) {
        labels = newLabels;
    }

    public void addLabel(CardLabel l) {
        labels.add(l);
    }

    public void removeLabel(CardLabel l) {
        labels.remove(l);
    }

    public void setParentCard(Card newParentCard) {
        parentCard = newParentCard;
    }

    public void setExpanded(boolean newExpanded) {
        expanded = newExpanded;
    }

    @Override
    public String toString() {
        return title;
    }
}
