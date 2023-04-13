import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
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

    // misc
    private SimpleDateFormat f;
    private TimeManager controller;

    public Card(Priority priority,
                String title,
                String description,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<CardLabel> labels,
                TimeManager controller) {
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

        this.f = new SimpleDateFormat("M/d @ h:mm a");
        this.controller = controller;

        loadGUI();
        addListeners();

        this.setVisible(true);
    }

    public Card(Priority priority,
                String title,
                String description,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<CardLabel> labels,
                Card parentCard,
                TimeManager controller) {
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

        this.f = new SimpleDateFormat("M/d @ h:mm a");
        this.controller = controller;

        // load GUI with data
        loadGUI();

        // listeners
        addListeners();

        this.setVisible(true);
    }

    public Card(TimeManager controller) {
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

        this.f = new SimpleDateFormat("M/d @ h:mm a");
        this.controller = controller;

        loadGUI();
        addListeners();

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
        System.out.println("Starting search in card " + this);
        for (Card c : subCards) {
            System.out.println("Searching through card " + c);
            subSubCards.addAll(c.getAllSubCards());
            System.out.println("Result now " + subSubCards);
        }
        subSubCards.addAll(subCards);
        System.out.println("Added all subcards. Result now " + subSubCards);
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
        System.out.println("For card " + this + ", returning that parent cards are: " + result);
        return result;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setPriority(Priority newPriority) {
        priority = newPriority;
        Color c = Priority.priorityToColor(priority);
        priorityLabelPanel.setBackground(c);
        deadlineBar.setForeground(c);
        progressBar.setForeground(c);
    }

    public void setTitle(String newTitle) {
        title = newTitle;
        mainButton.setText(title);
    }

    public void setDescrption(String newDescription) {
        description = newDescription;
    }

    public void setStartDate(Date newStartDate) {
        startDate = newStartDate;
    }

    public void setDeadline(Date newDeadline) {
        deadline = newDeadline;
        deadlineLabel.setText(f.format(deadline));
        updateDeadlineBar();
    }

    public void setProgress(int newProgress) {
        progress = newProgress;
        progressLabel.setText(progress + "%");
        updateProgressBar();
    }

    public void setSubCards(ArrayList<Card> newSubCards) {
        subCards = newSubCards;
    }

    public void addSubCard(Card subCard) {
        if (!subCards.contains(subCard)) {
            subCard.setParentCard(this);
            subCards.add(subCard);
        }
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
        cardCategoryPanel.add(l);
    }

    public void removeLabel(CardLabel l) {
        labels.remove(l);
        cardCategoryPanel.remove(l);
    }

    public void setParentCard(Card newParentCard) {
        parentCard = newParentCard;
    }

    public void setExpanded(boolean newExpanded) {
        expanded = newExpanded;
    }

    public void loadGUI() {
        mainButton.setText(title);
        if (deadline != null) {
            deadlineLabel.setText(f.format(deadline));
            updateDeadlineBar();
        }
        progressLabel.setText(progress + "%");
        updateProgressBar();
    }

    public void addListeners() {
        mainButton.addActionListener(e -> {
            new CardEditor(this, controller);
        });
        expandButton.addActionListener(e -> {
            expanded = !expanded;
            if (expanded) {
                expandButton.setText("v");
                for (Card sub : subCards) {
                    sub.setVisible(true);
                }
            } else {
                expandButton.setText("^");
                for (Card sub : subCards) {
                    sub.setVisible(false);
                }
            }
        });
    }

    public void updateDeadlineBar() {
        if (deadline.after(Calendar.getInstance().getTime())) { // not past due
            long totalTime = deadline.getTime() - startDate.getTime();
            long currentTime = Calendar.getInstance().getTimeInMillis() - startDate.getTime();
            deadlineBar.setValue((int) (((double) currentTime / totalTime) * 100));
        } else { // past due, 100%
            deadlineBar.setValue(100);
        }
    }

    public void updateProgressBar() {
        if (subCards.size() == 0) { // no need to calculate further
            progressBar.setValue(progress);
        } else { // calculate progress based on progress of subcards
            int totalProgress = 0;
            for (Card sub : subCards) {
                totalProgress += sub.progress;
            }
            progressBar.setValue((int) ((double) totalProgress / subCards.size()));
        }
    }

    @Override
    public String toString() {
        return title;
    }
}
