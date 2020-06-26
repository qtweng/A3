/**
 * Time event class for PriorityQueue of processes.
 *
 * @author QiTao Weng
 */
public class Event implements Comparable<Event> {
    private final String action;
    private double time;

    /**
     * Constructor for new event
     *
     * @param action   string indicating production action
     * @param startT   start time of event
     * @param duration duration of event
     */
    public Event(String action, double startT, double duration) {
        this.action = action;
        time = startT + duration;
    }

    /**
     * Constructor to record events
     *
     * @param action
     * @param time
     */
    public Event(String action, double time) {
        this.time = time;
        this.action = action;
    }

    /**
     * @return string for action name
     */
    public String getAction() {
        return action;
    }

    /**
     * @return double for ending time
     */
    public double getTime() {
        return time;
    }
    
    /**
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Event o) {
        if (time < o.time) {
            return -1;
        } else if (time == o.time) {
            return 0;
        } else {
            return 1;
        }
    }
}
