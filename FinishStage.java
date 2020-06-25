import java.util.LinkedList;

public class FinishStage<T extends Item>extends Stage {
    private final String name;
    private boolean starve = true;
    private double startStarve;
    private double starvedT;
    private T item;
    private Storage<T> prev;
    private LinkedList<T> warehouse;

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
        if (item == null) {
            if (prev.status() == -1 && !starve){
                starve(event.getTime());
            } else {
                unstarve(event.getTime());
                receive(event);
            }
        }
    }
    public void starve(double time){
        if(!starve) {
            starve = true;
            startStarve = time;
        }
    }

    public void unstarve(double time) {
        if (starve) {
            this.starve = false;
            starvedT += time - startStarve;
            receive(new Event("Starve", time - startStarve));
        }
    }

    public void receive(Event event) {
        item = prev.take();
        prev.calcAvgTime(event.getTime() - item.getLatest().getTime()); // calculate running average for storage
    }
    public void move(Event event){
        item.recordLine(event);
        warehouse.add(item);
        item = null;
    }

}
