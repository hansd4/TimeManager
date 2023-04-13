import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    // UI Components
    private JPanel mainPanel;
    private JButton tasksButton;
    private JButton Button2;
    private JButton Button3;
    private JButton QuitButton;
    private JTextArea textArea1;
    private JPanel dummyTextPanel;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel contentPanel;
    private JPanel actionPanel;
    private JPanel iconPanel;
    private JLabel mainLabel;
    private JSplitPane leftSplitPane;
    private JSplitPane mainSplitPane;
    private JPanel tasksPanel;
    private JScrollPane tasksScrollPane;
    private JButton addButton;
    private JLabel noCardsLabel;
    private JSplitPane pastDueList;
    private JPanel pastDueCards;
    private JSplitPane thisWeekList;
    private JSplitPane nextWeekList;
    private JPanel thisWeekCards;
    private JSplitPane laterList;
    private JPanel nextWeekCards;
    private JPanel laterCards;
    private JSplitPane noDueDateList;
    private JPanel noDueDateCards;
    private JSplitPane doneList;
    private JPanel doneCards;
    private JPanel[] cardLists;

    private TimeManager controller;

    public MainFrame(TimeManager controller) {
        // set up MainFrame
        super("TimeManager++");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setLocation(480, 270);
        this.pack();

        // initialize fields
        this.controller = controller;
        cardLists = new JPanel[6];
        cardLists[0] = pastDueCards;
        cardLists[1] = thisWeekCards;
        cardLists[2] = nextWeekCards;
        cardLists[3] = laterCards;
        cardLists[4] = noDueDateCards;
        cardLists[5] = doneCards;

        // set up components
        tasksScrollPane.setHorizontalScrollBar(new ScrollBar());
        for (JPanel c : cardLists) {
            ((JScrollPane) c.getParent().getParent()).setVerticalScrollBar(new ScrollBar());
        }
        displayCardLists();

        // listeners
        // button listeners
        addButton.addActionListener(e -> {
            controller.newCard();
        });
        tasksButton.addActionListener(e -> {
            switchPanels((JButton) e.getSource());
        });
        Button2.addActionListener(e -> {
            switchPanels((JButton) e.getSource());
        });
        Button3.addActionListener(e -> {
            switchPanels((JButton) e.getSource());
        });

        // listens for quit button (close)
        QuitButton.addActionListener(e -> quit());

        // listens for close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        // load data
        if (new File("1.save").exists()) {
            SaveFile file = (SaveFile) SaveManager.load("1.save");
            // load all data here
            textArea1.setText(file.getTextAreaText());
        }

        // start program
        this.setVisible(true);
    }

    public void switchPanels(JButton source) {
        LayoutManager layout = contentPanel.getLayout();
        CardLayout cardLayout = (CardLayout) layout;
        cardLayout.show(contentPanel, source.getText());
    }

    public void quit() {
        SaveFile s = new SaveFile(textArea1.getText());
        SaveManager.save(s, "1.save");
        System.exit(0);
    }

    public void displayCardLists() {
        for (JPanel c : cardLists) {
            c.getParent().getParent().getParent().setVisible(false);
        }
        CardList[] list = controller.getCardLists();
        // if no cards, display no cards warning (how to add them)
        noCardsLabel.setVisible(true);
        for (int i = 0; i < list.length; i++) {
            CardList c = list[i];
            System.out.println(c.getCards());
            if (c.getCards().size() > 0) { // only display card lists with more than one card inside
                noCardsLabel.setVisible(false);
                JPanel listGUI = cardLists[i];
                System.out.println("Successfully located listGUI at " + listGUI);
                System.out.println("C is null? " + (c == null));
                listGUI.getParent().getParent().getParent().setVisible(true);
                for (Card card : c.getCards()) {
                    System.out.println("Card " + card);
                    System.out.println("Card is null? " + (card == null));
                    listGUI.add(card.getMainPanel());
                }
            }
        }
    }
}
