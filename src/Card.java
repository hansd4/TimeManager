import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class Card extends JPanel {
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
    private JPanel cardLabelPane;
    private JPanel priorityLabelPanel;

    public Card() {
        super();
        this.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
