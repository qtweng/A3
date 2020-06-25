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
    private CreateStage<Item> S0a = null;
    private CreateStage<Item> S0b = null;
    private FinishStage<Item> S1 = null;

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
        S0a = new CreateStage("S0a", Q01);
        S0b = new CreateStage("S0b", Q01);
        S1 = new FinishStage("S1", Q01);
    }

    // retrieve random value
    private static double getR(int type) {
        if (type == 2) {
            return 2 * m + 2 * n * (r.nextDouble() - 0.5);
        }
        return m + n * (r.nextDouble() - 0.5);
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
            schedule(S0a.getName());
            schedule(S0b.getName());
//            schedule(S1.getName());
            for (int i = 0; i < 2; i++) {
                process(eventPriorityQueue.poll());
            }

        } while (eventPriorityQueue.peek().getTime() < 10000000);
    }

    /**
     * @param action schedules action into eventPriorityQueue
     */
    private void schedule(String action) {
        if (action == "S0a") {
            eventPriorityQueue.add(new Event("S0a", eventPriorityQueue.peek().getTime(), getR(2)));
        } else if (action == "S0b") {
            eventPriorityQueue.add(new Event("S0b", eventPriorityQueue.peek().getTime(), getR(1)));
//        } else if (action == "S1") {
//            eventPriorityQueue.add(new Event("S1", Q01.peek().getLatest().getTime(), getR(1)));
        }
    }

    /**
     * @param event takes event actions
     */
    private void process(Event event) {
        if (event.getAction() == "S0a") {
            S0a.makeItem(new Item("S0a"));
            S0a.process(event);
        } else if (event.getAction() == "S0b") {
            S0b.makeItem(new Item("S0b"));
            S0b.process(event);
        } else if (event.getAction() == "S1") {
        }
    }
}