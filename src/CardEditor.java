import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.awt.event.KeyEvent.VK_ENTER;

public class CardEditor extends JFrame implements ActionListener {
    // UI Components
    private JPanel mainPanel;
    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JComboBox priorityBox;
    private JTextField monthField;
    private JTextField dayField;
    private JTextField yearField;
    private JTextField hourField;
    private JTextField minuteField;
    private JComboBox AMPMBox;
    private JSlider progressSlider;
    private JComboBox parentCardBox;
    private JButton addSubCardsButton;
    private JCheckBox subCardBox1;
    private JCheckBox subCardBox3;
    private JCheckBox subCardBox2;
    private JButton addLabelButton;
    private JCheckBox labelBox1;
    private JCheckBox labelBox2;
    private JCheckBox labelBox3;
    private JLabel deadlineDateLabel;
    private JLabel titleLabel;
    private JLabel descLabel;
    private JLabel priorityLabel;
    private JLabel deadlineTimeLabel;
    private JLabel progressLabel;
    private JLabel parentCardLabel;
    private JLabel subCardsLabel;
    private JPanel subCardsPanel;
    private ArrayList<JCheckBox> subCardBoxes;
    private ArrayList<Card> potentialSubCards;
    private JPanel labelPanel;
    private ArrayList<JCheckBox> labelBoxes;
    private ArrayList<CardLabel> potentialLabels;
    private JLabel labelLabel;

    // card being edited
    private Card card;

    // date format
    private SimpleDateFormat f;

    // ref to controller
    private TimeManager controller;

    public CardEditor(Card card, TimeManager controller) {
        super(card.getTitle());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setLocation(MouseInfo.getPointerInfo().getLocation());
        this.pack();

        this.card = card;
        this.controller = controller;
        this.f = new SimpleDateFormat("M d yy h mm a");

        this.subCardBoxes = new ArrayList<>();
        this.potentialSubCards = new ArrayList<>();
        this.labelBoxes = new ArrayList<>();
        this.potentialLabels = new ArrayList<>();

        // initialize GUI components
        priorityBox.setModel(new DefaultComboBoxModel(Priority.values()));
        parentCardBox.setModel(new DefaultComboBoxModel(controller.getCardsBesides(card).toArray()));
        progressSlider.setEnabled(card.getSubCards().size() == 0);

        // listeners
        // listens for close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
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

        updateGUI();
        this.setVisible(true);
    }

    public void quit() {
        try {
            updateCard();
            controller.update();
            dispose();
        } catch (ParseException ex) {
            JFrame warningWindow = new JFrame();
            JLabel warning = new JLabel();
            warning.setText("Invalid date!");
            warningWindow.add(warning);
            warningWindow.setLocation(MouseInfo.getPointerInfo().getLocation());
            warningWindow.pack();
            warningWindow.setVisible(true);
        }
    }

    public void updateGUI() {
        titleTextField.setText(card.getTitle());
        descriptionTextArea.setText(card.getDescription());
        priorityBox.setSelectedItem(card.getPriority());

        String[] deadlineFields;
        if (card.getDeadline() != null) {
            deadlineFields = f.format(card.getDeadline()).split(" ");
        } else { // default to a week from today
            Calendar oneWeekFromNow = Calendar.getInstance();
            oneWeekFromNow.add(Calendar.WEEK_OF_MONTH, 1);
            deadlineFields = f.format(oneWeekFromNow.getTime()).split(" ");
        }
        monthField.setText(deadlineFields[0]);
        dayField.setText(deadlineFields[1]);
        yearField.setText(deadlineFields[2]);
        hourField.setText(deadlineFields[3]);
        minuteField.setText(deadlineFields[4]);
        AMPMBox.setSelectedItem(deadlineFields[5]);

        progressSlider.setValue(card.getProgress());
        parentCardBox.setSelectedItem(card.getParentCard());
        potentialSubCards.clear();
        subCardBoxes.clear();
        for (Component comp : subCardsPanel.getComponents()) {
            if (comp instanceof JCheckBox) {
                subCardsPanel.remove(comp);
            }
        }
        for (Card c : controller.getCardsBesidesParent(card)) {
            JCheckBox subCardBox = new JCheckBox();
            subCardBox.setText(c.getTitle());
            subCardBox.addActionListener(this);
            subCardBox.setSelected(card.getSubCards().contains(c));
            subCardBox.setVisible(true);
            potentialSubCards.add(c);
            subCardBoxes.add(subCardBox);
            subCardsPanel.add(subCardBox);
        }
        potentialLabels.clear();
        labelBoxes.clear();
        for (Component comp : labelPanel.getComponents()) {
            if (comp instanceof JCheckBox) {
                labelPanel.remove(comp);
            }
        }
        for (CardLabel l : controller.getLabels()) {
            JCheckBox labelBox = new JCheckBox();
            labelBox.setText(l.getName());
            labelBox.setBackground(l.getColor());
            labelBox.addActionListener(this);
            labelBox.setSelected(card.getLabels().contains(l));
            labelBox.setVisible(true);
            potentialLabels.add(l);
            labelBoxes.add(labelBox);
            labelPanel.add(labelBox);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
        controller.update();
    }

    public void updateCard() throws ParseException {
        card.setPriority((Priority) priorityBox.getSelectedItem());
        card.setTitle(titleTextField.getText());
        setTitle(card.getTitle());
        card.setDescrption(descriptionTextArea.getText());

        String deadline = monthField.getText() + " " +
                dayField.getText() + " " +
                yearField.getText() + " " +
                hourField.getText() + " " +
                minuteField.getText() + " " +
                AMPMBox.getSelectedItem();
        card.setDeadline(f.parse(deadline));

        card.setProgress(progressSlider.getValue());
        if (parentCardBox.getSelectedItem() != null && !((Card) parentCardBox.getSelectedItem()).getTitle().equals("None")) {
            card.setParentCard((Card) parentCardBox.getSelectedItem());
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox box = (JCheckBox) e.getSource();
            if (subCardBoxes.contains(box)) {
                if (box.isSelected()) { // card to be added as subcard
                    card.addSubCard(potentialSubCards.get(subCardBoxes.indexOf(box)));
                } else { // card to be removed from subcards
                    card.removeSubCard(potentialSubCards.get(subCardBoxes.indexOf(box)));
                }
            } else if (labelBoxes.contains(box)) {
                if (box.isSelected()) {
                    card.addLabel(potentialLabels.get(labelBoxes.indexOf(box)));
                } else {
                    card.removeLabel(potentialLabels.get(labelBoxes.indexOf(box)));
                }
            }
        }
    }
}
