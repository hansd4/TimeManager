import java.awt.*;

public class CardLabel {
    private Color color;
    private String name;

    public CardLabel(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
