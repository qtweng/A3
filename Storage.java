import java.util.ArrayDeque;

/**
 * Storage class for inter-stage storages of the production line
 *
 * @author QiTao Weng
 */
// 25 minutes coding 20% design error 80% coding error
public class Storage<T> {
    private final int qMax;
    ArrayDeque<T> queue = new ArrayDeque();
    private double avgTime = 0;
    private double weightedTotalItems = 0;
    private int timeN = 1;
    private double itemTimer = 0;
    private volatile int status = -1; // -1 empty queue, 0 available, 1 max queue

    public Storage(int qMax) {
        this.qMax = qMax;
    }

    /**
     * Add to storage
     *
     * @param item produced item to store
     */
    public void add(T item) {
        // can only add if less than qMax
        if (queue.size() < qMax) {
            // sets storage to available
            status = 0;
            queue.add(item);
            // if max queue, set status
            if (queue.size() == qMax) {
                status = 1;
            }
        } else {
            throw new IllegalStateException("Trying to add to maxed storage");
        }
    }

    /**
     * @return T item at front of queue and removes from queue
     */
    public T take() {
        if (status != -1) {
            T front = queue.poll();
            // if last item removed set status to empty
            if (queue.size() == 0) {
                status = -1;
            } else {
                status = 0;
            }
            return front;
        } else {
            throw new IllegalStateException("Trying to take from empty storage");
        }
    }

    /**
     * @return int status of the storage: -1 empty, 0 available, 1 max
     */
    public int getStatus() {

        return status;
    }

    /**
     * calculates running average time on each item spent in storage
     *
     * @param time new time
     */
    public void calcAvgTime(double time) {
        avgTime = avgTime + (time - avgTime) / timeN;
        timeN++;
    }
    public void calcAvgItem(double time) {
       weightedTotalItems += (queue.size()*(time - itemTimer));
       itemTimer = time;
    }

    public double getAvgTime() {
        return avgTime;
    }

    public double getAvgItems() {
        return (weightedTotalItems / 10000000);
    }
}