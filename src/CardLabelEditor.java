import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CardLabelEditor extends JFrame implements ActionListener {
    private JTextField textField;
    private JButton colorButton;
    private JPanel mainPanel;
    private JLabel nameLabel;
    private JButton previewButton;
    private CardLabel cardLabel;

    private CardEditor editor;

    public CardLabelEditor(CardLabel cardLabel, CardEditor editor) {
        super(cardLabel.getText());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setLocation(MouseInfo.getPointerInfo().getLocation());
        this.pack();

        // previewButton
        previewButton.setText("Preview");
        previewButton.setBackground(cardLabel.getBackground());

        // listens for close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        // listens for color
        colorButton.addActionListener(this);
        // listens for enter key press (close)
        class QuitAction extends AbstractAction {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        }
        QuitAction q = new QuitAction();
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "quit");
        mainPanel.getActionMap().put("quit", q);
        for (Component comp : mainPanel.getComponents()) {
            if (!(comp instanceof JTextArea)) {
                JComponent c = (JComponent) comp;
                c.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "quit");
                c.getActionMap().put("quit", q);
            }
        }
        // listens for DEL key press (delete label)
        class DelAction extends AbstractAction {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement label deletion
                quit();
            }
        }
        DelAction d = new DelAction();
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete");
        mainPanel.getActionMap().put("delete", d);
        for (Component comp : mainPanel.getComponents()) {
            if (!(comp instanceof JTextArea)) {
                JComponent c = (JComponent) comp;
                c.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete");
                c.getActionMap().put("delete", d);
            }
        }

        this.cardLabel = cardLabel;
        this.editor = editor;

        this.setVisible(true);
    }

    public void quit() {
        cardLabel.setText(textField.getText());
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == colorButton) {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(null, "Label Color", cardLabel.getBackground());
            cardLabel.setBackground(color);
            previewButton.setBackground(color);
        }
    }
}
