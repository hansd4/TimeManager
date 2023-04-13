import javax.swing.*;
import java.awt.*;

public class CardLabel extends JLabel{
    private Color color;
    private String name;

    public CardLabel(Color color, String name) {
        super();
        this.color = color;
        this.name = name;
        this.setText(name);
        this.setBackground(color);
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
