public class FinishStage<T extends Item>extends Stage {
    private final String name;
    private boolean starve;
    private double startStarve;
    private double starvedT;
    private T item;
    private final Storage<T> prev;

    public FinishStage(String name, Storage prev) {
        this.name = name;
        this.prev = prev;
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


}
