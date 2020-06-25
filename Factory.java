import java.util.PriorityQueue;
import java.util.Random;

/**
 * Factory class for production line operations
 *
 * @author QiTao Weng
 */

public class Factory {
    private static final Random r = new Random(100);
    private static PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>();
    private static int m;
    private static int n;
    private static int qMax;
    private Storage<Item> Q01 = null;
    private Storage<Item> Q12 = null;
    private Storage<Item> Q23 = null;
    private double S0aBlock;
    private double S0aBlockedT;
    private double S0bBlock;
    private double S0bBlockedT;
    private double S1Starve;
    private double S1StarvedT;

    /**
     * Constructor
     *
     * @param m    average processing time of an item in a stage, given as a program input
     * @param n    range of processing time in a stage, given as a program input.
     * @param qMax max size of storage queues
     */
    public Factory(int m, int n, int qMax) {
        Factory.m = m;
        Factory.n = n;
        Factory.qMax = qMax;
        Q01 = new Storage(qMax);
        Q12 = new Storage(qMax);
        Q23 = new Storage(qMax);
    }

    /**
     * Runs the Factory production
     */
    public void run() {
        // set end time
        eventPriorityQueue.add(new Event("end", 10000000, 0));
        eventPriorityQueue.add(new Event("start", 0, 0));
        // while loop so program runs continuously as long as time doesn't exceed 10,000,000
        do {
            schedule("S0a");
            schedule("S0b");
            schedule("S1");
            eventPriorityQueue.poll();
            eventPriorityQueue.poll();

        } while (eventPriorityQueue.peek().getTime() < 10000000);
    }

    // retrieve random value
    private static double getR(int type) {
        if (type == 2) {
            return 2 * m + 2 * n * (r.nextDouble() - 0.5);
        }
        return m + n * (r.nextDouble() - 0.5);
    }

    /**
     * @param action schedules action into eventPriorityQueue
     */
    private void schedule(String action) {
        if (action == "S0a") {
            eventPriorityQueue.add(new Event("S0a", 0, getR(2)));
        } else if (action == "S0b") {
            eventPriorityQueue.add(new Event("S0b", 0, getR(1)));
        } else if (action == "S1") {
            if (Q01.status() != -1) {
                eventPriorityQueue.add(new Event("S1", Q01.peek().getLatest().getTime(), getR(1)));
            } else {
                S1Starve = eventPriorityQueue.peek().getTime();
            }
        }

    }

    /**
     * @param event takes event actions
     * @return boolean of whether event is successful
     */
    private boolean process(Event event) {
        checkStatus();
        if (event.getAction() == "S0a") {
            if (Q01.status() != 1) {
                Item item = new Item("S0a");
                item.recordLine(event);
                Q01.add(item);
                return true;
            } else {
                S0aBlock = event.getTime();
                return false;
            }
        } else if (event.getAction() == "S0b") {
            if (Q01.status() != 1) {
                Item item = new Item("S0b");
                item.recordLine(event);
                Q01.add(item);
                return true;
            } else {
                S0bBlock = event.getTime();
                return false;
            }
        } else if (event.getAction() == "S1") {
            if (Q01.status() != -1) {
                Q01.take();
                return true;
            } else {
                S1Starve = event.getTime();
                return false;
            }
        }
        return false;
    }

    private void checkStatus() {
        if (Q01.status() != 1) {
            S0aBlockedT += eventPriorityQueue.peek().getTime() - S0aBlock;
        }
        if (Q12.status() != -1) {
            S1StarvedT += eventPriorityQueue.peek().getTime() - S1Starve;
        }
    }

    private void proceed() {
    }
}