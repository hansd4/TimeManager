import java.util.Date;

public class PastDueList extends CardList {
    public PastDueList(String title) {
        super(title);
    }

    @Override
    public boolean validDeadline(Date deadline) {

    }
}
