public class InterStage<T extends Item> extends Stage {
    private final String name;
    private boolean block;
    private boolean starve;
    private double startBlock;
    private double blockedT;
    private double startStarve;
    private double starvedT;
    private T item;
    private final Storage<T> prev;
    private final Storage<T> next;

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
        } else if (block == true && next.status() != 1){
            unblock(event.getTime());
        } else {
            move(event);
        }
    }
    public void block(double time) {
        if(!block) {
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

    public void move(Event event){
        item.recordLine(event);
        next.add(item);
        item = null;
    }

}
