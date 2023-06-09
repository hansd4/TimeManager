import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private JPanel labelPanel;
    private JScrollPane labelScrollPane;
    private JLabel parentLabel;

    // data
    private Priority priority;
    private String title;
    private String description;
    private Date startDate;
    private Date deadline; // CAN BE NULL
    private int progress;
    private ArrayList<Card> subCards;
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
                TimeManager controller) {
        super();

        // GUI setup
        ScrollBar s = new ScrollBar();
        s.setPreferredSize(new Dimension(1, 1));
        this.labelScrollPane.setHorizontalScrollBar(s);

        // data init
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = new ArrayList<>();
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
                Card parentCard,
                TimeManager controller) {
        super();

        // GUI setup
        ScrollBar s = new ScrollBar();
        s.setPreferredSize(new Dimension(1, 1));
        this.labelScrollPane.setHorizontalScrollBar(s);

        // data init
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = new ArrayList<>();
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

    public Card(Priority priority,
                String title,
                String description,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<Card> subCards,
                TimeManager controller) {
        super();

        // GUI setup
        ScrollBar s = new ScrollBar();
        s.setPreferredSize(new Dimension(1, 1));
        this.labelScrollPane.setHorizontalScrollBar(s);

        // data init
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = subCards;
        this.expanded = true;

        // init colors
        Color c = Priority.priorityToColor(priority);
        priorityLabelPanel.setBackground(c);
        deadlineBar.setForeground(c);
        progressBar.setForeground(c);

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

        // GUI setup
        ScrollBar s = new ScrollBar();
        s.setPreferredSize(new Dimension(1, 1));
        this.labelScrollPane.setHorizontalScrollBar(s);

        // data init
        this.priority = Priority.NO;
        this.title = "";
        this.description = "";
        this.startDate = Calendar.getInstance().getTime();
        this.deadline = null; // CAN BE NULL
        this.progress = 0;
        this.subCards = new ArrayList<>();
        this.parentCard = null;
        this.expanded = true;

        this.f = new SimpleDateFormat("M/d @ h:mm a");
        this.controller = controller;

        loadGUI();
        addListeners();

        this.setVisible(true);
    }

    public JPanel getMainPanel() {
        loadGUI();
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

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public void setStartDate(Date newStartDate) {
        startDate = newStartDate;
    }

    public void setDeadline(Date newDeadline) {
        if (newDeadline == null) {
            deadline = null;
            deadlineLabel.setText("");
            deadlineBar.setValue(0);
        } else {
            deadline = newDeadline;
            deadlineLabel.setText(f.format(deadline));
            updateDeadlineBar();
        }
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
            updateExpand();
        }
    }

    public void removeSubCard(Card subCard) {
        subCard.setParentCard(null);
        subCards.remove(subCard);
        updateExpand();
    }

    public void setParentCard(Card newParentCard) {
        updateParent();
        parentCard = newParentCard;
    }

    public void setExpanded(boolean newExpanded) {
        expanded = newExpanded;
    }

    public void loadGUI() {
        System.out.println(Arrays.toString(labelPanel.getComponents()));
        mainButton.setText(title);
        if (deadline != null) {
            deadlineLabel.setText(f.format(deadline));
            updateDeadlineBar();
        }
        progressLabel.setText(progress + "%");
        updateProgressBar();
        updateExpand();
        updateParent();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void addListeners() {
        mainButton.addActionListener(e -> {
            new CardEditor(this, controller);
        });
        expandButton.addActionListener(e -> {
            expanded = !expanded;
            updateExpand();
        });
    }

    public void updateParent() {
        if (parentCard != null) {
            parentLabel.setText("Subcard of " + parentCard.getTitle());
        } else {
            parentLabel.setText("");
        }
    }

    public void updateExpand() {
        if (expanded) {
            expandButton.setText("v");
            for (Card sub : getAllSubCards()) {
                sub.getMainPanel().setVisible(true);
            }
        } else {
            expandButton.setText("^");
            for (Card sub : getAllSubCards()) {
                sub.getMainPanel().setVisible(false);
            }
        }
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
            progress = progressBar.getValue();
        }
    }

    @Override
    public String toString() {
        return title;
    }
}
