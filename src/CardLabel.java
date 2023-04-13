import javax.swing.*;
import java.awt.*;

public class CardLabel extends JButton {
    public CardLabel() {
        super();
        setText("Label");
        setPreferredSize(new Dimension(78, 18));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.BOLD, 10));
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
