public class CreateStage<T extends Item> extends Stage {
    private final String name;
    private final double starvedT = 0;
    private final Storage<T> next;
    private boolean block;
    private double startBlock;
    private double blockedT = 0;
    private T item = null;

    public CreateStage(String name, Storage next) {
        this.name = name;
        this.next = next;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void process(Event event) {
        if (item != null) {
            if (next.getStatus() == 1) {
                block(event.getTime());
            } else if (block) {
                unblock(event.getTime());
            } else {
                move(event);
            }
        }
    }

    public void block(double time) {
        if (!block) {
            block = true;
            startBlock = time;
        }
    }

    public void unblock(double time) {
        if (block) {
            this.block = false;
            blockedT += time - startBlock;
            move(new Event(name, time - startBlock));
        }
    }

    public void makeItem(T item) {
        if (this.item == null) {
            this.item = item;
        }
    }

    public void move(Event event) {
        next.calcAvgItem(event.getTime());
        item.recordLine(event);
        next.add(item);
        item = null;
    }

    public double getBlockedT() {
        return blockedT;
    }

    public double getStarvedT() {
        return starvedT;
    }
}
