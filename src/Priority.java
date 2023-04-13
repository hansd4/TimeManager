import java.awt.*;

public enum Priority {
    NO,
    LOW,
    NORMAL,
    HIGH,
    URGENT;

    public static Color priorityToColor(Priority p) {
        return switch (p) {
            case LOW -> Color.GREEN;
            case NORMAL -> Color.YELLOW;
            case HIGH -> Color.RED;
            case URGENT -> Color.MAGENTA;
            default -> Color.GRAY;
        };
    }
}
