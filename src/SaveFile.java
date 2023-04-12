import java.io.Serial;

public class SaveFile implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // put data here
    private String textAreaText;

    public SaveFile(String textAreaText) {
        // set data here
        this.textAreaText = textAreaText;
    }

    public String getTextAreaText() {
        return textAreaText;
    }
}
