/**
 * Production stage of the production line
 *
 * @author QiTao Weng
 */
public abstract class Stage<T extends Item> {

    public abstract void process(Event event);

    public abstract String getName();
}
