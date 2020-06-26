import java.util.*;

/**
 * Factory class for production line operations
 *
 * @author QiTao Weng
 */

public class Factory {
    private static final Random r = new Random(100);
    private static final PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>();
    private static final ArrayList<Item> completed = new ArrayList<>();
    private static int m;
    private static int n;
    private static int qMax;
    private Storage<Item> q01 = null;
    private Storage<Item> q12 = null;
    private Storage<Item> q34 = null;
    private Storage<Item> q23 = null;
    private Storage<Item> q45 = null;
    private Storage<Item> q56 = null;
    private CreateStage<Item> s0a = null;
    private CreateStage<Item> s0b = null;
    private InterStage<Item> s1 = null;
    private InterStage<Item> s2 = null;
    private InterStage<Item> s3a = null;
    private InterStage<Item> s3b = null;
    private InterStage<Item> s4 = null;
    private InterStage<Item> s5a = null;
    private InterStage<Item> s5b = null;
    private FinishStage<Item> s6 = null;

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
        q01 = new Storage<Item>(qMax);
        q12 = new Storage<Item>(qMax);
        q34 = new Storage<Item>(qMax);
        q23 = new Storage<Item>(qMax);
        q45 = new Storage<Item>(qMax);
        q56 = new Storage<Item>(qMax);
        s0a = new CreateStage<Item>("S0a", q01);
        s0b = new CreateStage<Item>("S0b", q01);
        s1 = new InterStage<Item>("S1", q01, q12);
        s2 = new InterStage<Item>("S2", q12, q23);
        s3a = new InterStage<Item>("S3a", q23, q34);
        s3b = new InterStage<Item>("S3b", q23, q34);
        s4 = new InterStage<Item>("S4", q34, q45);
        s5a = new InterStage<Item>("S5a", q45, q56);
        s5b = new InterStage<Item>("S5b", q23, q56);
        s6 = new FinishStage<Item>("S6", q56, completed);
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
        schedule(s0a.getName());
        schedule(s0b.getName());
        schedule(s1.getName());
        schedule(s2.getName());
        schedule(s3a.getName());
        schedule(s3b.getName());
        schedule(s4.getName());
        schedule(s5a.getName());
        schedule(s5b.getName());
        schedule(s6.getName());
        do {
            schedule(s0a.getName());
            schedule(s0b.getName());
            schedule(s1.getName());
            schedule(s2.getName());
            schedule(s3a.getName());
            schedule(s3b.getName());
            schedule(s4.getName());
            schedule(s5a.getName());
            schedule(s5b.getName());
            schedule(s6.getName());

            for (int i = 0; i < 18; i++) {
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
        } else if (action == "S1") {
            eventPriorityQueue.add(s1.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S1", eventPriorityQueue.peek().getTime(), getR(1)));
        } else if (action == "S2") {
            eventPriorityQueue.add(s2.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S2", eventPriorityQueue.peek().getTime(), getR(1)));
        } else if (action == "S3a") {
            eventPriorityQueue.add(s3a.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S3a", eventPriorityQueue.peek().getTime(), getR(2)));
        } else if (action == "S3b") {
            eventPriorityQueue.add(s3b.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S3b", eventPriorityQueue.peek().getTime(), getR(2)));
        } else if (action == "S4") {
            eventPriorityQueue.add(s4.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S4", eventPriorityQueue.peek().getTime(), getR(1)));
        } else if (action == "S5a") {
            eventPriorityQueue.add(s5a.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S5a", eventPriorityQueue.peek().getTime(), getR(2)));
        } else if (action == "S5b") {
            eventPriorityQueue.add(s3b.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S5b", eventPriorityQueue.peek().getTime(), getR(2)));
        }  else if (action == "S6") {
            eventPriorityQueue.add(s6.ask(eventPriorityQueue.peek().getTime()));
            eventPriorityQueue.add(new Event("S6", eventPriorityQueue.peek().getTime(), getR(1)));
        }
    }

    /**
     * @param event takes event actions
     */
    private void process(Event event) {
        if (event.getAction().equals("S0a")) {
            s0a.makeItem(new Item("S0a"));
            s0a.process(event);
        } else if (event.getAction().equals("S0b")) {
            s0b.makeItem(new Item("S0b"));
            s0b.process(event);
        } else if (event.getAction().equals("S1")) {
            s1.process(event);
        } else if (event.getAction().equals("S2")) {
            s2.process(event);
        } else if (event.getAction().equals("S3a")) {
            s3a.process(event);
        } else if (event.getAction().equals("S3b")) {
            s3b.process(event);
        } else if (event.getAction().equals("S4")) {
            s4.process(event);
        } else if (event.getAction().equals("S5a")) {
            s5a.process(event);
        } else if (event.getAction().equals("S5b")) {
            s5b.process(event);
        } else if (event.getAction() == "S6") {
            s6.process(event);
        }
    }

    public void printInfo() {
        System.out.println("Total completed: " + completed.size());
        System.out.println("Production Stations:" );
        System.out.println("---------------------------------------" );
        String[][] production = new String[11][];
        production[0] = new String[] { "Stage:", "Work[%]", "Starve[t]", "Block[t]" };
        production[1] = new String[] { "S0a" , String.format( "%.2f%%",((10000000 - (s0a.getBlockedT() + s0a.getStarvedT()))/ 100000)), String.format("%.2f", s0a.getStarvedT()), String.format("%.2f", s0a.getBlockedT())};
        production[2] = new String[] { "S0b" , String.format( "%.2f%%",((10000000 - (s0b.getBlockedT() + s0b.getStarvedT()))/ 100000)), String.format("%.2f", s0b.getStarvedT()), String.format("%.2f", s0b.getBlockedT())};
        production[3] = new String[] { "S1" , String.format( "%.2f%%",((10000000 - (s1.getBlockedT() + s1.getStarvedT()))/ 100000)), String.format("%.2f", s1.getStarvedT()), String.format("%.2f", s1.getBlockedT())};
        production[4] = new String[] { "S2" , String.format( "%.2f%%",((10000000 - (s2.getBlockedT() + s2.getStarvedT()))/ 100000)), String.format("%.2f", s2.getStarvedT()), String.format("%.2f", s2.getBlockedT())};
        production[5] = new String[] { "S3a" , String.format( "%.2f%%",((10000000 - (s3a.getBlockedT() + s3a.getStarvedT()))/ 100000)), String.format("%.2f", s3a.getStarvedT()), String.format("%.2f", s3a.getBlockedT())};
        production[6] = new String[] { "S3b" , String.format( "%.2f%%",((10000000 - (s3b.getBlockedT() + s3b.getStarvedT()))/ 100000)), String.format("%.2f", s3b.getStarvedT()), String.format("%.2f", s3b.getBlockedT())};
        production[7] = new String[] { "S4" , String.format( "%.2f%%",((10000000 - (s4.getBlockedT() + s4.getStarvedT()))/ 100000)), String.format("%.2f", s4.getStarvedT()), String.format("%.2f", s4.getBlockedT())};
        production[8] = new String[] { "S5a" , String.format( "%.2f%%",((10000000 - (s5a.getBlockedT() + s5a.getStarvedT()))/ 100000)), String.format("%.2f", s5a.getStarvedT()), String.format("%.2f", s5a.getBlockedT())};
        production[9] = new String[] { "S5b" , String.format( "%.2f%%",((10000000 - (s5b.getBlockedT() + s5b.getStarvedT()))/ 100000)), String.format("%.2f", s5b.getStarvedT()), String.format("%.2f", s5b.getBlockedT())};
        production[10] = new String[] { "S6" , String.format( "%.2f%%",((10000000 - (s6.getBlockedT() + s6.getStarvedT()))/ 100000)), String.format("%.2f", s6.getStarvedT()), String.format("%.2f", s6.getBlockedT())};
        for (Object[] row : production) {
            System.out.format("%-15s%-15s%-15s%-15s\n", row);
        }
        System.out.println("Storage Queues:" );
        System.out.println("---------------------------------------" );
        String[][] storage = new String[7][];
        storage[0] = new String[] { "Store:", "AvgTime[t]", "AvgItems"};
        storage[1] = new String[] { "Q01" , String.format( "%.2f", q01.getAvgTime()), String.format( "%.2f", q01.getAvgItems())};
        storage[2] = new String[] { "Q12" , String.format( "%.2f", q12.getAvgTime()), String.format( "%.2f", q12.getAvgItems())};
        storage[3] = new String[] { "Q23" , String.format( "%.2f", q23.getAvgTime()), String.format( "%.2f", q23.getAvgItems())};
        storage[4] = new String[] { "Q34" , String.format( "%.2f", q34.getAvgTime()), String.format( "%.2f", q34.getAvgItems())};
        storage[5] = new String[] { "Q45" , String.format( "%.2f", q45.getAvgTime()), String.format( "%.2f", q45.getAvgItems())};
        storage[6] = new String[] { "Q56" , String.format( "%.2f", q56.getAvgTime()), String.format( "%.2f", q56.getAvgItems())};
        for (Object[] row : storage) {
            System.out.format("%-15s%-15s%-15s\n", row);
        }
        System.out.println("Production Paths:" );
        System.out.println("---------------------------------------" );
        System.out.println("S3a -> S5a: " + String.valueOf(completed.stream().filter(x -> "S3a".equals(x.events.get(3).getAction())).filter(y -> "S5a".equals(y.events.get(5).getAction())).count()));
        System.out.println("S3a -> S5b: " + String.valueOf(completed.stream().filter(x -> "S3a".equals(x.events.get(3).getAction())).filter(y -> "S5b".equals(y.events.get(5).getAction())).count()));
        System.out.println("S3b -> S5a: " + String.valueOf(completed.stream().filter(x -> "S3b".equals(x.events.get(3).getAction())).filter(y -> "S5a".equals(y.events.get(5).getAction())).count()));
        System.out.println("S3b -> S5b: " + String.valueOf(completed.stream().filter(x -> "S3b".equals(x.events.get(3).getAction())).filter(y -> "S5b".equals(y.events.get(5).getAction())).count()));
        System.out.println("Production Items:" );
        System.out.println("---------------------------------------" );
        System.out.println("S0a: " + String.valueOf(completed.stream().filter(x -> 'a' == (x.toString().charAt(6))).count()));
        System.out.println("S0b: " + String.valueOf(completed.stream().filter(x -> 'b' == (x.toString().charAt(6))).count()));
    }
}