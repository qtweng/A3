import java.util.LinkedList;

/**
 * Item class produced and used in stages of the production line
 *
 * @author QiTao Weng
 */

public class Item {
    LinkedList<Event> events = new LinkedList();
    private String uid = UniqueID.getID();

    public Item(String creator) {
        uid += creator.substring(creator.length() - 1);
    }

    @Override
    public String toString() {
        return uid;
    }

    /**
     * @param event adds time to the end of a linked list
     */
    public void recordLine(Event event) {
        events.add(event);
    }

    public Event getLatest() {
        return events.getLast();
    }
}
