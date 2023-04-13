import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardLabel extends JButton implements Cloneable {
    private CardLabelEditor editor;
    private int identifier;
    private ActionListener listener;

    public CardLabel() {
        super();
        setText("Label");
        setPreferredSize(new Dimension(78, 18));
        setBackground(Color.BLACK);
        setForeground(Color.BLACK);
        setFont(new Font("Roboto", Font.BOLD, 10));
        setBorderPainted(false);
        setFocusPainted(false);
        editor = new CardLabelEditor(this);
        identifier = hashCode();
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor = new CardLabelEditor(CardLabel.this);
            }
        };
        addActionListener(listener);
    }

    public CardLabel(CardEditor cardEditor) {
        super();
        setText("Label");
        setPreferredSize(new Dimension(78, 18));
        setBackground(Color.BLACK);
        setForeground(Color.BLACK);
        setFont(new Font("Roboto", Font.BOLD, 10));
        setBorderPainted(false);
        setFocusPainted(false);
        editor = new CardLabelEditor(this, cardEditor);
        identifier = hashCode();
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor = new CardLabelEditor(CardLabel.this);
            }
        };
        addActionListener(listener);
    }

    @Override
    public CardLabel clone() {
        try {
            CardLabel clone = (CardLabel) super.clone();
            clone.setText(getText());
            clone.setPreferredSize(getPreferredSize());
            clone.setBackground(getBackground());
            clone.setForeground(getForeground());
            clone.setFont(getFont());
            clone.setBorderPainted(isBorderPainted());
            clone.setFocusPainted(isFocusPainted());
            clone.editor = this.editor; // same instance for all of one type of label
            clone.identifier = this.identifier; // same id for same type of label
            clone.listener = this.listener;
            clone.addActionListener(clone.listener);
            return clone;
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CardLabel) {
            CardLabel otherLabel = (CardLabel) other;
            return identifier == otherLabel.identifier;
        }
        return false; // not same class
    }

    @Override
    public String toString() {
        return "Label " + getText() + " in color " + getBackground() + " with hashCode: " + hashCode();
    }
}
