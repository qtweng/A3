public class InterStage<T extends Item> extends Stage {
    private final String name;
    private final Storage<T> prev;
    private final Storage<T> next;
    private boolean block;
    private boolean starve;
    private double startBlock;
    private double blockedT;
    private double startStarve;
    private double starvedT;
    private T item;

    public InterStage(String name, Storage prev, Storage next) {
        this.name = name;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void process(Event event) {
        if (next.status() == 1) {
            block(event.getTime());
        } else if (block == true && next.status() != 1) {
            unblock(event.getTime());
        } else {
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

    public void block(double time) {
        if (!block) {
            block = true;
            startBlock = time;
        }
    }

    public void unblock(double time) {
        if (block == true) {
            this.block = false;
            blockedT += time - startBlock;
            move(new Event("Block", time - startBlock));
        }

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
        next.add(item);
        item = null;
    }

}
