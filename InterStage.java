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

    }

    public void setStarve(boolean starve) {
        this.starve = starve;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }


}
