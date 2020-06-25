public class CreateStage<T extends Item> extends Stage {
    private String name;
    private boolean block;
    private double startBlock;
    private double blockedT = 0;
    private double starvedT = 0;
    private T item = null;
    private Storage<T> next;

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
        if (block) {
            this.block = false;
            blockedT += time - startBlock;
            move(new Event("Block", time - startBlock));
        }
    }

    public void makeItem(T item){
        if (this.item == null){
            this.item = item;
        }
    }

    public void move(Event event){
        item.recordLine(event);
        next.add(item);
        item = null;
    }

}
