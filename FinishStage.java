import java.util.LinkedList;

public class FinishStage<T extends Item> extends Stage {
    private final String name;
    private final Storage<T> prev;
    private final LinkedList<T> warehouse;
    private boolean starve = true;
    private double startStarve;
    private double starvedT;
    private T item;

    public FinishStage(String name, Storage prev, LinkedList warehouse) {
        this.name = name;
        this.prev = prev;
        this.warehouse = warehouse;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void process(Event event) {
        if (item != null) {
            move(event);
        }
    }

    public Event ask(double time) {
        if (item == null) {
            if (prev.status() == -1 && !starve) {
                starve(time);
            } else if (prev.status() != -1) {
                unstarve(time);
                receive(time);
            }
        }
        return new Event(name, time, 0);
    }

    public void starve(double time) {
        if (!starve) {
            starve = true;
            startStarve = time;
        }
    }

    public void unstarve(double time) {
        if (starve) {
            this.starve = false;
            starvedT += time - startStarve;
        }
    }

    public void receive(double time) {
        item = prev.take();
        prev.status();
        prev.calcAvgTime(time - item.getLatest().getTime()); // calculate running average for storage
    }

    public void move(Event event) {
        item.recordLine(event);
        warehouse.add(item);
        item = null;
    }

}
