import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // TODO: add closing functionality
        this.setContentPane(mainPanel);
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
        parentCardBox.setModel(new DefaultComboBoxModel(controller.getCardsBesides(card).toArray(new Card[0])));

        // listeners
        // listens for close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updateCard();
                controller.update();
                dispose();
            }
        });

        updateGUI();
        this.setVisible(true);
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

    public void updateCard() {
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
        try {
            card.setDeadline(f.parse(deadline));
        } catch (Exception e) { // TODO: warn user if date invalid, don't take it
            System.out.println("Date could not be parsed. Exception: " + e.getMessage());
        }

        card.setProgress(progressSlider.getValue());

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