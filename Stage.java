/**
 * Production stage of the production line
 *
 * @author QiTao Weng
 */
public class Production <T> {
    private String name;
    private Storage<T> prev;
    private Storage<T> next;
    private boolean starve;
    private boolean block;
    private double startBlock;
    private double blockedT;
    private T item;

    public Production(String name, Storage prev, Storage next) {
        this.name = name;
        this.prev = prev;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setStarve(boolean starve) {
        this.starve = starve;
    }

    public void take() {
        if (prev == null) {
            make();
        }
        item = prev.take();
    }

    public void give() {
        next.add(item);
    }
    private void make(){

    }
}
