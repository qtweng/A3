import java.util.ArrayList;

public class FinishStage<T extends Item> extends Stage {
    private final String name;
    private final Storage<T> prev;
    private final ArrayList<T> warehouse;
    private final double blockedT = 0;
    private boolean starve = true;
    private double startStarve;
    private double starvedT = 0;
    private T item;

    public FinishStage(String name, Storage prev, ArrayList warehouse) {
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
            if (prev.getStatus() == -1) {
                starve(time);
            } else {
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
        prev.calcAvgItem(time);
        item = prev.take();
        prev.getStatus();
        prev.calcAvgTime(time - item.getLatest().getTime()); // calculate running average for storage
    }

    public void move(Event event) {
        item.recordLine(event);
        warehouse.add(item);
        item = null;
    }

    public double getBlockedT() {
        return blockedT;
    }

    public double getStarvedT() {
        return starvedT;
    }
}
