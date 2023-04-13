import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private Date startDate;
    private Date deadline; // CAN BE NULL
    private int progress;
    private ArrayList<Card> subCards;
    private ArrayList<CardLabel> labels;
    private boolean expanded;

    public Card(Priority priority,
                String title,
                Date startDate,
                Date deadline,
                int progress,
                ArrayList<CardLabel> labels) {
        super();

        // data init
        this.priority = priority;
        this.title = title;
        this.startDate = startDate;
        this.deadline = deadline; // CAN BE NULL
        this.progress = progress;
        this.subCards = new ArrayList<>();
        this.labels = labels;
        this.expanded = false;

        this.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getProgress() {
        return progress;
    }
}
