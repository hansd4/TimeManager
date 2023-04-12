import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainFrame extends JFrame {
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

    public MainFrame() {
        // set up MainFrame
        super("TimeManager++");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setLocation(480, 270);
        this.pack();

        // set up components

        // listeners
        // button listeners
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

        // TESTING
        for (int i = 0; i < 100; i++) {
            tasksPanel.add(new Card().getMainPanel());
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

}
